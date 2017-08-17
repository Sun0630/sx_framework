package com.sx.essayjoke;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.sx.essayjoke.fragment.FindFragment;
import com.sx.essayjoke.fragment.FragmentManagerHelper;
import com.sx.essayjoke.fragment.HomeFragment;
import com.sx.essayjoke.fragment.MessageFragment;
import com.sx.essayjoke.fragment.NewFragment;
import com.sx.framelibrary.BaseSkinActivity;
import com.sx.framelibrary.DefaultNavigationBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseSkinActivity {

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

    @Override
    protected void initTitle() {
        ViewGroup parent = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, parent)
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
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                }
                mFragmentManagerHelper.switchFragment(mHomeFragment);
                break;
            case R.id.find_rb:
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                }
                mFragmentManagerHelper.switchFragment(mFindFragment);
                break;
            case R.id.new_rb:
                if (mNewFragment == null) {
                    mNewFragment = new NewFragment();
                }
                mFragmentManagerHelper.switchFragment(mNewFragment);
                break;
            case R.id.msg_rb:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                }
                mFragmentManagerHelper.switchFragment(mMessageFragment);
                break;
        }
    }
}
