package ru.aikozin.testvpk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Question extends AppCompatActivity {

    JSONObject jsonConnection;
    int code, codeTest;
    List<Integer> questionOrder = new ArrayList<>();
    int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        try {
            jsonConnection = new JSONObject(getIntent().getExtras().getString("jsonConnection"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        code = getIntent().getExtras().getInt("code");
        codeTest = getIntent().getExtras().getInt("codeTest");
        questionOrder = getIntent().getExtras().getIntegerArrayList("questionOrder");
        order = getIntent().getExtras().getInt("order");

        TextView textView = findViewById(R.id.textView7);
        textView.setText(code + " " + codeTest);
    }
}
