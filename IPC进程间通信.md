## IPC进程间通信
### 应用层具体流程
    
    _data.writeInterfaceToken(DESCRIPTOR);
    //mRemote就是服务端返回的IBinder对象
    mRemote.transact(Stub.TRANSACTION_getUsername, _data, _reply, 0);
    _reply.readException();
    //读取用户名的数据
    _result = _reply.readString();
    
--- 
    
    case TRANSACTION_getUsername: {
         data.enforceInterface(DESCRIPTOR);
         java.lang.String _result = this.getUsername();
         reply.writeNoException();
         // 写入到
         reply.writeString(_result);
         return true;
    }
    
    
--- 
    private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
        protected IActivityManager create() {
            IBinder b = ServiceManager.getService("activity");
            if (false) {
                Log.v("ActivityManager", "default service binder = " + b);
            }
            IActivityManager am = asInterface(b);
            if (false) {
                Log.v("ActivityManager", "default service = " + am);
            }
            return am;
        }
    };