package com.foodscan.WsHelper.helper;

/**
 * Created by c166 on 23/10/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.foodscan.Utility.Utility;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * WebserviceConnector to make HTTP { GET,POST } calls.
 */
public class WebserviceConnector {

    private static final String LOG_TAG = "WebserviceConnector";
    private static final Lock lock = new ReentrantLock();
    private static ObjectMapper mapper = null;
    private String TAG = WebserviceConnector.class.getSimpleName();
    private String url;
    private Context mContext;

    public WebserviceConnector(String url, Context mContext) {
        this.url = url;
        this.mContext = mContext;
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @SuppressLint("NewApi")
    public <Request, Response> Response execute(Request request,
                                                Class<Response> responseType, ContentValues nameValuePairs) throws Exception {
        Response ret = null;
        int statusCode = 0;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            ObjectWriter writer = getMapper().writer();
            String jsonObject = "";

            if (request != null) {
                connection.setRequestProperty("User_Agent", "android");
                connection.setRequestProperty("Content_Type", "application/x-www-form-urlencoded");

                jsonObject = writer.writeValueAsString(request);
                Log.d(LOG_TAG, "REQUEST JSON OBJECT : " + jsonObject + "");

                try (OutputStream output = connection.getOutputStream()) {
                    output.write(jsonObject.getBytes());
                } catch (Exception error) {
                    Log.i(LOG_TAG, "JSON OBJECT  Write Exception: " + error.getLocalizedMessage() + "");
                }

                try {
                    InputStream response = connection.getInputStream();

                    statusCode = ((HttpURLConnection) connection).getResponseCode();
                    String json;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response.close();
                    json = sb.toString();

                    Log.e(LOG_TAG, "The Response is:::" + json);
                    ret = getMapper().readValue(json, responseType);

                } catch (FileNotFoundException fe) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            Utility.noInternetconnection(
                                    (AppCompatActivity) mContext,
                                    "Opps..Connection Error, Please Check your Internet connection..!!"
                            );
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error converting result " + e.getLocalizedMessage());
                }
            } else if (nameValuePairs != null) {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                bufferedWriter.write(getPostDataString(nameValuePairs));
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                statusCode = conn.getResponseCode();
                String json;
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    json = sb.toString();

                    // if((url.equals(WsConstants.LOGIN_URL)) || url.equals(WsConstants.REGISTRATION_URL))
//                    if(url.equals(WsConstants.LOGIN_URL))
//                    {
//                        Realm realm;
//                        realm = Realm.getInstance(mcoContext);
//                        realm.beginTransaction();
//                        JSONObject jobj = new JSONObject(json);
//                        realm.createOrUpdateObjectFromJson(UserInfo.class, jobj);
//                        realm.commitTransaction();
//                    }

                    Log.e(LOG_TAG, "The Response is:::" + json);
                    ret = getMapper().readValue(json, responseType);
                } else {
                    json = "";
                    Log.e(LOG_TAG, "Error converting result");
                    return null;
                }
            }
        } catch (@SuppressWarnings("deprecation") ConnectTimeoutException e) {

            Toast.makeText(mContext, "Opps..Connection timeout, Please try again later..!!", Toast.LENGTH_SHORT).show();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Socket Timeout exception");
        } catch (Exception ex) {
            if (ex != null)
                Log.e(LOG_TAG, "Status code: " + Integer.toString(statusCode)
                        + " Exception thrown: " + ex.getMessage());
            ret = null;
        }
        return ret;
    }

    protected synchronized ObjectMapper getMapper() {
        if (mapper != null) {
            return mapper;
        }
        try {
            lock.lock();
            if (mapper == null) {
                mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
                        false);
            }
            lock.unlock();
        } catch (Exception ex) {
            if (ex != null)
                Log.e(LOG_TAG, "Mapper Initialization Failed. Exception :: "
                        + ex.getMessage());
        }

        return mapper;
    }

    /**
     * this method is for upload media realted to multipart utility for media upload.
     *
     * @param responseType
     * @param mediaUploadAttribute
     * @param <Response>
     * @return
     */
    public <Response> Response uploadMedia(Class<Response> responseType, Attribute mediaUploadAttribute) {
        String charset = "UTF-8";
        String requestURL = url;
        String responseString = null;
        Response ret = null;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addHeaderField("User-Agent", "android");

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");




            /*This is to add parameter values */
//            for (int i = 0; i < mediaUploadAttribute.getArrListParam().size(); i++) {
//                multipart.addFormField(mediaUploadAttribute.getArrListParam().get(i).getParamName(),
//                        mediaUploadAttribute.getArrListParam().get(i).getParamValue());
//            }

            /*This is to add file content*/
//            for (int i = 0; i < mediaUploadAttribute.getArrListFile().size(); i++) {
//                multipart.addFilePart(mediaUploadAttribute.getArrListFile().get(i).getParamName(),
//                        new File(mediaUploadAttribute.getArrListFile().get(i).getFileName()));
//            }


            List<String> response = multipart.finish();
            Log.e(TAG, "SERVER REPLIED:");
            for (String line : response) {
                Log.e(TAG, "Upload Files Response:::" + line);
                responseString = line;
            }

            ret = getMapper().readValue(responseString, responseType);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return ret;

    }

    private String getPostDataString(ContentValues values) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> pair : values.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    public <Response> Response uploadFiles(Class<Response> responseType, FileUploadInput uploadInput) {
        Response ret = null;
        String charset = "UTF-8";
        String responseString = null;

        try {
            MultipartUtility multipart = new MultipartUtility(url, charset);
//            multipart.addHeaderField("User-Agent", "android");
             /*This is to add parameter values */
            for (int i = 0; i < uploadInput.paramList.size(); i++) {
                multipart.addFormField(uploadInput.paramList.get(i).key, uploadInput.paramList.get(i).value);
            }

            /*This is to add file content*/
            for (int i = 0; i < uploadInput.fileList.size(); i++) {
                multipart.addFilePart(uploadInput.fileList.get(i).key, new File(uploadInput.fileList.get(i).value));
            }

            List<String> response = multipart.finish();
            for (String line : response) {
                responseString = line;
                Log.e(TAG, "Response :" + responseString);
            }

            ret = getMapper().readValue(responseString, responseType);

            Log.e("WebserviceConnector", "uploadFiles: " + responseString);
            return ret;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;

    }


}
