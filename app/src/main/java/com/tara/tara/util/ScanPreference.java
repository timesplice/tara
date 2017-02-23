package com.tara.tara.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tara.tara.model.ScanModel;

/**
 * Created by M1032467 on 2/22/2017.
 */

public class ScanPreference {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String ID = "id";
    private static final String PREF_NAME = "scan_preference";
    public static final String HOTELID = "hotelId";
    public static final String TABLEID = "tableId";


    public ScanPreference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setScanInPreference(String hotelId, String tableId) {
        editor.putString(HOTELID, hotelId);
        editor.putString(TABLEID, tableId);
        editor.commit();
    }

    public boolean isDataPresent() {
        String hotelId = pref.getString(HOTELID, null);
        if (hotelId == null)
            return false;
        return true;
    }

    public ScanModel getScanDetails() {
        ScanModel scanModel = new ScanModel();
        scanModel.setHotelId(pref.getString(HOTELID, ""));
        scanModel.setTableId(pref.getString(TABLEID, ""));

        return scanModel;
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }
}
