package ru.ganieva343.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    EditText editTextName;
    EditText editTextSurname;
    EditText editTextEmail;
    EditText editTextPassword;
    long userId=0;
    boolean isAllFieldsChecked = false;

    Button buttonRegister;
    Button buttonlogin;
    Button buttonzaRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextName = findViewById(R.id.PersonName);
        editTextSurname = findViewById(R.id.PersonFamily);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        buttonRegister = findViewById(R.id.register);
        buttonlogin = findViewById(R.id.loginr);
        buttonzaRegister = findViewById(R.id.zaregister);
        //делаем поля не видимыми
        editTextSurname.setVisibility(View.INVISIBLE);
        editTextName.setVisibility(View.INVISIBLE);
        buttonzaRegister.setVisibility(View.INVISIBLE);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        // создаем базу данных

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Проверяем текущее состояние видимости EditText
                if (editTextSurname.getVisibility() == View.INVISIBLE && editTextName.getVisibility() == View.INVISIBLE) {
                    // Если EditText невидим, делаем его видимым
                    editTextSurname.setVisibility(View.VISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                    buttonzaRegister.setVisibility(View.VISIBLE);
                    buttonlogin.setVisibility(View.INVISIBLE);
                    buttonRegister.setText("Вход");
                } else {
                    editTextSurname.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.INVISIBLE);
                    buttonzaRegister.setVisibility(View.INVISIBLE);
                    buttonlogin.setVisibility(View.VISIBLE);
                    buttonRegister.setText("Зарегистрироваться");
                    //Toast.makeText(MainActivity.this, "Ошибка, возможно некоторые поля незаполнены", Toast.LENGTH_SHORT).show();
                    //сюда можно вставить код авторизации
                }
            }
        });

        buttonzaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked = CheckAllFields();

                if (isAllFieldsChecked){
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_NAME, editTextName.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_SURNAME, editTextSurname.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_E_MAIL, editTextEmail.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_PASSWORD, editTextPassword.getText().toString());
                    long result = db.insert(DatabaseHelper.TABLE, null, cv);
                    if (result > 0) {
                        Snackbar.make(view, "Пользователь зарегистрирован", BaseTransientBottomBar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, "Ошибка", BaseTransientBottomBar.LENGTH_LONG)
                                .setTextColor(getColor(R.color.white))
                                .setBackgroundTint(getColor(R.color.error))
                                .show();
                    }

                    editTextSurname.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.INVISIBLE);
                    buttonzaRegister.setVisibility(View.INVISIBLE);
                    buttonlogin.setVisibility(View.VISIBLE);
                    buttonRegister.setVisibility(View.VISIBLE);
                }

            }
        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCursor = db.rawQuery("select * from "+ DatabaseHelper.TABLE
                        , null);
                userCursor.moveToFirst();
                boolean isFind = false;
                try {
                    for (int i = 0; i < userCursor.getCount() && !userCursor.isAfterLast(); i++) {
                        if (userCursor
                                .getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_E_MAIL))
                                .equals(String.valueOf(editTextEmail.getText().toString()))) {
                            isFind = true;

                            if (userCursor
                                    .getString( userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
                                    .equals(String.valueOf(editTextPassword.getText().toString()))) {
                                Snackbar.make(view, "Успешно", BaseTransientBottomBar.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, LoginIn.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                //отправка id пользователя в активность LoginIn
                                int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                                Intent intent = new Intent(view.getContext(), LoginIn.class);
                                intent.putExtra("id", id); // id - переменная, содержащая ID пользователя
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "Неправильный пароль", BaseTransientBottomBar.LENGTH_LONG)
                                        .setTextColor(getColor(R.color.white))
                                        .setBackgroundTint(getColor(R.color.error))
                                        .show();
                            }
                        }
                        userCursor.moveToNext();
                    }
                    if (!isFind) {
                        Snackbar.make(view, "Пользователь не найден", BaseTransientBottomBar.LENGTH_LONG)
                                .setBackgroundTint(getColor(R.color.error))
                                .show();
                    }
                } catch (Exception e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    Snackbar.make(view, "Нет пользователей", BaseTransientBottomBar.LENGTH_LONG)
                            .setTextColor(getColor(R.color.white))
                            .setBackgroundTint(getColor(R.color.error))
                            .show();
                    Log.e("Error find", errors.toString());
                }
            }
        });

    }
    private boolean CheckAllFields() {

        if (editTextSurname.getText().toString().isEmpty()) {
            editTextSurname.setError("Поле не заполнено");
            return false;
        }

        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError("Поле не заполнено");
            return false;
        }


        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("Поле не заполнено");
            return false;
        }

        if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError("Поле не заполнено");
            return false;
        } else if (editTextPassword.length() < 8) {
            editTextPassword.setError("В пароле должно быть минимум 8 символов");
            return false;
        }

        return true;
    }

}