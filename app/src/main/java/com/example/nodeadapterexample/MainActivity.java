package com.example.nodeadapterexample;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.xunevermore.nodeadapter.NodeAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AsyncTask.execute(() -> {
            String string = getFromAssets("city.json");
            List<ProvinceBean> provinceBeans = JSON.parseArray(string, ProvinceBean.class);
            for (ProvinceBean provinceBean : provinceBeans) {
                provinceBean.init();
            }
            runOnUiThread(() -> {
                NodeAdapter nodeAdapter = new NodeAdapter();
                nodeAdapter.replaceAll(provinceBeans);
                recyclerView.setAdapter(nodeAdapter);

            });
        });
    }

    public String getFromAssets(String fileName) {
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null)
                result.append(line);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
