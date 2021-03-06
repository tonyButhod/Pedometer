package buthod.tony.appManager.database;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import buthod.tony.appManager.R;

/**
 * Created by Tony on 29/07/2017.
 */

public class DAOBase {
    protected final static int DATABASE_VERSION = 5;
    protected final static String NAME = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context context) {
        mHandler = new DatabaseHandler(context, NAME, null, DATABASE_VERSION);
    }

    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
        mDb = null;
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public static void saveDataExternalStorage(Activity activity) {
        boolean success = true;
        try {
            DAOBase dao = new DAOBase(activity);
            dao.open();
            success &= PedometerDAO.saveDataExternalStorage(activity, dao.getDb());
            success &= AccountDAO.saveDataExternalStorage(activity, dao.getDb());
            success &= RecipesDAO.saveDataExternalStorage(activity, dao.getDb());
            dao.close();
        }
        catch (JSONException e) {
            success = false;
        }
        Resources res = activity.getResources();
        Toast.makeText(activity,
                success ? res.getString(R.string.data_saved_success) : res.getString(R.string.data_saved_error),
                Toast.LENGTH_LONG).show();
    }

    public static void loadDataExternalStorage(Activity activity) {
        boolean success = true;
        try {
            DAOBase dao = new DAOBase(activity);
            dao.open();
            success &= PedometerDAO.loadDataExternalStorage(activity, dao.getDb());
            success &= AccountDAO.loadDataExternalStorage(activity, dao.getDb());
            success &= RecipesDAO.loadDataExternalStorage(activity, dao.getDb());
            dao.close();
        }
        catch (JSONException e) {
            success = false;
        }
        Resources res = activity.getResources();
        Toast.makeText(activity,
                success ? res.getString(R.string.data_loaded_success) : res.getString(R.string.data_loaded_error),
                Toast.LENGTH_LONG).show();
    }

    //region UTILS

    public static String formatLongArrayForInQuery(long[] array) {
        StringBuilder inQuery = new StringBuilder("(");
        for (int i = 0; i < array.length; ++i) {
            inQuery.append(i == 0 ? "" : ",").append(String.valueOf(array[i]));
        }
        inQuery.append(")");
        return inQuery.toString();
    }

    //endregion
}
