package com.sun.motherbirthday;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class Userset{

    private final static String PREFERENCES_NAME = "preferences";
    private final static String key_personal_isnofirst = "key_personal_isnofirst";
    private final static String key_personal_isClickNumber = "key_personal_isClickNumber";
    public static boolean ISNOTFIRST = false;
    public static int ISCLICKkNUMBER = -1;

    public static void loadPersonalInfo(Activity activity){
        Log.i( "DEBUG", "==UserSet: loadPersonalInfo==");

        SharedPreferences pref = activity.getSharedPreferences( PREFERENCES_NAME, MODE_PRIVATE);//MODE_PRIVATE: 程序內存取
        ISNOTFIRST = pref.getBoolean (key_personal_isnofirst ,ISNOTFIRST );
        ISCLICKkNUMBER = pref.getInt (key_personal_isClickNumber ,ISCLICKkNUMBER );

    }

    public static void  savePersonalInfo(Activity activity){
        Log.i( "DEBUG", "==UserSet: savePersonalInfo==");

        SharedPreferences pref = activity.getSharedPreferences( PREFERENCES_NAME, MODE_PRIVATE);//MODE_PRIVATE: 程序內存取

        pref.edit()
                .putBoolean ( key_personal_isnofirst, ISNOTFIRST)
                .putInt ( key_personal_isClickNumber, ISCLICKkNUMBER)
                .apply();
    }
}
