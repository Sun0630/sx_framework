package com.sx.essayjoke;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.baselibrary.dialog.AlertDialog;
import com.sx.baselibrary.fix.FixDexManager;
import com.sx.baselibrary.http.HttpUtils;
import com.sx.essayjoke.model.DiscoverListResult;
import com.sx.essayjoke.model.Person;
import com.sx.framelibrary.BaseSkinActivity;
import com.sx.framelibrary.DefaultNavigationBar;
import com.sx.framelibrary.HttpCallBack;
import com.sx.framelibrary.db.DaoSupportFactory;
import com.sx.framelibrary.db.IDaoSupport;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "Main";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .setRightText("发布")
                .setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "已经发布", Toast.LENGTH_SHORT).show();
                    }
                })
//                .setRightIcon(R.mipmap.account_icon_weibo)
                .build();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        IDaoSupport<Person> daoSupport = DaoSupportFactory
                .getFactory()
                .getDao(Person.class);

        //插入10数据
//        ArrayList<Person> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            list.add(new Person("sunxin", 1 + i));
//        }
//        //测试插入5000条数据的效率，并优化
//        long startTime = System.currentTimeMillis();
//        daoSupport.insert(list);
//        long endTime = System.currentTimeMillis();
//        //优化前：47035 ms
//        //          41801
//        Log.e("TAG", "Time---》" + (endTime - startTime));

        //查询所有数据的条目数
//        List<Person> persons = daoSupport.querySupport().queryAll();
//        Log.e(TAG, "initData: "+persons.size() );



        //链式调用查询
//        List<Person> value = daoSupport
//                .querySupport()
//                .selection("age = ?")
//                .selectionArgs("24")
//                .query();
//        Logger.d("------------" + value.size());
//        for (Person person : value) {
//            Log.e(TAG, "initData: " + person.toString());
//            Logger.d(person.toString());
//        }

        //自定义热修复
//        fixDexBug();

        //阿里热修复
//        andFix();

        //网络请求
        HttpUtils
                .with(this)
                .get()
                .url("http://is.snssdk.com/2/essay/discovery/v3/")//路径和参数都需要放入到jni中
                .cache(true)//添加缓存
                .addParams("iid", "6152551759")
                .addParams("aid", "7")
                .excute(new HttpCallBack<DiscoverListResult>() {

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onSuccess(DiscoverListResult result) {
                        // result --> 对象，会添加缓存功能
                        Log.e("请求的最终结果", result.getData().getCategories().getName());

                    }
                });

        /**
         * 遗留的问题：
         * 1，请求参数，有很多都是公用的，可以抽取出来
         * 2，JSON转换成对象，不能使用泛型
         * 3，数据库的问题，数据缓存问题，第三方数据库都是缓存在/data/data/<packageName>/database/下
         *
         * 工厂设计模式+单例设计模式 --->> UML图
         */

    }

    /**
     * 自定义热修复
     */
    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDexBug(fixFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void andFix() {
        //下次进入应用获取到这个文件
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();

        //每次启动的时候，就要去后台获取差分包 fix.patch ，然后修复bug

        //差分包需要请求接口拿到一个文件，这里只做测试，保存到本地一个fix.patch
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");

        if (fixFile.exists()) {
            //如果文件存在，就去修复bug。
            try {
                MyApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void click(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.detail_common_dialog)
                .fromBottom(true)
                .fullWidth()
                .show();

        final EditText et_common = dialog.getView(R.id.comment_editor);
        dialog.setText(R.id.submit_btn, "发送");
        dialog.setOnClickListener(R.id.submit_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, et_common.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
