package com.foodscan.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.Security.AESUtil;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;

public class Utility {

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final String TAG = Utility.class.getSimpleName();

    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {

            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    public static Typeface setCustomFont(Context contex, String fontType) {
        return Typeface.createFromAsset(contex.getAssets(),
                fontType);
    }

    public static Typeface getFont(Context mContext, String font) {
        return Typeface.createFromAsset(mContext.getAssets(), font);
    }

    public static boolean validateStringPresence(EditText editText) {
        if (editText.getText() == null
                || editText.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static void showLongSnackBar(View view, String message, Context context) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(Color.WHITE);
        //View snackbarView = snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getColorWrapper(context, R.color.colorPrimaryDark));
        snackbar.show();
    }

    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int getScreenWidth(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            //noinspection deprecation
            width = display.getWidth();  // Deprecated
        }
        return width;
    }

    public static boolean validateEmail(EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText()).matches();
    }

    public static boolean validateWebSite(EditText editText) {
        return Patterns.WEB_URL.matcher(editText.getText()).matches();
    }

    public static void hideKeyboard(Activity activity) {

        Context mContext = activity;
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWhiteSpaceContains(EditText editText) {
        String str = editText.getText().toString().trim();
        if (str.contains(" ") && (!str.startsWith(" ") && !str.endsWith(" "))) {
            return false;
        }
        return true;
    }

    public static String convertDateStringFormat(String strDate, String fromFormat, String toFormat) {
        if (strDate != null && strDate.length() > 0) {

            try {
                //SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
                SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.US);
                //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                //SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim());
                SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), Locale.US);

                return dateFormat2.format(sdf.parse(strDate)).toUpperCase(Locale.US);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } else {
            return "";
        }
    }

    public static Date stringToDate(String Date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(Date);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hrs24(String date) {

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm:ss");

        try {
            String time = date24Format.format(date12Format.parse(date));
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
            return null;
        }
    }

    public static boolean dateIsBetweenLimit(Date currentDate, Date startDate, Date endDate) {
        //return currentDate.after(startDate) && currentDate.before(endDate);
        return startDate.getTime() <= currentDate.getTime() && currentDate.getTime() <= endDate.getTime();
    }

    public static boolean dateIscurrentDate(Date currentDate, Date startDate) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(currentDate).equals(fmt.format(startDate));
    }

    public static String getCommentTime(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a", Locale.US);
        return fmt.format(date);

    }

    public static String getCommentMonthTime(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd", Locale.US);
        return fmt.format(date);

    }

    public static String getCommentShortMonthTime(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd", Locale.US);
        return fmt.format(date);

    }


    public static boolean isTimeWith_in_Interval(String valueToCheck, String endTime, String startTime) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(endTime);

            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(startTime);

            Date d = new SimpleDateFormat("HH:mm:ss").parse(valueToCheck);

            if (time1.after(d) && time2.before(d)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static String getCurrentWeekDay() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        String Day = "";

        switch (day) {
            case Calendar.SUNDAY:
                Day = "sun";
                break;

            case Calendar.MONDAY:
                Day = "mon";
                break;

            case Calendar.TUESDAY:
                Day = "tue";
                break;

            case Calendar.WEDNESDAY:
                Day = "wed";
                break;

            case Calendar.THURSDAY:
                Day = "thu";
                break;

            case Calendar.FRIDAY:
                Day = "fri";
                break;

            case Calendar.SATURDAY:
                Day = "sat";
                break;
        }

        return Day;
    }

    /* This is method for create image file capture by camera */
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public static String removeLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long diffrenceFromCurrentDate(String closingDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        long diffMillies = 0;
        Date curDate = new Date();
        String dateStart = format.format(curDate);
        String dateStop = closingDate;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            diffMillies = d2.getTime() - d1.getTime();

            return diffMillies;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffMillies;
    }

    public static String getRealPathFromURI(Uri contentURI, Context mContext) {
        //Uri contentUri = Uri.parse(contentURI);
        Uri contentUri = contentURI;
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String compressImage(Uri imageUri, Context mContext) {

        //String filePath = getRealPathFromURI(imageUri, mContext);
        String filePath = getRealPathFromURI(imageUri, mContext);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    @SuppressWarnings("deprecation")
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static boolean validatePasswordLength(String Password) {
        return Password.length() >= MIN_PASSWORD_LENGTH;
    }


    public static String encode(String keyString, String keyString_iv, String stringToEncode) {
//        return AES_Helper_new.encrypt(stringToEncode, keyString);
        return AESUtil.encrypt(keyString, keyString_iv, stringToEncode);
    }


//    public static String encode(String keyString, String stringToEncode) throws NullPointerException {
//        if (keyString.length() == 0 || keyString == null) {
//            throw new NullPointerException("Please give Password");
//        }
//
//        if (stringToEncode.length() == 0 || stringToEncode == null) {
//            throw new NullPointerException("Please give text");
//        }
//
//        try {
//            SecretKeySpec skeySpec = getKey(keyString);
//            byte[] clearText = stringToEncode.getBytes("UTF8");
//
//            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
//            final byte[] iv = new byte[16];
//            Arrays.fill(iv, (byte) 0x00);
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
//
//            // Cipher is not thread safe
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
//
//            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
//            Log.d("jacek", "Encrypted: " + stringToEncode + " -> " + encrypedValue);
//            return encrypedValue;
//
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {

        // You can change it to 128 if you wish
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

    public static void noInternetConnection(final AppCompatActivity mContext, String message) {
        try {
            com.rey.material.app.Dialog.Builder builder = null;
            boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

            builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                    mContext.finish();
                }
            };

            ((SimpleDialog.Builder) builder).message(message)
                    .title(mContext.getString(R.string.app_name))
                    .negativeAction(mContext.getString(R.string.Exit));

            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(mContext.getSupportFragmentManager(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;


    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

}
