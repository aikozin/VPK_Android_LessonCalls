package ru.aikozin.testvpk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question extends AppCompatActivity {

    JSONObject jsonConnection;
    int code, codeTest;
    List<Integer> questionOrder = new ArrayList<>();
    int order;
    LinearLayout mainLayout;
    JSONArray questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mainLayout = findViewById(R.id.mainlayout);

        try {
            //{"status":101,"numberQuestion":2,"questions":[{"number":"0","type":"1","question":"Как тебя зовут?","answer":["Рома","Коля","Петька"]},{"number":"1","type":"2","question":"Жопа?","answer":["Да","Нет","Конечно"]}],"numberInApiTable":"2"}
            jsonConnection = new JSONObject(getIntent().getExtras().getString("jsonConnection"));
            questions = jsonConnection.getJSONArray("questions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        code = getIntent().getExtras().getInt("code");
        codeTest = getIntent().getExtras().getInt("codeTest");
        questionOrder = getIntent().getExtras().getIntegerArrayList("questionOrder");

        int order = 0;
        ((TextView) findViewById(R.id.numberQuestion)).setText(order + 1 + " вопрос из " + questionOrder.size());
        ((TextView) findViewById(R.id.name)).setText("Сашка");
        try {
            JSONObject question = questions.getJSONObject(questionOrder.get(order));
            int type = question.getInt("type");
            if (type == 1) {
                //получаем текст вопроса и выводим на экран
                TextView tvQuestion = new TextView(Question.this);
                tvQuestion.setText(question.getString("question"));
                mainLayout.addView(tvQuestion);

                //получаем все ответы
                JSONArray answers = question.getJSONArray("answer");
                //создаем случайную очередь ответов
                List<Integer> orderAnswer = new ArrayList<>();
                for (int i = 0; i < answers.length(); i++)
                    orderAnswer.add(i);
                Collections.shuffle(orderAnswer);
                //выводим ответы
                RadioGroup radioGroup = new RadioGroup(Question.this);
                for (int i = 0; i < answers.length(); i++) {
                    RadioButton radioButton = new RadioButton(Question.this);
                    radioButton.setText(answers.getString(orderAnswer.get(i)));
                    radioGroup.addView(radioButton);
                }
                mainLayout.addView(radioGroup);
            }

            if (type == 2) {
                //получаем текст вопроса и выводим на экран
                TextView tvQuestion = new TextView(Question.this);
                tvQuestion.setText(question.getString("question"));
                mainLayout.addView(tvQuestion);

                //получаем все ответы
                JSONArray answers = question.getJSONArray("answer");
                //создаем случайную очередь ответов
                List<Integer> orderAnswer = new ArrayList<>();
                for (int i = 0; i < answers.length(); i++)
                    orderAnswer.add(i);
                Collections.shuffle(orderAnswer);
                //выводим ответы
                for (int i = 0; i < answers.length(); i++) {
                    CheckBox checkBox = new CheckBox(Question.this);
                    checkBox.setText(answers.getString(orderAnswer.get(i)));
                    mainLayout.addView(checkBox);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
