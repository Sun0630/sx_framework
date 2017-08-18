package com.sx.essayjoke;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sx.baselibrary.base.BaseActivity;
import com.sx.essayjoke.fragment.FindFragment;
import com.sx.essayjoke.fragment.FragmentManagerHelper;
import com.sx.essayjoke.fragment.HomeFragment;
import com.sx.essayjoke.fragment.MessageFragment;
import com.sx.essayjoke.fragment.NewFragment;
import com.sx.framelibrary.DefaultNavigationBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_rb)
    RadioButton mHomeRb;
    @BindView(R.id.find_rb)
    RadioButton mFindRb;
    @BindView(R.id.new_rb)
    RadioButton mNewRb;
    @BindView(R.id.msg_rb)
    RadioButton mMsgRb;
    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private MessageFragment mMessageFragment;
    private NewFragment mNewFragment;
    private FragmentManagerHelper mFragmentManagerHelper;
    private DefaultNavigationBar mNavigationBar;
    private ViewGroup mParent;

    @Override
    protected void initTitle() {
        mParent = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        mNavigationBar = new DefaultNavigationBar.Builder(this, mParent)
                .setTitle("首页")
                .hideLeftIcon()
                .build();

    }


    @Override
    protected void initData() {
        mFragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentManagerHelper.add(mHomeFragment);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.home_rb, R.id.find_rb, R.id.new_rb, R.id.msg_rb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_rb:
                setTitle("首页");
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                }
                mFragmentManagerHelper.switchFragment(mHomeFragment);
                break;
            case R.id.find_rb:
                setTitle("发现");
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                }
                mFragmentManagerHelper.switchFragment(mFindFragment);
                break;
            case R.id.new_rb:
                setTitle("新鲜");
                if (mNewFragment == null) {
                    mNewFragment = new NewFragment();
                }
                mFragmentManagerHelper.switchFragment(mNewFragment);
                break;
            case R.id.msg_rb:
                setTitle("消息");
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                }
                mFragmentManagerHelper.switchFragment(mMessageFragment);
                break;
        }
    }

    private void setTitle(String title) {
        TextView textView = mNavigationBar.findViewById(com.sx.framelibrary.R.id.title);
        textView.setText(title);
    }
}
