package com.sdsmdg.harjot.crollerTest.utilities;

import android.util.Log;

public class Log4j {

    private static final String tag = "2026";
    private static boolean deBug = true;
    public static void w(String msg) {
        if (deBug) {
            Log.w(tag, msg);
        }
    }

    public static void i(String msg) {
        if (deBug) {
            Log.i(tag, msg);
        }
    }

    public static void e(String msg) {
        if (deBug) {
            Log.e(tag, msg);
        }
    }

}
