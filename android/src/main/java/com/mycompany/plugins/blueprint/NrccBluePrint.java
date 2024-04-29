package com.mycompany.plugins.blueprint;

import android.util.Log;

public class NrccBluePrint {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
