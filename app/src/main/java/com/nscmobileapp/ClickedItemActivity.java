package com.nscmobileapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickedItemActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    TextView descriptionView;
    TextView directorView;
    TextView yearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.tvName);
        directorView = findViewById(R.id.director);
        yearView = findViewById(R.id.year);
        descriptionView = findViewById(R.id.description);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            int selectedImage = intent.getIntExtra("image", 0);
            String selectedName = intent.getStringExtra("name");
            String year = intent.getStringExtra("year");
            String director = intent.getStringExtra("director");
            String description = intent.getStringExtra("description");

            imageView.setImageResource(selectedImage);
            textView.setText(selectedName);
            directorView.setText(director);
            yearView.setText(year);
            descriptionView.setText(description);
            descriptionView.setMovementMethod(new ScrollingMovementMethod());

        }
    }
}