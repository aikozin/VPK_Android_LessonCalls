package ru.aikozin.testvpk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question extends AppCompatActivity {

    JSONObject jsonConnection;
    int code, codeTest;
    List<Integer> questionOrder = new ArrayList<>();
    int order = 0;
    LinearLayout mainLayout;
    JSONArray questions;
    String name;

    //массивы и объекты для хранения выбранных ответов и их отправки на сервак
    Handler handler = new Handler();
    //для 1 типа
    RadioGroup radioGroup;
    //для 2 типа
    List<CheckBox> idCheckboxes;
    int type, numberInApiTable, countAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mainLayout = findViewById(R.id.mainlayout);

        try {
            //{"status":101,"numberQuestion":2,"questions":[{"number":"0","type":"1","question":"Как тебя зовут?","answer":["Рома","Коля","Петька"]},{"number":"1","type":"2","question":"Жопа?","answer":["Да","Нет","Конечно"]}],"numberInApiTable":"2"}
            jsonConnection = new JSONObject(getIntent().getExtras().getString("jsonConnection"));
            questions = jsonConnection.getJSONArray("questions");
            numberInApiTable = jsonConnection.getInt("numberInApiTable");
            name = getIntent().getExtras().getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        code = getIntent().getExtras().getInt("code");
        codeTest = getIntent().getExtras().getInt("codeTest");
        questionOrder = getIntent().getExtras().getIntegerArrayList("questionOrder");

        setAnswerOnView();
        (findViewById(R.id.next)).setEnabled(false);
    }

    public void setAnswerOnView () {
        ((TextView) findViewById(R.id.numberQuestion)).setText(order + 1 + " вопрос из " + questionOrder.size());
        ((TextView) findViewById(R.id.name)).setText(name);
        mainLayout.removeAllViews();
        (findViewById(R.id.next)).setEnabled(false);
        try {
            JSONObject question = questions.getJSONObject(questionOrder.get(order));
            type = question.getInt("type");
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
                radioGroup = new RadioGroup(Question.this);
                countAnswers = answers.length();
                for (int i = 0; i < countAnswers; i++) {
                    RadioButton radioButton = new RadioButton(Question.this);
                    radioButton.setText(answers.getString(orderAnswer.get(i)));
                    radioButton.setTag(orderAnswer.get(i));
                    radioGroup.addView(radioButton);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        (findViewById(R.id.next)).setEnabled(true);
                    }
                });
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
                countAnswers = answers.length();
                idCheckboxes = new ArrayList<>();
                for (int i = 0; i < answers.length(); i++) {
                    CheckBox checkBox = new CheckBox(Question.this);
                    checkBox.setText(answers.getString(orderAnswer.get(i)));
                    checkBox.setTag(orderAnswer.get(i));
                    idCheckboxes.add(checkBox);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            (findViewById(R.id.next)).setEnabled(true);
                        }
                    });
                    mainLayout.addView(checkBox);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickNext (View view) {
        String answerString = "";
        if (type == 1) {
            int select = (int) (findViewById(radioGroup.getCheckedRadioButtonId())).getTag();
            int[] answer = new int[countAnswers];
            for (int i = 0; i < countAnswers; i++) {
                if (i == select)
                    answer[i] = 1;
                else
                    answer[i] = 0;
            }
            answerString = Arrays.toString(answer).replaceAll(" ", "");
        }
        if (type == 2) {
            int[] answer = new int[countAnswers];
            for (int i = 0; i < countAnswers; i++) {
                int tag = (int) idCheckboxes.get(i).getTag();
                if (idCheckboxes.get(i).isChecked())
                    answer[tag] = 1;
                else
                    answer[tag] = 0;
            }
            answerString = Arrays.toString(answer).replaceAll(" ", "");
        }

        final String finalAnswerString = answerString;
        new Thread() {
            public void run() {
                String parameters = "type=%s&codeTest=%d&numberInApiTable=%d&numberQuestion=%d&answer=%s";
                parameters = String.format(parameters, "setAnswer", codeTest, numberInApiTable, questionOrder.get(order), finalAnswerString);
                final JSONObject jsonConnection = API.getJSON(parameters);
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
                                order++;
                                if (order == questionOrder.size()) {
                                    Intent intent = new Intent(Question.this, Result.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("codeTest", codeTest);
                                    intent.putExtra("numberInApiTable", numberInApiTable);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                                else {
                                    setAnswerOnView();
                                }
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
