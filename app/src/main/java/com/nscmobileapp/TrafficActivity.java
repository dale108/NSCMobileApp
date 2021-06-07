package com.nscmobileapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nscmobileapp.SingletonRequestQueue;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TrafficActivity extends AppCompatActivity {
    private static final Gson gson = new Gson();
    GridView gridView;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        gridView = findViewById(R.id.gridview);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Map<String, Object> map = gson.fromJson(response, Map.class);
                        Optional.ofNullable(map.get("Features"))
                                .map(list -> (List) list)
                                .ifPresent(jsonToParse -> {
                                    List<Camera> cameraList = makeCameraList(jsonToParse);
                                    if (getIntent().getBooleanExtra("showMap", false)) {
                                        ArrayList<String> formattedCameraData = cameraList.stream()
                                                .map(cam -> cam.getFormattedLocationData()).collect(Collectors.toCollection(ArrayList::new));
                                        startActivity(
                                                new Intent(TrafficActivity.this, MapActivity.class).putExtra("positionList", formattedCameraData));
                                    } else {
                                        initializeView(cameraList);
                                    }
                                });
                    }
                }, error ->
                Log.getStackTraceString(new Exception()));
        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void initializeView(List<Camera> collect) {
        CustomAdapter customAdapter = new CustomAdapter(collect, this);
        gridView.setAdapter(customAdapter);
    }

    private ArrayList<String> extractCoordinates(final List<Map> jsonToParse) {
        return jsonToParse.stream().map(camElement -> camElement.get("PointCoordinate"))
                .map(camElement -> (List) camElement)
                .map(camElement -> camElement.get(0) + " " + camElement.get(1))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private List<Camera> makeCameraList(List<Map> jsonToParse) {
        List<Camera> cameraList = new ArrayList<>();
        for (Map camMap : jsonToParse) {
            List nestedList = (List) camMap.get("Cameras");
            Map camDetails = (Map) nestedList.get(0);
            List<Double> coordinateList = (List<Double>) camMap.get("PointCoordinate");

            cameraList.add(new Camera(coordinateList.get(0), coordinateList.get(1),
                    (String) camDetails.get("Id"), (String) camDetails.get("Description"),
                    (String) camDetails.get("ImageUrl"), (String) camDetails.get("Type")));
        }
        return cameraList;
    }

    public class CustomAdapter extends BaseAdapter {
        List<Camera> cameraList;
        private LayoutInflater layoutInflater;

        public CustomAdapter(List<Camera> cameraList, Context context) {
            this.cameraList = cameraList;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return cameraList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_items, parent, false);
            }

            TextView tvName = convertView.findViewById(R.id.tvName);
            ImageView imageView = convertView.findViewById(R.id.imageView);

            tvName.setText(cameraList.get(position).getDescription());
            Picasso.get().load("https://www.seattle.gov/trafficcams/images/" + cameraList.get(position).getImageUrl()).into(imageView);
            return convertView;
        }
    }
}