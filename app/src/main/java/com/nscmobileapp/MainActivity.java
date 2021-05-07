package com.nscmobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new ButtonAdapter(this));
    }

    public class ButtonAdapter extends BaseAdapter {
        private Context mContext;
        private int buttonId;
        private int total_buttons = 6;

        public ButtonAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return total_buttons;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            Button button;
            final Map<Integer, String> fieldMap = Map.of(0, "CITIES", 1, "MOVIES", 2, "TRAFFIC",
                    3, "PARKS", 4, "MUSIC", 5, "FOOD");

            if (view == null) {
                button = new Button(mContext);
                button.setText(fieldMap.get(buttonId));
                button.setId(buttonId);
                buttonId++;
            } else {
                button = (Button) view;
            }

            button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Button #" + (i + 1), Toast.LENGTH_SHORT).show();
                    if (v.getId() == 1) {
                        Intent intent = new Intent(MainActivity.this, ShowMovies.class);
                        startActivity(intent);
                    } else if (v.getId() == 2) {
                        Intent intent2 = new Intent(MainActivity.this, TrafficActivity.class);
                        startActivity(intent2);
                    }
                }
            });
            return button;
        }
    }
}