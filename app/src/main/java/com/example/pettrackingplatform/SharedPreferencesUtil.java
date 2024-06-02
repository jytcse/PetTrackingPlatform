package com.example.pettrackingplatform;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static final String PREFS_NAME = "UserPrefs";

    public static void saveUserId(Context context, int userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }
    public static void saveUserName(Context context, String userName) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1); // 如果没有找到用户ID则返回-1
    }
    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString("UserName", null); // 如果没有找到用户ID则返回-1
    }




}
