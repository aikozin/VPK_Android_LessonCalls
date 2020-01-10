package ru.aikozin.testvpk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            public void run() {
                String parameters = "type=%s";
                parameters = String.format(parameters, "getRunningTests");
                String finalParameters = parameters;
                final JSONObject jsonConnection = API.getJSON(finalParameters);
                if (jsonConnection != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            int status = 0;
                            try {
                                status = jsonConnection.getInt("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status == 101) {
                                Intent intent = new Intent(MainActivity.this, ChangeTest.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("data", jsonConnection.toString());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
