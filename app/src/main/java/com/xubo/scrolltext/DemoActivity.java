package com.xubo.scrolltext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xubo.scrolltextview.ScrollTextView;
import com.xubo.scrolltextview.ScrollTextView.OnScrollClickListener;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    List<String> list1 = new ArrayList<String>();

    List<String> list2 = new ArrayList<String>();

    List<String> list3 = new ArrayList<String>();

    List<OnScrollClickListener> listener1 = new ArrayList<OnScrollClickListener>();

    List<OnScrollClickListener> listener2 = new ArrayList<OnScrollClickListener>();

    ScrollTextView text1_stv;

    ScrollTextView text2_stv;

    ScrollTextView text3_stv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        setContentView(R.layout.activity_demo);
        text1_stv = (ScrollTextView)findViewById(R.id.text1_stv);
        text2_stv = (ScrollTextView)findViewById(R.id.text2_stv);
        text3_stv = (ScrollTextView)findViewById(R.id.text3_stv);
        initData();

        text1_stv.setTextContent(list1, listener1);
        text2_stv.setTextContent(list2, listener2);
        text3_stv.setTextContent(list3);
    }
    private void initData()
    {
        // TODO Auto-generated method stub
        list1.add("曼联是英格兰足球史上最为成功的俱乐部之一，也是欧洲乃至世界最具有影响力最成功的的球队之一，共获得20次英格兰顶级联赛冠军，12次英格兰足总杯冠军，4次英格兰联赛杯冠军（除英格兰联赛杯外均为最高纪录）。在欧洲赛场上，曼联共获得3次欧洲冠军联赛冠军，1次欧洲优胜者杯和1次欧洲超级杯冠军。");
        list1.add("2008年曼城被来自中东的阿拉伯财团收购，并夺得2010-11赛季英格兰足总杯冠军，成为曼城近三十五年来首个锦标。2011-12赛季，曼城首次夺得英超联赛冠军，时隔44年重新获得英格兰顶级联赛冠军。2013-14赛季，曼城第二次夺得英格兰超级足球联赛冠军，这也是曼城球队历史的第四个顶级联赛冠军。");
        listener1.add(new OnScrollClickListener()
        {

            @Override
            public void onClick()
            {
                // TODO Auto-generated method stub
                Toast.makeText(DemoActivity.this, "a1", Toast.LENGTH_SHORT).show();
            }
        });
        listener1.add(new OnScrollClickListener()
        {

            @Override
            public void onClick()
            {
                // TODO Auto-generated method stub
                Toast.makeText(DemoActivity.this, "a2", Toast.LENGTH_SHORT).show();
            }
        });

        list2.add("阿布收购切尔西后斥巨资引援，球队逐渐成为豪门。球队以稳如磐石的防守和铁血精神著称，也以过于防守的“摆大巴”战术而蜚声足坛，是足坛防守反击打法的代表球队之一，也是欧洲乃至世界最具有影响力最成功的球队之一。");
        list2.add("利物浦是英格兰足球历史上最成功的俱乐部之一，也是欧洲乃至世界最成功的足球俱乐部之一[1]  。利物浦一共夺取过18次英格兰甲级联赛冠军、7次英格兰足总杯冠军、8次英格兰联赛杯冠军、5次欧洲冠军联赛冠军以及3次欧洲联盟杯冠军，也曾为已解散的G-14创立成员，乃是英格兰历史上最成功的球队之一。");
        listener2.add(new OnScrollClickListener()
        {

            @Override
            public void onClick()
            {
                // TODO Auto-generated method stub
                Toast.makeText(DemoActivity.this, "b1", Toast.LENGTH_SHORT).show();
            }
        });
        listener2.add(new OnScrollClickListener()
        {

            @Override
            public void onClick()
            {
                // TODO Auto-generated method stub
                Toast.makeText(DemoActivity.this, "b2", Toast.LENGTH_SHORT).show();
            }
        });

        list3.add("阿森纳是英超上最具规模的俱乐部之一，从俱乐部成立后共夺得13次取得英格兰顶级联赛的冠军，12次英格兰足总杯的冠军，3次英格兰足球超级联赛的冠军，是英格兰顶级足球联赛停留得最久的俱乐部。");
        list3.add("热刺是二十世纪首支成为联赛及英格兰足总杯双料冠军的球队。是三支可以连夺两届英格兰足总杯的球队之一，亦是唯一曾两度实现这一成绩的球队。在1963年夺得欧洲优胜者杯宝座，是英国首支取得欧洲赛事锦标的队伍。");
    }

    @Override
    protected void onRestart()
    {
        // TODO Auto-generated method stub
        super.onRestart();
        text1_stv.startTextScroll();
        text2_stv.startTextScroll();
        text3_stv.startTextScroll();
    }

    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        text1_stv.stopTextScroll();
        text2_stv.stopTextScroll();
        text3_stv.stopTextScroll();
    }
}
