package ru.aikozin.testvpk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeTest extends AppCompatActivity {

    JSONObject json;
    JSONArray data = null;
    List<String> nameTest = new ArrayList<>(),
            discipline = new ArrayList<>(),
            trainingGroup = new ArrayList<>(),
            author = new ArrayList<>(),
            startTime = new ArrayList<>();
    List<Integer> code = new ArrayList<>(),
            codeTest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_test);

        try {
            json = new JSONObject(getIntent().getExtras().getString("data"));
            data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                nameTest.add(data.getJSONObject(i).getString("nameTest"));
                discipline.add(data.getJSONObject(i).getString("discipline"));
                trainingGroup.add(data.getJSONObject(i).getString("trainingGroup"));
                author.add(data.getJSONObject(i).getString("author"));
                int date = data.getJSONObject(i).getInt("startTime");
                DateFormat df = new SimpleDateFormat("HH:mm");
                String strDate = df.format(new Date(date));
                startTime.add(strDate);
                code.add(data.getJSONObject(i).getInt("code"));
                codeTest.add(data.getJSONObject(i).getInt("codeTest"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener viewClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChangeTest.this, UserName.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("code", code.get(position));
                intent.putExtra("codeTest", codeTest.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        };
        listView.setOnItemClickListener(viewClickListener);
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        MyAdapter() {
            mLayoutInflater = LayoutInflater.from(ChangeTest.this);
        }

        @Override
        public int getCount() {
            return nameTest.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.activity_change_test_list, null);

            TextView nameTest_textView = convertView.findViewById(R.id.nameTest);
            nameTest_textView.setText(nameTest.get(position));

            TextView discipline_textView = convertView.findViewById(R.id.discipline);
            discipline_textView.setText(discipline.get(position));

            TextView trainingGroup_textView = convertView.findViewById(R.id.trainingGroup);
            trainingGroup_textView.setText(trainingGroup.get(position));

            TextView author_textView = convertView.findViewById(R.id.author);
            author_textView.setText(author.get(position));

            TextView startTime_textView = convertView.findViewById(R.id.startTime);
            startTime_textView.setText("Запущен: " + startTime.get(position));

            return convertView;
        }
    }
}
