package com.foodscan.WsHelper.helper;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOHistoryData;
import com.foodscan.WsHelper.model.DTOLoginData;
import com.foodscan.WsHelper.model.DTOProductDetailsData;
import com.foodscan.WsHelper.model.DTORefreshTokenData;
import com.foodscan.WsHelper.model.DTOResponse;
import com.foodscan.WsHelper.model.DTOUserFavouriteData;

import dmax.dialog.SpotsDialog;

/**
 * WebserviceWrapper class to make calls for all the api.
 */
public class WebserviceWrapper {

    private static final String TAG = WebserviceWrapper.class.getSimpleName();

    private Attribute   attribute;
    private WebserviceResponse webserviceResponse;
    private Context mContext;
    private ContentValues contentValues;
    private FileUploadInput fileUploadInput;

    //PROFILE_1500374622.png

    boolean displayProgress = false;
    String dialogMessage = "";

    private final int LOGIN = 0, REFRESH_TOKEN = 1, REGISTRATION = 2, PRODUCT_DETAILS = 3, HISTORY = 4, REMOVE_FROM_HISTORY = 5, FAVOURITE = 6, USER_FAVOURITE = 7,
            EDIT_PROFILE = 8, CHANGE_PASSWORD = 9, FORGET_PASSWORD = 10;


    public interface WebserviceResponse {
        public void onResponse(int apiCode, Object object, Exception error);
    }

    public WebserviceWrapper(Context context, ContentValues contentValues, WebserviceResponse listener, boolean displayProgress, String dialogMessage) {
        this.contentValues = contentValues;
        this.webserviceResponse = listener;
        this.mContext = context;
        this.displayProgress = displayProgress;
        this.dialogMessage = dialogMessage;
    }


    public WebserviceWrapper(Context context, FileUploadInput fileUploadInput, WebserviceResponse listener, boolean displayProgress, String dialogMessage) {
        this.fileUploadInput = fileUploadInput;
        this.webserviceResponse = listener;
        this.mContext = context;
        this.displayProgress = displayProgress;
        this.dialogMessage = dialogMessage;
    }


    public WebserviceWrapper(Context context, Attribute attribute, WebserviceResponse listener, boolean displayProgress, String dialogMessage) {
        this.attribute = attribute;
        this.webserviceResponse = listener;
        this.mContext = context;
        this.displayProgress = displayProgress;
        this.dialogMessage = dialogMessage;
    }

    public enum WEB_CALLID {
        LOGIN(0), REFRESH_TOKEN(1), REGISTRATION(2), PRODUCT_DETAILS(3), HISTORY(4), REMOVE_FROM_HISTORY(5), FAVOURITE(6), USER_FAVOURITE(7),
        EDIT_PROFILE(8), CHANGE_PASSWORD(9), FORGET_PASSWORD(10);

        int callId;

        private WEB_CALLID(int s) {
            callId = s;
        }

        public final int getTypeCode() {
            return callId;
        }
    }


    public class WebserviceCaller extends AsyncTask<Integer, Void, Object> {

        public int currentApiCode;
        private Exception exception;

        SpotsDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (displayProgress) {

                if (dialog == null) {
                    dialog = new SpotsDialog(mContext, dialogMessage, R.style.Custom);

                    dialog.show();
                }
            }
        }

        @Override
        protected Object doInBackground(Integer... params) {
            Object responseObject = null;
            currentApiCode = params[0];
            try {
                switch (currentApiCode) {
                    case LOGIN: {

                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.LOGIN, mContext).execute(attribute, DTOLoginData.class, null);
                    }
                    break;

                    case REFRESH_TOKEN: {

                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.REFRESH_TOKEN, mContext).execute(attribute, DTORefreshTokenData.class, null);
                    }
                    break;

                    case REGISTRATION: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.REGISTRATION, mContext).execute(attribute, DTOLoginData.class, null);
                    }
                    break;

                    case PRODUCT_DETAILS: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.GET_PRODUCT_DETAILS, mContext).execute(attribute, DTOProductDetailsData.class, null);
                    }
                    break;

                    case HISTORY: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.GET_USER_HISTORY, mContext).execute(attribute, DTOHistoryData.class, null);
                    }
                    break;

                    case REMOVE_FROM_HISTORY: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.REMOVE_PRODUCT_FROM_HISTORY, mContext).execute(attribute, DTOResponse.class, null);
                    }
                    break;

                    case FAVOURITE: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.ADD_TO_FAVOURITE, mContext).execute(attribute, DTOResponse.class, null);
                    }
                    break;

                    case USER_FAVOURITE: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.GET_USER_FAVOURITE, mContext).execute(attribute, DTOUserFavouriteData.class, null);
                    }
                    break;

                    case EDIT_PROFILE: {
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.EDIT_PROFILE, mContext).execute(attribute, DTOLoginData.class, null);
                    }
                    break;

                    case CHANGE_PASSWORD:{
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.CHANGE_PASSWORD, mContext).execute(attribute, DTOResponse.class, null);
                    }
                    break;

                    case FORGET_PASSWORD:{
                        responseObject = new WebserviceConnector(WsConstants.SERVICE_URL + WsConstants.FORGOT_PASSWORD, mContext).execute(attribute, DTOResponse.class, null);
                    }
                    break;


                }
            } catch (Exception e) {
                Log.i(TAG, "WebserviceCaller Background Exception : " + e.toString());
                exception = e;
            }
            return responseObject;
        }

        @Override
        protected void onPostExecute(Object responseObject) {
            if (displayProgress) {
                dialog.dismiss();
            }

            webserviceResponse.onResponse(currentApiCode, responseObject, exception);
            super.onPostExecute(responseObject);
        }
    }

    public void cancelAsynch(WebserviceCaller webserviceCaller) {
        if (webserviceCaller != null && webserviceCaller.getStatus() != AsyncTask.Status.FINISHED)
            webserviceCaller.cancel(true);
    }

}
