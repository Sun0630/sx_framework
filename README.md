## EssayJoke
仿内涵段子

* 模板方法模式构建BaseActivity
* [创建全局的异常捕捉类](https://sun0630.github.io/2017/03/25/%E6%9E%84%E5%BB%BA%E5%85%A8%E5%B1%80%E7%9A%84%E5%BC%82%E5%B8%B8%E6%8D%95%E6%8D%89%E7%B1%BB/)
* 阿里热修复框架:AndFix
* 自定义热修复框架
* 使用[Builder设计模式](https://sun0630.github.io/2017/08/03/Builder(%E5%BB%BA%E9%80%A0%E8%80%85)%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/)打造支持链式调用的AlertDialog

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
            
* 使用Builder设计模式打造通用的支持链式调用的顶部标题栏

        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.Builder(this)
                    .setTitle("投稿")
                    .setRightText("发布")
                    .setOnRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "已经发布", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setRightIcon(R.mipmap.account_icon_weibo)
                    .build();
                    
* 封装基于OkHttp的网络引擎，链式调用，便于切换引擎

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
                    
* 自定义数据库框架(工厂模式+单例模式)并进行优化，数据加密
    * 自定义数据库与网络引擎结合实现数据缓存
    
             IDaoSupport<Person> daoSupport = DaoSupportFactory
                             .getFactory()
                             .getDao(Person.class);
     
             ArrayList<Person> list = new ArrayList<>();
             for (int i = 0; i < 100; i++) {
                 list.add(new Person("sunxin", 1 + i));
             }
             //测试插入数据的效率，并优化
             long startTime = System.currentTimeMillis();
             daoSupport.insert(list);
             long endTime = System.currentTimeMillis();
             
             Log.e("TAG", "Time---》" + (endTime - startTime));
     
            // 查询所有数据的条目数
             List<Person> persons = daoSupport.querySupport().queryAll();
             Log.e(TAG, "initData: "+persons.size() );
     
     
             // 链式调用查询
             List<Person> value = daoSupport
                     .querySupport()
                     .selection("age = ?")
                     .selectionArgs("24")
                     .query();
             Logger.d("------------" + value.size());
             for (Person person : value) {
                 Log.e(TAG, "initData: " + person.toString());
                 Logger.d(person.toString());
             }
                        
* 插件换肤
    * 资源的加载流程分析
        * Resources与AssetManager源码分析
    * Hook拦截View创建分析
        * setContentView源码分析
        * LayoutInflate源码分析
    * 换肤框架的完善
        1. 保证如果换肤，下次进入还要是新皮肤(保存皮肤状态即可)
        2. 重新进入应用需要换肤
        3. 恢复默认皮肤
        4. 自定义View的换肤(提供接口回调)
        
                 //加载指定皮肤，皮肤文件需要联网获取
                 String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "red.skin";
                 int result = SkinManager.getInstance().loadSkin(skinPath);
                
                
                 //加载默认皮肤
                 int result = SkinManager.getInstance().loadDefault();
                        
    * 内存泄漏的监测和分析
         1. 垃圾回收问题(GC)
         2. 内存泄漏(不要把Activity的this交出去)
         
* IPC进程间通信
    * Binder源码阅读
    * Service保活
        * 进程被杀的原因
            1. 系统内存不足，需要回收，杀掉进程
                * 进程的优先级（前台进程，可见进程，服务进程，后台进程，空进程）
            2. 第三方清理软件会杀掉进程
            3. Rom厂商会在应用退出的时候清理进程
        * 解决方案
            1. 提高进程优先级，比如启动Service的时候调用startForeground()
            2. 当应用退到后台的时候要释放资源，比如轮播图，在退到后台的时候就不需要轮播了
            3. 开启守护进程，避免第三方清理软件杀死进程
            4. 使用JobScheduler，防止手机清除缓存的时候杀死进程
          
* 功能
    * [一种文字两种颜色](https://sun0630.github.io/2017/08/18/%E8%87%AA%E5%AE%9A%E4%B9%89View-%E5%AD%97%E4%BD%93%E5%8F%98%E8%89%B2/)
    * 自定义无限轮播
    * RecyclerView 万能适配器的封装
    * 打造链式调用自定义图片选择器