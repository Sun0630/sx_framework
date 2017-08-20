package com.sx.essayjoke.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.sx.essayjoke.R;
import com.sx.essayjoke.imageselector.ImageSelector;
import com.sx.essayjoke.imageselector.SelectImageActivity;
import com.sx.framelibrary.BaseSkinActivity;

import java.util.ArrayList;

public class TestImageActivity extends BaseSkinActivity {

    private static final int IMAGE_REQUEST_CODE = 0x0011;
    private static final String TAG = "TestImageActivity";
    private ArrayList<String> mList;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_image);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void selectImage(View view) {
//        Intent intent = new Intent(this, SelectImageActivity.class);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT, 9);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, SelectImageActivity.MODE_MULTI);
//        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
//        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECT_LIST, mList);
//        startActivityForResult(intent, IMAGE_REQUEST_CODE);

        //优化：链式调用图片选择器
        ImageSelector
                .create()
                .origin(mList)
                .Multi()
                .showCamera(true)
                .start(this,IMAGE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_REQUEST_CODE && data != null) {
                mList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);

                Log.e(TAG, "onActivityResult: "+mList.toString() );
            }
        }
    }
}
