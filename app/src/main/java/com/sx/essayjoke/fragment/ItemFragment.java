package com.sx.essayjoke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sx.essayjoke.R;

/**
 * @Author sunxin
 * @Date 2017/8/17 17:43
 * @Description
 */

public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String item) {
        Bundle args = new Bundle();
        args.putString("title",item);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item,null,false);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        Bundle b = getArguments();
        textView.setText(b.getString("title"));
        return view;
    }
}
