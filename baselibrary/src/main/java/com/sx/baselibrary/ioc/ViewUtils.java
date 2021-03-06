package com.sx.baselibrary.ioc;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author Sunxin
 * @Date 2017/3/17 17:00
 * @Description
 */

public class ViewUtils {

    /**
     * 注入Activity
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        inject(activity,new ViewFinder(activity));
    }


    public static void inject(View view) {
        inject(view,new ViewFinder(view));
    }

    public static void inject(Object object,View view ) {
        inject(object,new ViewFinder(view));
    }

    /**
     * @param viewFinder
     * @param object     反射需要执行的类
     */
    public static void inject(Object object, ViewFinder viewFinder) {
        injectFiled(viewFinder, object);//注入属性
        injectEvent(viewFinder, object);//注入事件
    }


    /**
     * 注入属性
     *
     * @param finder
     * @param object
     */
    private static void injectFiled(ViewFinder finder, Object object) {
        //1,获取所有的属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //2,遍历
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //获取注解里面id的值
                int viewId = viewById.value();
                //通过id找到view
                View view = finder.findViewById(viewId);
                if (view != null) {
                    //设置能够注入所有修饰符权限的属性
                    field.setAccessible(true);
                    //动态的注入到找到view
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    /**
     * 注入事件
     *
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        //1,获取所有的事件
        Class<?> aClass = object.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        //2,遍历
        for (Method method : methods) {
            OnClick click = method.getAnnotation(OnClick.class);
            if (click != null) {
                //3,获取onClick的value值
                int[] viewIds = click.value();
                //4,执行findViewById找到这个view
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);
                    //添加检测网络
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;

                    if (view != null) {
                        //5,setOnClickListener
                        view.setOnClickListener(new DeclaredOnClickListener(method,object,isCheckNet));
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener{

        private Method mMethod;
        private Object mObject;
        private boolean mIsCheckNet;

        public DeclaredOnClickListener(Method method, Object object,boolean isCheckNet) {

            mMethod = method;
            mObject = object;
            mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {
            //点击执行到这里
            if (mIsCheckNet){
                //需要检测网络
                //判断网络是否可用
                if (!NetworkUtils.isAvailable(v.getContext())){
                    Toast.makeText(v.getContext(), "网络不可用哟~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            try {
                //设置权限都可访问
                mMethod.setAccessible(true);
                //6,反射执行方法
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                e.printStackTrace();
                //处理没有传View的可能性
                try {
                    mMethod.invoke(mObject,mObject);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
