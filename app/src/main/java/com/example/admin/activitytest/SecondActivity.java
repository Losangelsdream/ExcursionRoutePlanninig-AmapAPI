package com.example.admin.activitytest;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private RangeSeekBar timeSeekBar = null;
    private RangeSeekBar financialSeekBar = null;
    private Button planButton = null;
    private CheckBox checkbox1 = null;
    private CheckBox checkbox2 = null;
    private CheckBox checkbox3 = null;
    private CheckBox checkbox4 = null;
    private TextView StartingPoint_view = null;
    private TextView EndPoint_view = null;
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        InitView();
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        StartingPoint_view.setText(address);
    }

    /**
     * 定义界面视图的显现初始化
     */
    private void InitView()
    {
        planButton = (Button) findViewById(R.id.buttonSubscribe);
        checkbox1 = (CheckBox) findViewById(R.id.hobby_view);
        checkbox2 = (CheckBox) findViewById(R.id.hobby_history);
        checkbox3 = (CheckBox) findViewById(R.id.hobby_mall);
        checkbox4 = (CheckBox) findViewById(R.id.hobby_eatery);


        checkBoxList.add(checkbox1);
        checkBoxList.add(checkbox2);
        checkBoxList.add(checkbox3);
        checkBoxList.add(checkbox4);

        StartingPoint_view = findViewById(R.id.start_address);
        EndPoint_view = findViewById(R.id.end_address);

        timeSeekBar = (RangeSeekBar) findViewById(R.id.TimeBar);
        financialSeekBar = (RangeSeekBar) findViewById(R.id.financialBar);

        planButton.setOnClickListener(this);
    }

    /**
     * 规划按钮点击函数
     * @param view
     */
    @Override
    public void onClick(View view) {


        StringBuffer sb = new StringBuffer();
        //遍历集合中的checkBox,判断是否选择，获取选中的文本
        for (CheckBox checkbox : checkBoxList) {
            if (checkbox.isChecked()){
                sb.append(checkbox.getText().toString() + " ");
            }
        }
        if (sb!=null && "".equals(sb.toString())){
            Toast.makeText(getApplicationContext(), "请至少选择一个", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
            float[] results_time = timeSeekBar.getCurrentRange();
            float[] results_financial = financialSeekBar.getCurrentRange();

            JSONObject jo = new JSONObject();
            try {
                jo.put("start_address",StartingPoint_view.getText());
                jo.put("end_address",EndPoint_view.getText());
                jo.put("time_min",(int)results_time[0] );
                jo.put("time_max", (int)results_time[1]);
                jo.put("money_min", (int)results_financial[0]);
                jo.put("money_max", (int)results_financial[1]);
                jo.put("view", checkbox1.isChecked());
                jo.put("history", checkbox2.isChecked());
                jo.put("mall", checkbox3.isChecked());
                jo.put("rest", checkbox4.isChecked());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), jo.toString(), Toast.LENGTH_SHORT).show();
            //Conpute(jo);     //后台计算路径函数
        }
           Intent intent2 = new Intent(SecondActivity.this,ThirdActivity.class);
           startActivity(intent2);
    }
}
