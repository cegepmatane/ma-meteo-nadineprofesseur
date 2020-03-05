package org.appducegep.mameteo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.util.Date;

// https://developer.android.com/training/data-storage/sqlite.html

public class MeteoDAO extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Meteo.db";

    public MeteoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    String SQL_CREATION_TABLE = "create table meteo(id INTEGER PRIMARY KEY, ville TEXT, vent TEXT, soleilOuNuage TEXT, date TEXT)";
    String SQL_MISEAJOUR_TABLE_METEO_1_A_2 = "alter table meteo add column vent TEXT";
    String SQL_MISEAJOUR_TABLE_METEO_2_A_1 = "alter table meteo drop column vent";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int avant, int apres) {
        if(1 == avant && 2 == apres)
        {
            db.execSQL(SQL_MISEAJOUR_TABLE_METEO_1_A_2);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int avant, int apres) {
        if(2 == avant && 1 == apres)
        {
            db.execSQL(SQL_MISEAJOUR_TABLE_METEO_2_A_1);
        }
    }

    public void ajouterMeteo(String soleilOuNuage, String vent)
    {
        //Date aujourdhui = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues meteoDuJour = new ContentValues();
        meteoDuJour.put("ville", "Matane");
        meteoDuJour.put("soleilOuNuage", soleilOuNuage);
        meteoDuJour.put("vent", vent);
        meteoDuJour.put("date", DateFormat.format("MMMM d, yyyy ", (new Date()).getTime()).toString());
        long newRowId = db.insert("meteo", null, meteoDuJour);

    }
}
