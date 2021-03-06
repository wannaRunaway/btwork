package com.kulun.energynet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.hjq.toast.ToastUtils;
import com.kulun.energynet.R;
import com.kulun.energynet.login.PasswordLoginActivity;
import com.kulun.energynet.main.MainActivity;
import com.kulun.energynet.model.City;
import com.kulun.energynet.model.StationInfo;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import me.leefeng.promptlibrary.PromptDialog;
import me.nereo.multi_image_selector.bean.Image;
import okhttp3.OkHttpClient;

import static android.text.TextUtils.substring;

/**
 * created by xuedi on 2019/7/31
 */
public class Utils {
    public static final String TAG = "xuedi";

    public static void snackbar(Activity activity, String message) {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Display display = activity.getWindowManager().getDefaultDisplay();
                    int height = display.getHeight();
                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    ToastUtils.show(message);
                }
            });
        } catch (Exception e) {
            Utils.log(null, "", "toast????????????");
        }
    }

    public static void log(String url, String params, String json) {
        Log.d(TAG, "url:" + url + "params:" + params + "json:" + json);

    }

    public static boolean isPhone(String phone, Activity activity) {
//        return false;
//        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            snackbar(activity, "???????????????11??????");
            return false;
        }
        return true;
//        } else {
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(phone);
//            boolean isMatch = m.matches();
//            if (!isMatch) {
//                snackbar(activity, "???????????????????????????");
//            }
//            return isMatch;
//        }
    }

    public static boolean teshu(String string) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}????????????????????????????????????]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.find();
    }

    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        if (days > 0) {
            DateTimes = days + "???" + hours + "??????" + minutes + "??????" + seconds + "???";
        } else if (hours > 0) {
            DateTimes = hours + "??????" + minutes + "??????" + seconds + "???";
        } else if (minutes > 0) {
            DateTimes = minutes + "??????" + seconds + "???";
        } else {
            DateTimes = seconds + "???";
        }
        return DateTimes;
    }

    /**
     * ?????????????????????????????????
     *
     * @param IDStr ????????????
     * @return true ?????????false ??????
     * @throws ParseException
     */
    public static boolean IDCardValidate(String IDStr) {
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ ???????????????18??? ================
        if (IDStr.length() != 18) {
            return false;
        }
        // ================ ?????? ??????????????????????????? ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            //errorInfo = "?????????15???????????????????????? ; 18????????????????????????????????????????????????";
            return false;
        }
        // ================ ???????????????????????? ================
        String strYear = Ai.substring(6, 10);// ??????
        String strMonth = Ai.substring(10, 12);// ??????
        String strDay = Ai.substring(12, 14);// ???
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
//          errorInfo = "????????????????????????";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                //errorInfo = "????????????????????????????????????";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //errorInfo = "?????????????????????";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //errorInfo = "?????????????????????";
            return false;
        }
        // ================ ????????????????????? ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            //errorInfo = "??????????????????????????????";
            return false;
        }
        // ================ ???????????????????????? ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //errorInfo = "????????????????????????????????????????????????";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    /**
     * ???????????????????????????
     *
     * @return Hashtable ??????
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "??????");
        hashtable.put("12", "??????");
        hashtable.put("13", "??????");
        hashtable.put("14", "??????");
        hashtable.put("15", "?????????");
        hashtable.put("21", "??????");
        hashtable.put("22", "??????");
        hashtable.put("23", "?????????");
        hashtable.put("31", "??????");
        hashtable.put("32", "??????");
        hashtable.put("33", "??????");
        hashtable.put("34", "??????");
        hashtable.put("35", "??????");
        hashtable.put("36", "??????");
        hashtable.put("37", "??????");
        hashtable.put("41", "??????");
        hashtable.put("42", "??????");
        hashtable.put("43", "??????");
        hashtable.put("44", "??????");
        hashtable.put("45", "??????");
        hashtable.put("46", "??????");
        hashtable.put("50", "??????");
        hashtable.put("51", "??????");
        hashtable.put("52", "??????");
        hashtable.put("53", "??????");
        hashtable.put("54", "??????");
        hashtable.put("61", "??????");
        hashtable.put("62", "??????");
        hashtable.put("63", "??????");
        hashtable.put("64", "??????");
        hashtable.put("65", "??????");
        hashtable.put("71", "??????");
        hashtable.put("81", "??????");
        hashtable.put("82", "??????");
        hashtable.put("91", "??????");
        return hashtable;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void toLogin(Activity activity) {
        Intent intent = new Intent(activity, PasswordLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * ???????????????????????????debug??????
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getToken(Activity activity) {
        String token = null;
        if (UserLogin.getInstance().getToken() == null || UserLogin.getInstance().getToken().equals("")) {
            token = (String) SharePref.get(activity, API.token, "");
        } else {
            token = UserLogin.getInstance().getToken();
        }
        return token;
    }

    public static String getUseName(Activity activity) {
        String username = null;
        if (UserLogin.getInstance().getName() == null || UserLogin.getInstance().getName().equals("")) {
            username = (String) SharePref.get(activity, API.username, "");
        } else {
            username = UserLogin.getInstance().getName();
        }
        return username;
    }

    public static double getBalance(Activity activity) {
        double balance = 0;
        if (UserLogin.getInstance().getBalance() == 0) {
            balance = (double) SharePref.get(activity, API.balance, "0");
        } else {
            balance = UserLogin.getInstance().getBalance();
        }
        return balance;
    }

    public static String getPhone(Activity activity) {
        String phone = null;
        if (UserLogin.getInstance().getPhone() == null || UserLogin.getInstance().getPhone().equals("")) {
            phone = (String) SharePref.get(activity, API.phone, "");
        } else {
            phone = UserLogin.getInstance().getPhone();
        }
        return phone;
    }

    public static String getAccount(Activity activity) {
        String account = null;
        if (UserLogin.getInstance().getAccount() == null || UserLogin.getInstance().getAccount().equals("")) {
            account = (String) SharePref.get(activity, API.account, "");
        } else {
            account = UserLogin.getInstance().getAccount();
        }
        return account;
    }

    public static String getAliyunEndpoint(Activity activity) {
        return "http://" + (String) SharePref.get(activity, API.aliyunEndpoint, "");
    }

    public static String getBucketName(Activity activity) {
        return (String) SharePref.get(activity, API.bucketName, "");
    }

    public static String getStsserver(Activity activity) {
        return "http://" + (String) SharePref.get(activity, API.aliyunStsServer, "");
    }

    public static void userParse(JsonObject jsonObject, Activity activity) {
        UserLogin.getInstance().setIslogin(true);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void shareprefload(JsonObject jsonObject, Activity activity) {
        UserLogin userLogin = GsonUtils.getInstance().fromJson(jsonObject, UserLogin.class);
        if (userLogin != null) {
            SharePref.put(activity, API.balance, userLogin.getBalance()+"");
            SharePref.put(activity, API.phone, userLogin.getPhone());
            SharePref.put(activity, API.username, userLogin.getName());
//        SharePref.put(activity, API.token, userLogin.getToken());
            SharePref.put(activity, API.account, userLogin.getAccount());
            UserLogin.getInstance().setAccount(userLogin.getAccount());
            SharePref.put(activity, API.aliyunEndpoint, userLogin.getAli_oss().getOssEndPoint());
            SharePref.put(activity, API.aliyunStsServer, userLogin.getAli_oss().getStsEndPoint());
            SharePref.put(activity, API.bucketName, userLogin.getAli_oss().getBucketName());
        }
    }

    public static UseBind getusebind(boolean isjson, JsonObject json, Activity activity) {
        if (isjson) {
            if (json.has("use_bind")) {
                JsonObject usebind = json.get("use_bind").getAsJsonObject();
                SharePref.put(activity, API.usebind, usebind.toString());
                return GsonUtils.getInstance().fromJson(usebind, UseBind.class);
            }
        } else {
            String usebind = (String) SharePref.get(activity, API.usebind, "");
            if (usebind != null && !usebind.equals("")) {
                return GsonUtils.getInstance().fromJson(usebind, UseBind.class);
            }
        }
        return null;
    }

    public static void refreandproUIdismiss(Activity activity, PromptDialog promptDialog, SmartRefreshLayout smartRefreshLayout) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }
        });
    }

    public static StationInfo getstation(List<StationInfo> list, int stationId) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (stationId == list.get(i).getId()) {
                position = i;
            }
        }
        if (position == -1){
            return null;
        }
        return list.get(position);
    }

    public static int getCityPosition(List<City> list, String city) {
        if (list.size() == 0){
            return -1;
        }
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (city.equals(list.get(i).getName())) {
                position = i;
            }
        }
        return position;
    }

    public static boolean usebindisNotexist(UseBind useBind) {
        if (useBind == null) {
            return true;
        }
        if (useBind.getId() == 0) {
            return true;
        }
        return false;
    }

    public static String getDate(String string) {
        String fromjson = string.replaceAll("???", "-").replaceAll("???", "");
        String start[] = fromjson.split("-");
        if (start[1].length() == 1) {
            start[1] = "0" + start[1];
            return start[0] + "-" + start[1];
        }
        return fromjson;
    }

    public static void loadkefu(Activity activity, View imageView) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + UserLogin.getInstance().getCustphone(activity));
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param inputString
     *            ???????????????
     * @param length
     *            ????????????
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param inputString
     *            ???????????????
     * @param length
     *            ????????????
     * @param size
     *            ??????????????????
     * @return
     */
    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param str
     *            ???????????????
     * @param f
     *            ????????????
     * @param t
     *            ????????????
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    // ??????????????????????????????1000ms
    private static final int FAST_CLICK_DELAY_TIME = 5000;
    private static long lastClickTime;
    public static boolean isFastClick() {
        boolean flag = true;
        if ((System.currentTimeMillis() - lastClickTime) >= FAST_CLICK_DELAY_TIME ) {
            flag = false;
        }
        lastClickTime = System.currentTimeMillis();
        return flag;
    }
}
