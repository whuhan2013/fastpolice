package com.zj.base;

import com.baidu.mapapi.SDKInitializer;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by jjx on 2016/6/1.
 */
public class FastPoliceApplication extends SolidApplication{

    private MyLifecycleHandler myLifecycleHandler;
    private static final  String APP_ID = "OLWHHM4o06lSRnqfHF8K5n94-gzGzoHsz";
    private static final  String APP_KEY = "1aaz4nwvMpglAwY29wIUsYKY";
    private static FastPoliceApplication mInstance;
    private final int PEOPELEDITOR=0;
    private final int POLICEEDITOR=1;
    private final int OFFICALEDITOR=2;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private int role;

    @Override
    public void onCreate() {
        myLifecycleHandler = new MyLifecycleHandler();
        registerActivityLifecycleCallbacks(myLifecycleHandler);
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
        //AVOSCloud.initialize(this,APP_ID,APP_KEY);
        mInstance = this;
        SDKInitializer.initialize(getApplicationContext());
        setRole(PEOPELEDITOR);
    }

    public static boolean isBackground(){
        return MyLifecycleHandler.isBackground();
    }

    public static FastPoliceApplication getInstance() {
        return mInstance;
    }
}
