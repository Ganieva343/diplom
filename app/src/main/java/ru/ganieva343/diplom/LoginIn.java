package ru.ganieva343.diplom;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LoginIn extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor typeCursor;
    SimpleCursorAdapter typeAdapter;

    ArrayList<State> states = new ArrayList<State>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // открываем подключение
        db = databaseHelper.open();;


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogToAddDevice();
            }
        });

        // начальная инициализация списка
        getDevicesWithTypes();
        RecyclerView recyclerView = findViewById(R.id.list);
        // создаем адаптер
        StateAdapter adapter = new StateAdapter(this, states);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        //adapter.setOnItemClickListener(new StateAdapter.OnItemClickListener() {
         //   @Override
        //    public void onItemClick(View view, int position) {
        //        Intent intent = new Intent(LoginIn.this, Update_delete.class);
        //        startActivity(intent);
        //    }
        //});

    }

    private void showDialogToAddDevice() {
        Dialog dialog = new Dialog(LoginIn.this);
        dialog.setContentView(R.layout.dialog_add_device);

        EditText editTextWifiSSID = dialog.findViewById(R.id.editTextWifiSSID);
        EditText editTextWifiPassword = dialog.findViewById(R.id.editTextWifiPassword);
        Button buttonAddWiFi = dialog.findViewById(R.id.buttonAddWiFi);

        buttonAddWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!EditTextEmpty(editTextWifiSSID, editTextWifiPassword)) {
                    return; // Если поля пустые, прекращаем выполнение
                }
                //String deviceName = editTextDeviceName.getText().toString();
                String wifiSSID = editTextWifiSSID.getText().toString();
                String wifiPassword = editTextWifiPassword.getText().toString();

                Toast.makeText(LoginIn.this, "Устройство добавлено", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                Dialog dialog = new Dialog(LoginIn.this);
                dialog.setContentView(R.layout.dialog_update_device);

                TextView textTextTypeDevice = dialog.findViewById(R.id.editTextTypeDevice);
                Spinner spinner = dialog.findViewById(R.id.spinner);
                EditText editTextDeviceName = dialog.findViewById(R.id.editTextDeviceName);
                Button buttonAddDevice = dialog.findViewById(R.id.buttonAddDevice);
                //адаптер для заполнения списка типов
                ArrayAdapter<MyPair> adapter = new ArrayAdapter<>(LoginIn.this, android.R.layout.simple_spinner_item, getAllNamesWithIds());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                //метод сохранения устройства в базе
                buttonAddDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = v;
                        // получение id юзера
                        Intent intent = getIntent();
                        int userId = intent.getIntExtra("id", -1);
                        // получение id типа устройства
                        MyPair selectedDeviceType = (MyPair) spinner.getSelectedItem();
                        int typeId = selectedDeviceType.first;

                        // получение id картинки
                        Cursor imageCursor = db.rawQuery("select " + DatabaseHelper.COLUMN_IDI + " from "+ DatabaseHelper.TABLE5
                                + " where " + DatabaseHelper.COLUMN_IDT + " = " + typeId
                                , null);
                        imageCursor.moveToFirst();

                        int imageId = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IDI));

                        imageCursor.close();

                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.COLUMN_IDuser, userId);
                        cv.put(DatabaseHelper.COLUMN_IDtype, typeId);
                        cv.put(DatabaseHelper.COLUMN_NAMEdevices, editTextDeviceName.getText().toString());
                        cv.put(DatabaseHelper.COLUMN_IDimage, imageId);
                        long result = db.insert(DatabaseHelper.TABLE3, null, cv);
                        if (result > 0) {
                            Snackbar.make(view, "Устройство сохранено", BaseTransientBottomBar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(view, "Ошибка", BaseTransientBottomBar.LENGTH_LONG)
                                    .setTextColor(getColor(R.color.white))
                                    .setBackgroundTint(getColor(R.color.error))
                                    .show();
                        }

                    }
                });
                dialog.show();
            }
        });

        dialog.show();

    }
    //метод проверки поля на заполненность
    private boolean checkEditText(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Поле не заполнено");
            return false;
        }
        return true;
    }

    private boolean EditTextEmpty(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (!checkEditText(editText)) {
                return false;
            }
        }
        return true;
    }

    public List<MyPair> getAllNamesWithIds() {
        List<MyPair> namesAndIdsList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+ DatabaseHelper.TABLE2
                , null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                namesAndIdsList.add(new MyPair(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return namesAndIdsList;
    }

    public class MyPair extends Pair<Integer, String> {
        public MyPair(Integer first, String second) {
            super(first, second);
        }

        @Override
        public String toString() {
            return second;
        }
    }

    //метод вывода устройств в основное окно
    private void getDevicesWithTypes() {
        // Получаем список всех устройств
        String query = "SELECT " + DatabaseHelper.COLUMN_NAMEdevices + ", " + DatabaseHelper.COLUMN_NAMEType +
                ", " + DatabaseHelper.COLUMN_IDimage + " FROM " + DatabaseHelper.TABLE3 +
                " JOIN " + DatabaseHelper.TABLE2 + " ON " + DatabaseHelper.TABLE2 + "." + DatabaseHelper.COLUMN_IDType + " = " +
                DatabaseHelper.TABLE3 + "." + DatabaseHelper.COLUMN_IDtype;;
        Cursor deviceCursor = db.rawQuery(query, null);
        try {
            while (deviceCursor!= null && deviceCursor.moveToNext()) {
                int imageId = deviceCursor.getInt(deviceCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IDimage));

                if (imageId > 0) {
                    Cursor imageCursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_image + " FROM " + DatabaseHelper.TABLE4
                            + " WHERE " + DatabaseHelper.COLUMN_IDImage + " =?", new String[]{String.valueOf(imageId)});
                    if (imageCursor!= null && imageCursor.moveToFirst()) {
                        String imageD = imageCursor.getString(imageCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_image));
                        String deviceName = deviceCursor.getString(deviceCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMEdevices));
                        String deviceType = deviceCursor.getString(deviceCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMEType));

                        Context context = getApplicationContext();

                        // Преобразование строки в идентификатор ресурса
                        int resourceId = context.getResources().getIdentifier(imageD, "drawable", context.getPackageName());

                        states.add(new State(deviceName, deviceType, resourceId));
                    }
                    imageCursor.close();
                }
            }
        } finally {
            if (deviceCursor!= null) {
                deviceCursor.close();
            }
        }
            // Добавляем устройство с типом в список
            //devicesWithTypes.add(new DeviseList(deviceType, deviceName));

            // Получаем тип устройства по ID
            //Cursor typeCursor = db.query(DatabaseHelper.TABLE2, null, DatabaseHelper.COLUMN_IDtype + "=?", new String[]{String.valueOf(deviceType)}, null, null, null);
            //if (typeCursor!= null && typeCursor.moveToFirst()) {
              //  String typeName = typeCursor.getString(typeCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMEType));
              //  typeCursor.close();

                // Добавляем устройство с типом в список
             //   devicesWithTypes.add(new DeviseList(deviceName, typeName));
           // }
    }
        // return devicesWithTypes;

    public class DeviseList extends Pair<String, String> {
        public DeviseList(String  first, String second)
        {
            super(first, second);
        }

        @Override
        public String toString() {
            return first + " : " + second;
        }
    }


}