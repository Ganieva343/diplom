package ru.ganieva343.diplom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Update_delete extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_delete);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // открываем подключение
        db = databaseHelper.open();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        int imageResource = intent.getIntExtra("imageResource", R.drawable.light);
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");

        TextView idView = findViewById(R.id.idDevice);
        idView.setText(id);
        idView.setVisibility(View.INVISIBLE);

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(imageResource);

        EditText nameView = findViewById(R.id.name);
        nameView.setText(name);

        TextView typeView = findViewById(R.id.type);
        typeView.setText(type);

        Button saveButton = findViewById(R.id.save);
        Button deleteButton = findViewById(R.id.delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = idView.getText().toString();
                if (!idText.isEmpty()) {
                    try {
                        int deviceId = Integer.parseInt(idText);
                        String delete = " DELETE FROM " + DatabaseHelper.TABLE3 +
                                " Where " + DatabaseHelper.COLUMN_IDdevices + " = " + deviceId;
                        Cursor deleteCursor = db.rawQuery(delete, null);
                        if (deleteCursor.moveToFirst()) {
                            Toast.makeText(Update_delete.this, "Строка не найдена", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Update_delete.this, "Устройство удалено", Toast.LENGTH_SHORT).show();
                            deleteCursor.close();
                        }
                    } finally {
                        finish();
                        db.close();
                    }
                }
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = idView.getText().toString();
                int deviceId = Integer.parseInt(idText);
                String nameText = nameView.getText().toString();
                if (!nameText.isEmpty()) {
                    try {

                        String update = " UPDATE " + DatabaseHelper.TABLE3 + " SET " +
                                DatabaseHelper.COLUMN_NAMEdevices + " = " + nameText +
                        " WHERE "+ DatabaseHelper.COLUMN_IDdevices + " = " + deviceId;
                        Cursor updateCursor = db.rawQuery(update, null);
                        if (updateCursor.moveToFirst()) {
                            Toast.makeText(Update_delete.this, "Строка не найдена", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Update_delete.this, "Данные обновлены", Toast.LENGTH_SHORT).show();
                            updateCursor.close();
                        }
                    } finally {
                        finish();
                        db.close();
                    }
                }
                else {
                    Toast.makeText(Update_delete.this, "Строка 'Имя устройства' пустая", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}