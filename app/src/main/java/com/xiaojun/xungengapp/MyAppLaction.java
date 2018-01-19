package com.xiaojun.xungengapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.tencent.bugly.Bugly;
import com.xiaojun.xungengapp.beans.DaoMaster;
import com.xiaojun.xungengapp.beans.DaoSession;
import com.xiaojun.xungengapp.beans.DengLuBean;
import com.xiaojun.xungengapp.cookies.CookiesManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2017/7/5.
 */

public class MyAppLaction extends Application {
    // 超时时间
    public static final int TIMEOUT = 1000 * 30;
    public DaoMaster mDaoMaster;
    public DaoSession mDaoSession;
    public static MyAppLaction myAppLaction;


    @Override
    public void onCreate() {
        super.onCreate();

        myAppLaction=this;

        setDatabase();

        try {
            Bugly.init(getApplicationContext(), "d25944f49f", false);

            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//                @Override
//                public void onViewInitFinished(boolean arg0) {
//
//                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                    Log.d("app", " onViewInitFinished is " + arg0);
//                }
//
//                @Override
//                public void onCoreInitFinished() {
//                    QbSdk.reset(getApplicationContext());
//                }
//            };
//            //x5内核初始化接口
//            QbSdk.initX5Environment(getApplicationContext(), cb);

        } catch (Exception e) {
            Log.d("gggg", e.getMessage());

        }


    }

    public static OkHttpClient getOkHttpClient(){

        return new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        //okhttpclient.dispatcher().cancelAll();取消所有的请求
    }


    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "dbweibao", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        if (mDaoSession.getDengLuBeanDao().load(123456L)==null){
            DengLuBean baoCunBean=new DengLuBean();
            baoCunBean.setId(123456L);
            baoCunBean.setZhuji("http://14.23.169.42:8090/api/");
            mDaoSession.getDengLuBeanDao().insert(baoCunBean);
        }else {
           mDaoSession.clear();
        }

    }


    public  DaoSession getDaoSession() {
        return mDaoSession;
    }



 //   Item是项目，plan是维保计划，device是设备台账，menu是维保项，detection是维保项的维保内容措施
  //  parent_id>0的是维保项
//“项目item”，"计划plans"，"维保项menus"，"设备(台账)devices"以及“维保问题及处理措施detections”
    //有dto_result字段。这个字段如果是1表示新增，2表示修改，3表示删除
   // 用fault里面的status
//    '状态，0是待回复，1是回复待审核，2回复审核通过，3回复审核不通过，4处理待审核，5处理审核通过，6处理审核不通过，7已完成处理'
//            0是待提交，也就是客户端报障未提交到后台的状态

//根据角色判断提交

 //   项目id&设备编号&省编号&市编号&单元编号&楼栋编号&楼层编号&功能区编号&细区编号&系统类型编号&子系统编号



}
