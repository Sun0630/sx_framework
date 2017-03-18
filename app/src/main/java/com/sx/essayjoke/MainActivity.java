package com.sx.essayjoke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sx.baselibrary.ioc.CheckNet;
import com.sx.baselibrary.ioc.OnClick;
import com.sx.baselibrary.ioc.ViewById;
import com.sx.baselibrary.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv_text)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        mTextView.setText("do it by yourself");
    }

    @OnClick(R.id.tv_text)
    public void clickEvent(View view){
        Toast.makeText(this, "点击事件发生了", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_test)
    @CheckNet
    public void clickImage(View view){
        Toast.makeText(this, "图片被点击了", Toast.LENGTH_SHORT).show();
    }


}
