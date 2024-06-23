package ru.ganieva343.diplom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // открываем подключение
        db = databaseHelper.open();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText familyEditText = findViewById(R.id.userFamily);
        EditText nameEditText = findViewById(R.id.userName);
        EditText emailEditText = findViewById(R.id.userEmail);
        EditText passwordEditText = findViewById(R.id.userPassword);

        // получение id юзера
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("id", 2); // defaultValue - это значение по умолчанию, если ключ не найден


        // получение информации о пользователе
        Cursor userCursor = db.rawQuery("select * from "+ DatabaseHelper.TABLE
                        + " where " + DatabaseHelper.COLUMN_ID + " = " + userId
                , null);
        if (userCursor.moveToFirst()) {
            String family = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SURNAME));
            String name = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String email = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_E_MAIL));
            String password = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));

            familyEditText.setText(family);
            nameEditText.setText(name);
            emailEditText.setText(email);
            passwordEditText.setText(password);
        }
        userCursor.close();
    }
}