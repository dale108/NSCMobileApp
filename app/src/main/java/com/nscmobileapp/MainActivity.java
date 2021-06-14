package com.nscmobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    //proof of recency
    GridView gridView;
    EditText emailElement;
    EditText userNameElement;
    EditText passwordElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new ButtonAdapter(this));

        userNameElement = findViewById(R.id.user_name);
        userNameElement.setText(getSharedpref("username"));

        emailElement = findViewById(R.id.email_address);
        emailElement.setText(getSharedpref("email"));

        passwordElement = findViewById(R.id.password);
        passwordElement.setText(getSharedpref("password"));
    }


    private void signIn(String email, String password, String displayname) {
        Log.d("FIREBASE", "signIn");
        // 1 - validate display name, email, and password entries
        // 2 - save valid entries to shared preferences
        // 3 - sign into Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());
                boolean successful = task.isSuccessful();
                if (task.isSuccessful()) {
                    // update profile. displayname is the value entered in UI
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FIREBASE", "User profile updated.");
                                // Go to FirebaseActivity
                                startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                            }
                        }
                    });
                } else {
                    Log.d("FIREBASE", "sign-in failed");
                    Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private String getSharedpref(String key)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "default");
    }


    public void buttonHandler(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String email = emailElement.getText().toString();
        String username = userNameElement.getText().toString();
        String password = passwordElement.getText().toString();
        InputValidator.setSharedpreferance(pref,"usernameText", username);
        InputValidator.setSharedpreferance(pref,"emailText", email);
        InputValidator.setSharedpreferance(pref,"passwordText", password);

        InputValidator.ValidationState validationState = InputValidator.validateInputs(email,
                password, username);
        if (validationState.isValid) {
            signIn(getSharedpref("emailText"), getSharedpref("passwordText"), getSharedpref("usernameText"));
        } else {
            Toast.makeText(view.getContext(), validationState.message, Toast.LENGTH_SHORT).show();
        }
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
                    if (v.getId() == 0) {
                        startActivity(new Intent(MainActivity.this, TrafficActivity.class)
                                .putExtra("showMap", true));
                    }
                    if (v.getId() == 1) {
                        Intent intent = new Intent(MainActivity.this, ShowMovies.class);
                        startActivity(intent);
                    } else if (v.getId() == 2) {
                        if (hasConnection()) {
                            startActivity(new Intent(MainActivity.this, TrafficActivity.class)
                                    .putExtra("showMap", false));
                        } else {
                            Toast.makeText(v.getContext(),
                                    "Please check you connection and try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            return button;
        }
    }

    private boolean hasConnection() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return activeInfo.getType() == ConnectivityManager.TYPE_WIFI || activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            return false;
        }
    }

    public SharedPreferences prefs() {
        return getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

    }
}