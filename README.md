## EssayJoke
仿内涵段子

* 模板方法模式构建BaseActivity
* 创建全局的异常捕捉类
* 阿里热修复框架:AndFix
* 自定义热修复框架
* 使用Builder设计模式打造支持链式调用的AlertDialog
* 使用Builder设计模式打造通用的支持链式调用的顶部标题栏
* 封装基于OkHttp的网络引擎，链式调用，便于切换引擎
* 自定义数据库框架(工厂模式+单例模式)并进行优化，数据加密
    * 自定义数据库与网络引擎结合实现数据缓存
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
        
    * 内存泄漏的监测和分析
         1. 垃圾回收问题(GC)
         2. 内存泄漏(不要把Activity的this交出去)