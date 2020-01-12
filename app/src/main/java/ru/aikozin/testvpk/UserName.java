package ru.aikozin.testvpk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserName extends AppCompatActivity {

    int code, codeTest;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        code = getIntent().getExtras().getInt("code");
        codeTest = getIntent().getExtras().getInt("codeTest");
    }

    public void clickStartTest(View view) {
        String editText1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String editText2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String editText3 = ((EditText) findViewById(R.id.editText3)).getText().toString();

        if (editText1.isEmpty() || editText2.isEmpty() || editText3.isEmpty()) {
            Toast.makeText(this, "Некоторые поля пусты", Toast.LENGTH_SHORT).show();
        } else {
            final String name = editText1 + " " + editText2 + " " + editText3;
            final String deviceType = "mobile";

            new Thread() {
                public void run() {
                    String parameters = "type=%s&code=%d&codeTest=%d&deviceType=%s&name=%s";
                    parameters = String.format(parameters, "getQuestionsAndStartTest", code, codeTest, deviceType, name);
                    final JSONObject jsonConnection = API.getJSON(parameters);
                    if (jsonConnection != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    int numberQuestion = jsonConnection.getInt("numberQuestion");
                                    List<Integer> questionOrder = new ArrayList<>();
                                    for (int i = 0; i < numberQuestion; i++)
                                        questionOrder.add(i);
                                    Collections.shuffle(questionOrder);

                                    Intent intent = new Intent(UserName.this, Question.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("jsonConnection", String.valueOf(jsonConnection));
                                    intent.putExtra("code", code);
                                    intent.putExtra("codeTest", codeTest);
                                    intent.putExtra("name", name);
                                    intent.putIntegerArrayListExtra("questionOrder", (ArrayList<Integer>) questionOrder);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
}
