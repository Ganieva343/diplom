package ru.ganieva343.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Update_delete extends AppCompatActivity {

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

        Intent intent = getIntent();
        int imageResource = intent.getIntExtra("imageResource", R.drawable.light);
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(imageResource);

        TextView nameView = findViewById(R.id.name);
        nameView.setText(name);

        TextView typeView = findViewById(R.id.type);
        typeView.setText(type);

    }
}