package com.nic.thooimaikaavalar_old.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.nic.thooimaikaavalar_old.BuildConfig;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.application.NICApplication;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.session.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Utils {

    private static final String SHARED_PREFERENCE_UTILS = "Nimble";
    private static final int SECONDS_IN_A_MINUTE = 60;
    private static final int MINUTES_IN_AN_HOUR = 60;
    private static SharedPreferences sharedPreferences;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static int CIPHER_KEY_LEN = 16; //128 bits
    private static PrefManager prefManager;

    //Datepicker
    private static String fromToDate = "";
    private static  boolean date_flag =true;
    private static String fromDate,toDate;
    public static boolean flag = false; //128 bits

    static DateInterface dateInterface  ;
    private static void initializeSharedPreference() {
        sharedPreferences = NICApplication.getGlobalContext()
                .getSharedPreferences(SHARED_PREFERENCE_UTILS,
                        Context.MODE_PRIVATE);
    }

    public static boolean isOnline() {
        boolean status = false;
        ConnectivityManager cm = (ConnectivityManager) NICApplication.getGlobalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = true;
            }
        } else {
            status = false;
        }

        return status;
    }

    public static Boolean isValidEmail(String email) {
        Boolean status = true;
        String mail[] = email.split(",");
        for (int i = 0; i < mail.length; i++) {
            if (!validateMail(mail[i])) {
                return false;
            }
        }
        return status;
    }

    public static boolean validateMail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {

        boolean flag;
        String expression = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" + ")+";

        CharSequence inputStr = email.trim();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        flag = matcher.matches();
        return flag;

    }

//    public static void showAlert(Context context, String message) {
//        try {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_dialog,null);
//            final AlertDialog alertDialog = builder.create();
//            alertDialog.setView(dialogView, 0, 0, 0, 0);
//            alertDialog.setCancelable(false);
//            alertDialog.show();
//
//            MyCustomTextView tv_message = (MyCustomTextView) dialogView.findViewById(R.id.tv_message);
//            tv_message.setText(message);
//
//            Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
//            btnOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void showAlert(Activity activity, String msg){
        try {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);

       TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String parseDate(String time) {
        String inputPattern = "MM/dd/yyyy HH:mm:ss";
        String outputPattern = "dd MMM'T'yy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("T", "'").replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }

    public static boolean checkValidTime(String d1, String d2) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM,yy h:mm aa", Locale.US);
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public static boolean checkValidTime(String time) {
        boolean isValid = false;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.format(date);
        try {
            if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(time))) {
                isValid = false;
            } else {
                isValid = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static String checkAM_PM(int mEndHour) {
        String AM_PM;
        if (mEndHour < 12) {
            AM_PM = "PM";
        } else {
            AM_PM = "PM";
        }
        return AM_PM;
    }

    public static String parseDateForCalendar(String time) {
        String inputPattern = "MM/dd/yyyy";
        String outputPattern = "dd MMM'T'yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String parseDateForChart(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM'T'yy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("T", "'").replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }

    public static String getYesterdayDateString(String currentDate) {
        String result = null;
        String inputPattern = "MM/dd/yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd H:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);
        Date date = null;
        try {
            date = inputFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() - 1000 * 60 * 60 * 24);
            result = outputFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String parseSmsDate(String time) {

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM'T'yy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("T", "'").replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }

    public static String parseDateFormatter(String time) {
        String inputPattern = "dd MMM yy hh:mm a";
        String outputPattern = "yyyy-MM-dd H:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time.replaceAll(",", " "));
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeDate(String time) {
        String inputPattern = "MM/dd/yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd H:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeDateForTimePeriod(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm";
        String outputPattern = "dd MMM'T'yy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date;
        date = new Date();
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("T", "'").replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }

    public static String displayTimePeriod(String time) {
        String inputPattern = "dd MMM,yy hh:mm a";
        String outputPattern = "dd MMM'T'yy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("T", "'").replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }

    public static String dateFormatter(String time) {
        time = time.replace(",", " ");
        time = time.replace("T", " ");

        String inputPattern = "dd MMM yy h:mm a";
        String outputPattern = "dd MMM,yy + HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String timeFormatChange(String time) {
        String inputPattern = "HH:mm";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.replace("am", "AM").replace("pm", "PM").replace("a.m.", "AM").replace("p.m.", "PM");
    }


    public static Date convertStringToDate(String dateString) {
        Date date = null;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.US);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getBatteryUnit(int value) {
        String batteryValue = null;
        if (value >= 0 && value <= 100) {
            batteryValue = "" + value + " " + "%";
        } else if (value == -1) {
            batteryValue = "Unknown";
        } else if (value == -2) {
            batteryValue = "Low";
        } else if (value == -3) {
            batteryValue = "Charging";
        } else if (value == -4) {
            batteryValue = "Full";
        }
        return batteryValue;
    }

    public static String getAlertValue(int value) {
        if (value == 1) {
            return "Normal";
        } else if (value == 2) {
            return "Attention";
        } else if (value == 3) {
            return "Critical";
        } else {
            return "Fault";
        }
    }

    public static boolean checkCompanyName(String compName) {
        return "srei".equalsIgnoreCase(compName);
    }


//    public static int getIndexFromList(List<CommonModel> listValues, String value) {
//        // TODO Auto-generated method stub
//        int index = 0;
//        for (int i = 0; i < listValues.size(); i++) {
//            if (listValues.get(i).getName().equalsIgnoreCase(value)) {
//                index = i;
//                break;
//            }
//        }
//        return index;
//    }

    public static int getIndexFromListForAlertRange(List<String> listValues, String value) {
        // TODO Auto-generated method stub
        int index = 0;
        for (int i = 0; i < listValues.size(); i++) {
            if (listValues.get(i).equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }


    public static double getScreenInch(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

    public static String gpsModeForMessage(String gpsValue) {
        String value = "";
        if ("Indoor".equalsIgnoreCase(gpsValue)) {
            value = "msbased";
        } else if ("Outdoor".equalsIgnoreCase(gpsValue)) {
            value = "standalone";
        } else if ("Deep Indoor".equalsIgnoreCase(gpsValue)) {
            value = "assisted";
        }
        return value;
    }

    public static String powerSaveMode(String powerSaveValue) {
        String value = "";
        if ("On".equalsIgnoreCase(powerSaveValue)) {
            value = "alwayson";
        } else if ("Low".equalsIgnoreCase(powerSaveValue)) {
            value = "standby";
        } else if ("Very Low".equalsIgnoreCase(powerSaveValue)) {
            value = "hibernate";
        }
        return value;
    }

    public static String panicMode(String powerSaveValue) {
        String value = "";
        if ("On".equalsIgnoreCase(powerSaveValue)) {
            value = "on";
        } else if ("Off".equalsIgnoreCase(powerSaveValue)) {
            value = "off";
        }
        return value;
    }

    public static String checkSeverity(String severityValue) {
        String severityType = "";
        if ("1".equals(severityValue)) {
            severityType = "Normal";
        } else if ("2".equals(severityValue)) {
            severityType = "Attention";
        } else if ("3".equals(severityValue)) {
            severityType = "Critical";
        } else if ("4".equals(severityValue)) {
            severityType = "Fault";
        }
        return severityType;
    }

    public static boolean isValidMobile(String phone2) {
        boolean check;
        check = phone2.length() == 10;
        return check;
    }

    public static String emailOrNumberValues(List<String> values) {
        String listValues = "";
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (String number : values) {
            if (values.size() > 1) {
                builder.append(prefix);
                prefix = ",";
                builder.append(number);
            } else {
                builder.append(number);
            }
        }
        listValues = builder.toString();
        return listValues;
    }

    public static String displayListAsString(List<String> values) {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (String number : values) {
            if (values.size() > 1) {
                builder.append(prefix);
                prefix = "<br><br>";
                builder.append(number);
            } else {
                builder.append(number);
            }
        }
        return builder.toString();
    }


    public static int minutesToSeconds(int minutes) {
        return minutes * SECONDS_IN_A_MINUTE;
    }

    public static int secondsToMinutes(int seconds) {
        return seconds / SECONDS_IN_A_MINUTE;
    }


    public static String enableSwitchValues(SwitchCompat enableSwitch) {
        String switchValue = "";
        if (enableSwitch.isChecked()) {
            switchValue = "on";
        } else {
            switchValue = "";
        }
        return switchValue;
    }

//    public static String getMessageQuestion(List<MessageModel> listValues, String value) {
//        // TODO Auto-generated method stub
//        String questionName = "";
//        try {
//            for (int i = 0; i < listValues.size(); i++) {
//                if (listValues.get(i).getMessageFormat().split("=")[0].equalsIgnoreCase(value.split("=")[0])) {
//                    questionName = listValues.get(i).getQuestion();
//                    break;
//                }
//            }
//        } catch (ArrayIndexOutOfBoundsException a) {
//            a.printStackTrace();
//        }
//
//        return questionName;
//    }

    public static String name = "";

    public static Boolean getMessageQuestionForSelectBox(String value) {
        // TODO Auto-generated method stub
        Boolean questionName = false;
        try {
            if ("gpsmode".equalsIgnoreCase(value.split("=")[0])) {
                name = "1";
                questionName = true;
            } else if ("lpm".equalsIgnoreCase(value.split("=")[0])) {
                name = "2";
                questionName = true;
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            a.printStackTrace();
        }
        return questionName;
    }

    public static String getAnswerValue(String value) {
        String questionName = "";
        try {
            if (name.equalsIgnoreCase("1")) {
                if ("assisted".equalsIgnoreCase(value)) {
                    questionName = "Deep Indoor";
                } else if ("msbased".equalsIgnoreCase(value)) {
                    questionName = "Indoor";
                } else if ("standalone".equalsIgnoreCase(value)) {
                    questionName = "Out Door";
                }
            } else if (name.equalsIgnoreCase("2")) {
                if ("alwayson".equalsIgnoreCase(value)) {
                    questionName = "On";
                } else if ("standby".equalsIgnoreCase(value)) {
                    questionName = "Low";
                } else if ("hibernate".equalsIgnoreCase(value)) {
                    questionName = "Very Low";
                }
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            a.printStackTrace();
        }
        return questionName;
    }


    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static SpannableStringBuilder checkMandatoryField(String firstPart) {
        String simple = firstPart;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    public static String updateTemperature(String temperature) {
        String temperatureValue = "";
        if ("-500000.0".equalsIgnoreCase(temperature)) {
            temperatureValue = "Error";
        } else if ("-100000.0".equalsIgnoreCase(temperature) || "-100000".equalsIgnoreCase(temperature)) {
            temperatureValue = "Error";
        } else if ("100000".equalsIgnoreCase(temperature) || "500000".equalsIgnoreCase(temperature)) {
            temperatureValue = "Error";
        } else {
            temperatureValue = temperature;
        }
        return temperatureValue;
    }

//    public static int updateBatteryImage(int value) {
//        if (value >= 0 && value <= 10) {
//            return R.drawable.ten_per_battery;
//        } else if (value > 10 && value <= 20) {
//            return R.drawable.twenty_per_battery;
//        } else if (value > 20 && value <= 25) {
//            return R.drawable.twentyfive_per_battery;
//        } else if (value > 25 && value <= 50) {
//            return R.drawable.fifty_per_battery;
//        } else if (value > 50 && value <= 75) {
//            return R.drawable.seventy_five_per_battery;
//        } else if (value > 75 && value <= 100) {
//            return R.drawable.full_battery;
//        } else if (value == -1) {
//            return R.drawable.unknown_battery;
//        } else if (value == -2) {
//            return R.drawable.dead_battery;
//        } else if (value == -3) {
//            return R.drawable.charging_battery;
//        } else {
//            return R.drawable.full_battery;
//        }
//    }

    public static String convertCelciusToFahrenheit(String temperature) {
        DecimalFormat df = new DecimalFormat("###.##");
        double fahrenheit = 9 * (Double.parseDouble(temperature) / 5) + 32;
        return df.format(fahrenheit);
    }

    public static int convertToMinutesToHR(String minutes) {
        return Integer.parseInt(minutes) / 60;
    }


    public static String convertHoursToDays(int hrs) {
        int noOfdays;
        noOfdays = hrs / 24;
        return String.valueOf(noOfdays);
    }


    public static int convertHrsToMinutes(String hrs, String type) {
        int minutes;
        if ("Minutes".equalsIgnoreCase(type))
            return minutes = Integer.parseInt(hrs);
        else if ("Hour".equalsIgnoreCase(type))
            return minutes = Integer.parseInt(hrs) * 60;
        else
            return minutes = Integer.parseInt(hrs) * 24 * 60;
    }

    //Version name
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }


    public static boolean contains(List<String> list, String name) {
        for (String item : list) {
            if (item.equals(name)) {
                return true;
            }
        }
        return false;
    }


    public static Date getDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


    public static String getTransportType(String type) {
        String value = "";
        if ("1".equalsIgnoreCase(type)) {
            value = "Data Call";
        } else {
            value = "Text Message";
        }
        return value;
    }


    public static String getTwoinutesBackFromDate(String time) {
        String inputPattern = "MM/dd/yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd H:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, -2);
            Date twoMinutesBack = cal.getTime();
            str = outputFormat.format(twoMinutesBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupUI(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(activity);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }

    public static long convertDateToTimeStamp(String dateValue) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    private static Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IrisReport";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File oldFile = new File(new File(path), fileName);
        if (oldFile.exists())
            oldFile.delete();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void captureMapScreen(final Activity activity, final View mView, ImageView imageView) {
        Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mView.draw(canvas);
        imageView.setImageDrawable(null);
        shareScreenShot(activity, bitmap);

    }

    public static void shareScreenShot(Activity activity, View view) {
        Bitmap bm = screenShot(view);
        shareScreenShot(activity, bm);
    }

    public static void shareScreenShot(Activity activity, Bitmap bm) {
        File file = saveBitmap(bm, "IrisReport.png");   /*change Uri.fromfile to Fileprovider.getUriForFile becoz the old method is not Supported for Android 8 so permission must be added in manifest file*/
//        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Uri uri = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(Intent.createChooser(shareIntent, "share via"));
    }


    public static String randomChar() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        Log.d("rand", sb.toString());
        return sb.toString();
    }

    public static String getSHA(String UserPassWord) {
        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(UserPassWord.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String encrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String encryptedDataInBase64 = android.util.Base64.encodeToString(encryptedData, 0);
            String ivInBase64 = android.util.Base64.encodeToString(iv.getBytes("UTF-8"), 0);

            return encryptedDataInBase64 + ":" + ivInBase64;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

    private static String fixKey(String key) {

        if (key.length() < CIPHER_KEY_LEN) {
            int numPad = CIPHER_KEY_LEN - key.length();

            for (int i = 0; i < numPad; i++) {
                key += "0"; //0 pad to len 16 bytes
            }

            return key;

        }

        if (key.length() > CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        return key;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key  - key to use should be 16 bytes long (128 bits)
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */


    public static String decrypt(String key, String data) {

        try {
            String[] parts = data.split(":");

            IvParameterSpec iv = new IvParameterSpec(android.util.Base64.decode(parts[1], 1));
            // System.out.println(fixKey(iv));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedEncryptedData = android.util.Base64.decode(parts[0], 1);

            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public static JSONObject motivatorScheduleListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_MOTIVATOR_SCHEDULE);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public static String formatDateReverse (String date){
        String initDateFormat = "yyyy-MM-dd";
        String endDateFormat = "dd-MM-yyyy";

        String parsedDate = null;

        try {
            Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
            parsedDate = formatter.format(initDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return parsedDate;
    }

    public static void getCurrentTimeUsingDate() {

        Date date = new Date();

        String strDateFormat = "hh:mm:ss a";

        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        String formattedDate= dateFormat.format(date);

        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);

    }


    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }

    ///////////////// ***************** Now Not Need *********************
    public static JSONObject districtListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_DISTRICT_LIST_ALL);
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    public static JSONObject blockListDistrictWiseJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_BLOCK_LIST_DISTRICT_WISE);
        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        Log.d("blockListDistrictWise", "" + dataSet);
        return dataSet;
    }
    public static JSONObject schemeFinyearListJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_SCHEME_FINYEAR_LIST_LAST_NYEARS);
        dataSet.put(AppConstant.N_YEAR, 6);
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    public static JSONObject stageListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.STAGE_LIST);
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    public static JSONObject additionalstageListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.ADDITIONAL_STAGE_LIST);
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    public static JSONObject schemeListBlockWiseJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_SCHEME_LIST_DISTRICT_FINYEAR_WISE);
        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCodeJson());
        Log.d("objectschemeLis", "" + dataSet);
        return dataSet;
    }
    public static JSONObject workListBlockWiseJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.WORK_LIST_BASED_ON_FINYEAR_VILLAGE);
        dataSet.put(AppConstant.FINANCIAL_YEAR, prefManager.getFinancialyearName());
        dataSet.put(AppConstant.SCHEME_ID, prefManager.getKeySpinnerSelectedSchemeSeqId());
        dataSet.put(AppConstant.PV_CODE, prefManager.getPvCode());
        Log.d("objectworkLis", "" + dataSet);
        return dataSet;
    }

    //////////////// *********************** //////////////////

    public static JSONObject villageListDistrictBlockWiseJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_VILLAGE_LIST_DISTRICT_BLOCK_WISE);
        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("villageListDistBlock", "" + dataSet);
        return dataSet;
    }
    public static JSONObject HabitationListDistrictBlockVillageWiseJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_HABITATION_LIST_DISTRICT_BLOCK_WISE);
        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("HabListDistBlock", "" + dataSet);
        return dataSet;
    }

    public static JSONObject swm_capacity_of_mcc_in_tonesParamsJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_swm_capacity_of_mcc_in_tones);
        //dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        //dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("swm_capacity_of_mcc", "" + dataSet);
        return dataSet;
    }
    public static JSONObject swm_no_of_thooimai_kaavalarsParamsJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_swm_no_of_thooimai_kaavalars);
        //dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        //dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("swm_no_of_thooimai", "" + dataSet);
        return dataSet;
    }
    public static JSONObject swm_photographs_of_mcc_componentsJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_swm_photographs_of_mcc_components);
        //dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        //dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("swm_photographs", "" + dataSet);
        return dataSet;
    }
    public static JSONObject swm_water_supply_availabilityParamsJsonParams(Activity activity) throws JSONException {
        prefManager = new PrefManager(activity);
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_swm_water_supply_availability);
        //dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        //dataSet.put(AppConstant.BLOCK_CODE, prefManager.getBlockCode());
        Log.d("swm_water_supply", "" + dataSet);
        return dataSet;
    }



//    public static void deleteCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            if (dir != null && dir.isDirectory()) {
//                deleteDir(dir);
//            }
//        } catch (Exception e) { e.printStackTrace();}
//    }
//
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//            return dir.delete();
//        } else if(dir!= null && dir.isFile()) {
//            return dir.delete();
//        } else {
//            return false;
//        }
//    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static boolean isValidMobilenew(String phone2) {
        boolean check= false;
        boolean startDigitCheck = false;
        boolean sameDigitCheck= false;
        String[] startDigit=new String[] {"0","1","2","3","4","5"};
        String[] sameDigit=new String[] {"6666666666","7777777777","8888888888","9999999999"};
        for(int i=0;i<startDigit.length;i++){
            if(phone2.startsWith(startDigit[i])){
                startDigitCheck=false;
                return false;
            }else {
                startDigitCheck=true;
            }
        }

        if(startDigitCheck){
            for(int i=0;i<sameDigit.length;i++){
                if(phone2.equals(sameDigit[i])){
                    sameDigitCheck=false;
                    return false;
                }else {
                    sameDigitCheck=true;
                }
            }

        }else {
            return  false;
        }
        if(sameDigitCheck){
            check = phone2.length() == 10;
        }
        else {
            return  false;
        }
        if(check){
            return check;
        }else {
            return  false;
        }

    }

    //Date Picker
    public static void showDatePickerDialog(final Context context) {
        fromToDate=" ";
        dateInterface= (DateInterface) context;
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.date_picker_layout, null);
        final android.app.AlertDialog alertDialog = builder.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(true);
        alertDialog.show();

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        final TextView from = (TextView) dialogView.findViewById(R.id.from);
        final TextView to = (TextView) dialogView.findViewById(R.id.to);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        TextView ok = (TextView) dialogView.findViewById(R.id.ok);
        final TextView fromDateValue = (TextView) dialogView.findViewById(R.id.fromDateValue);
        final TextView toDateValue = (TextView) dialogView.findViewById(R.id.toDateValue);
        final LinearLayout t_layout = (LinearLayout) dialogView.findViewById(R.id.t_layout);
        final LinearLayout f_layout = (LinearLayout) dialogView.findViewById(R.id.f_layout);
        RelativeLayout button_layout = (RelativeLayout) dialogView.findViewById(R.id.button_layout);

        /*Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Medium.ttf");
        from.setTypeface(custom_font);
        to.setTypeface(custom_font);
        fromDateValue.setTypeface(custom_font);
        toDateValue.setTypeface(custom_font);
        cancel.setTypeface(custom_font);
        ok.setTypeface(custom_font);*/

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        date_flag=true;
        f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        fromDateValue.setTextColor(context.getResources().getColor(R.color.white));
        from.setTextColor(context.getResources().getColor(R.color.white));
        toDateValue.setTextColor(context.getResources().getColor(R.color.unselect));
        to.setTextColor(context.getResources().getColor(R.color.unselect));
        t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(0);

        fromDate= format.format(c.getTime());
        fromDateValue.setText(fromDate);
        toDate= format.format(c.getTime());
        toDateValue.setText(toDate);
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(0);
// set current date into datepicker
        datePicker.init(year, month, day, null);
        f_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_flag=true;
                f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                fromDateValue.setTextColor(context.getResources().getColor(R.color.white));
                from.setTextColor(context.getResources().getColor(R.color.white));
                toDateValue.setTextColor(context.getResources().getColor(R.color.unselect));
                to.setTextColor(context.getResources().getColor(R.color.unselect));
                t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                datePicker.setMaxDate(new Date().getTime());
                datePicker.setMinDate(0);
            }
        });
        t_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_flag=false;
                f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                fromDateValue.setTextColor(context.getResources().getColor(R.color.unselect));
                from.setTextColor(context.getResources().getColor(R.color.unselect));
                toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                to.setTextColor(context.getResources().getColor(R.color.white));
                t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                Date date = null;
                try {
                    date = format.parse(fromDate);
                    datePicker.setMinDate(date.getTime());
                    datePicker.setMaxDate(new Date().getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                if(date_flag){

                    fromDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    fromDateValue.setText(updateLabel(fromDate));

                    try {
                        Date strDate = format.parse(fromDate);
                        Date endDate = format.parse(toDate);
                        if (!endDate.after(strDate)) {
                            toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                            toDateValue.setText(updateLabel(toDate));
                        }else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    date_flag=false;
                    f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    fromDateValue.setTextColor(context.getResources().getColor(R.color.unselect));
                    from.setTextColor(context.getResources().getColor(R.color.unselect));
                    toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                    to.setTextColor(context.getResources().getColor(R.color.white));
                    t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    Date date = null;
                    try {
                        date = format.parse(fromDate);
                        datePicker.setMinDate(date.getTime());
                        datePicker.setMaxDate(new Date().getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {



                    toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    toDateValue.setText(updateLabel(toDate));

                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    fromDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    fromDateValue.setText(updateLabel(fromDate));

                    try {
                        Date strDate = format.parse(fromDate);
                        Date endDate = format.parse(toDate);
                        if (!endDate.after(strDate)) {
                            toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                            toDateValue.setText(updateLabel(toDate));
                        }else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    date_flag=false;
                    f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    fromDateValue.setTextColor(context.getResources().getColor(R.color.unselect));
                    from.setTextColor(context.getResources().getColor(R.color.unselect));
                    toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                    to.setTextColor(context.getResources().getColor(R.color.white));
                    t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    Date date = null;
                    try {
                        date = format.parse(fromDate);
                        datePicker.setMinDate(date.getTime());
                        datePicker.setMaxDate(new Date().getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {
                    fromToDate=fromDateValue.getText().toString()+":"+toDateValue.getText().toString();
                    dateInterface.getDate(fromToDate);
                    alertDialog.dismiss();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromToDate=" ";
                alertDialog.dismiss();
            }
        });

    }
    private static String updateLabel(String dateStr) {
        String myFormat="";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(dateStr);
            System.out.println(date1);
            String dateTime = format.format(date1);
            System.out.println("Current Date Time : " + dateTime);
            myFormat = dateTime; //In which you need put here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFormat;
    }
}
