package ru.aikozin.testvpk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Result extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        new Thread() {
            public void run() {
                int numberInApiTable = getIntent().getExtras().getInt("numberInApiTable");
                int codeTest = getIntent().getExtras().getInt("codeTest");
                String parameters = "type=%s&numberInApiTable=%d";
                parameters = String.format(parameters, "testComlition", numberInApiTable);
                final JSONObject jsonConnection = API.getJSON(parameters);
                if (jsonConnection != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                int status = jsonConnection.getInt("status");
                                if (status == 101) {
                                    TextView tvResult = findViewById(R.id.result);
                                    TextView tvResultPerc = findViewById(R.id.resultPerc);
                                    int result = jsonConnection.getInt("result");
                                    int resultPerc = jsonConnection.getInt("resultPerc");
                                    tvResult.setText("" + result);
                                    tvResultPerc.setText("" + resultPerc + "%");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
