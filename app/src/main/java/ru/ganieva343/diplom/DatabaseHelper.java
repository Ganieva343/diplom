package ru.ganieva343.diplom;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static final String DB_NAME = "diplom.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "users";
    static final String TABLE2 = "types";
    static final String TABLE3 = "devices";
    // Название столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_E_MAIL = "email";
    static final String COLUMN_PASSWORD = "password";

    // Название столбцы таблицы type
    static final String COLUMN_IDType = "_id";
    static final String COLUMN_NAMEType = "type";

    // Название столбцы таблицы devices
    static final String COLUMN_IDdevices = "_id";
    static final String COLUMN_IDuser = "_id_user";
    static final String COLUMN_IDtype = "_id_type";
    static final String COLUMN_NAMEdevices = "nameTEXT";
    static final String COLUMN_IDimage = "_id_image";
    static final String COLUMN_IP_address = "ip_address";
    static final String COLUMN_IDwifi_network = "wifi_network_id";


    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SURNAME
                + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_E_MAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT); ");

        db.execSQL("CREATE TABLE " + TABLE2 + " (" + COLUMN_IDType
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAMEType + "TEXT); ");

        db.execSQL("CREATE TABLE " + TABLE3 + " (" + COLUMN_IDdevices
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IDuser + " INTEGER, " + COLUMN_IDtype + " INTEGER, " +
                COLUMN_NAMEdevices + "TEXT, " + COLUMN_IDimage + " INTEGER, " + COLUMN_IP_address + " TEXT, " + COLUMN_IDwifi_network + " INTEGER);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users;");
    }
}

