package com.sx.essayjoke.imageselector;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sx.baselibrary.ioc.ViewById;
import com.sx.essayjoke.R;
import com.sx.essayjoke.activity.TestImageActivity;
import com.sx.essayjoke.bean.ImageEntity;
import com.sx.framelibrary.BaseSkinActivity;
import com.sx.framelibrary.DefaultNavigationBar;
import com.sx.framelibrary.utils.AlexStatusBarUtils;

import java.util.ArrayList;

/**
 * @Author sunxin
 * @Date 2017/8/18 23:18
 * @Description 图片选择
 */
public class SelectImageActivity extends BaseSkinActivity implements View.OnClickListener, ImageSelectedListener {

    //单选
    public static final int MODE_SINGLE = 0x0012;
    //多选
    public static final int MODE_MULTI = 0x0011;

    //key
    //是否显示相机
    public static final String EXTRA_SHOW_CAMERA = "extra_show_camera";
    //选择的个数
    public static final String EXTRA_SELECT_COUNT = "extra_select_count";
    //原始图片路径
    public static final String EXTRA_DEFAULT_SELECT_LIST = "extra_default_select_list";
    //类型
    public static final String EXTRA_SELECT_MODE = "extra_select_mode";
    //图片列表
    public static final String EXTRA_RESULT = "extra_result";
    private static final int LOADER_TYPE = 0x0021;
    private static final String TAG = "SelectImageActivity";


    /*获取传递过来的参数*/
    //是否需要拍照功能 boolean
    private boolean mShowCamera;
    //每次选中的图片的列表 ArrayList
    private ArrayList<String> mResultList;
    //单选或多选 int类型的type
    private int mMode = MODE_MULTI;
    //图片的张数
    private int mMaxCount = 8;

    @ViewById(R.id.rv_image_list)
    private RecyclerView mRvImageList;
    @ViewById(R.id.select_preview)
    private TextView mSelectPreview;
    @ViewById(R.id.select_number)
    private TextView mSelectNum;
    @ViewById(R.id.select_finish)
    private TextView mSelectFinish;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_select_image);
    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setTitle("所有图片")
                .setColor(Color.parseColor("#261f1f"))
                .setOnLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setTextColor(Color.WHITE)
                .build();

        //改变状态栏的颜色
        AlexStatusBarUtils.setStatusColor(this, Color.parseColor("#261f1f"));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        //1,获取参数
        Intent intent = getIntent();
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECT_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }

        //2,获取图片
        initImageList();

        //3,改变布局显示
        exchangeViewShow();
    }

    /**
     * 改变布局显示
     */
    private void exchangeViewShow() {
        //改变预览两个字的颜色
        if (mResultList.size() > 0) {
            mSelectPreview.setEnabled(true);
            mSelectFinish.setEnabled(true);
            //预览
            mSelectPreview.setOnClickListener(this);
        } else {
            mSelectPreview.setEnabled(false);
            mSelectFinish.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }

        //改变数字
        mSelectNum.setText(mResultList.size() + "/" + mMaxCount);

    }

    /**
     * 使用ContentProvider获取本地图片
     */
    private void initImageList() {
        //耗时操作
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        public String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID,
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //查询
            String selection = IMAGE_PROJECTION[4] + "> 0 AND " + IMAGE_PROJECTION[3] + " =? OR " + IMAGE_PROJECTION[3];
            Log.e(TAG, "onCreateLoader: selection-->" + selection);
//            IMAGE_PROJECTION[4] + '> 0 AND ' + IMAGE_PROJECTION[3] + '=? OR ' + IMAGE_PROJECTION[3]
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + "> 0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //解析，封装name到集合
            if (data != null && data.getCount() > 0) {
                ArrayList<ImageEntity> imageEntities = new ArrayList<>();
                //判断是否显示拍照
                if (mShowCamera) {
                    //放入空字符串
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setPath("");
                    imageEntities.add(imageEntity);
                }
                //遍历
                while (data.moveToNext()) {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dataTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

//                    Logger.e(path + "---" + name + "---" + dataTime);

                    //判断文件是否存在


                    //封装对象
                    ImageEntity imageEntity = new ImageEntity(path, name, dataTime);
                    imageEntities.add(imageEntity);
                }

                //显示列表数据
                showImageList(imageEntities);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 3，显示图片列表
     *
     * @param entities
     */
    private void showImageList(ArrayList<ImageEntity> entities) {
        SelectImageAdapter selectImageAdapter = new SelectImageAdapter(this, entities, mResultList,mMaxCount);
        selectImageAdapter.setOnImageSelectedLisener(this);
        mRvImageList.setLayoutManager(new GridLayoutManager(this, 4));
        mRvImageList.setAdapter(selectImageAdapter);
    }

    @Override
    public void onClick(View v) {
        //点击预览大图
    }

    @Override
    public void select() {
        //图片选择，改变布局
        exchangeViewShow();
    }

    /**
     * 点击确定
     * @param view
     */
    public void selectFinish(View view) {
        Intent intent = new Intent(this, TestImageActivity.class);
        intent.putStringArrayListExtra(EXTRA_RESULT,mResultList);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //1,把拍好的图片加入集合
        //2,调用selectFinish()方法
        //3.发广播通知更新
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE), Uri.parse());
    }
}
