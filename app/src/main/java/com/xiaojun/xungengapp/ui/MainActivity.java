package com.xiaojun.xungengapp.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.utils.AprilL;
import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;
import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.beans.DataSynEvent;
import com.xiaojun.xungengapp.beans.DengLuBean;
import com.xiaojun.xungengapp.beans.DengLuBeanDao;
import com.xiaojun.xungengapp.beans.MainBean;
import com.xiaojun.xungengapp.dialog.TiJIaoDialog;
import com.xiaojun.xungengapp.utils.ComparatorBeaconByRssi;
import com.xiaojun.xungengapp.utils.SpringEffect;
import com.xiaojun.xungengapp.views.ViewPagerFragmentAdapter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static org.greenrobot.eventbus.EventBus.TAG;

public class MainActivity extends FragmentActivity  {
    private RelativeLayout r1,r2;
    private LinearLayout zhong_ll;
    private ViewPager mViewPager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ImageView tabIm,tabIm2;
    private TextView tabText,tabText2;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private TiJIaoDialog tiJIaoDialog=null;
    private Call call=null;
    private DengLuBean dengLuBean=null;
    private DengLuBeanDao dengLuBeanDao=null;
    // private int maxCount=0;
    //定义一个过滤器；
    private IntentFilter intentFilter;
    //定义一个广播监听器；
    private NetChangReceiver netChangReceiver;
    private final int REQUEST_ENABLE_BT=5678;
    private static final Region ALL_BEACONS_REGION = new Region(
            "customRegionName", null, null, null);
    private BeaconManager beaconManager;
    private ArrayList<Beacon> myBeacons = new ArrayList<Beacon>();;
    private static final String TAG = "MainActivity";
    private static boolean isShow=true;



    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
           // window.setNavigationBarColor(Color.TRANSPARENT);
        }
        EventBus.getDefault().register(MainActivity.this);//订阅

        setContentView(R.layout.activity_main);
        AprilL.enableDebugLogging(false);
        beaconManager = new BeaconManager(MainActivity.this);
        //实例化过滤器；
        intentFilter = new IntentFilter();
        //添加过滤的Action值；
        intentFilter.addAction("guanbiyemian");
        //实例化广播监听器；
        netChangReceiver = new NetChangReceiver();
        //将广播监听器和过滤器注册在一起；
        registerReceiver(netChangReceiver, intentFilter);

        initFragmetList();

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);

        initView();
        initViewPager();


        AndPermission.with(MainActivity.this)
                .requestCode(300)
                .permission(Permission.STORAGE,Permission.CAMERA)
                .callback(listener)
                .start();

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region,
                                            final List<Beacon> beacons) {

//                for (Beacon beacon : beacons) {
//                    if (beacon.getRssi() > 0) {
//                        Log.i(TAG, "rssi = " + beacon.getRssi());
//                        Log.i(TAG, "mac = " + beacon.getMacAddress());
//                    }
//                }
               // Log.i(TAG, "------------------------------beacons.size = " + beacons.size());
                myBeacons.clear();
                myBeacons.addAll(beacons);
                ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
                Collections.sort(myBeacons, com);
                if (isShow){
                    isShow=false;
                    startActivity(new Intent(MainActivity.this,DaKaActivity.class));

                }


            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            public void onExitedRegion(Region arg0) {
                showMSG("进入范围",3);

            }

            @Override
            public void onEnteredRegion(Region arg0, List<Beacon> arg1) {
                showMSG("不在范围",3);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MainBean bean) {
        Log.d(TAG,"收到"+bean.isIstrue());
        isShow=true;
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode,  List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 300) {
             // Initializes Bluetooth adapter.

                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else {
                    //开启扫描
                    Log.d("MainActivity", "扫描开始");

                    connectToService();

                }


            }
        }

        @Override
        public void onFailed(int requestCode,  List<String> deniedPermissions) {
            // 权限申请失败回调。
            showMSG("授权失败,请到设置--软件权限界面重新授权",3);

        }
    };


    /**
     * 连接服务 开始搜索beacon connect service start scan beacons
     */
    private void connectToService() {
        Log.i(TAG, "connectToService");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_BEACONS_REGION);
                    // beaconManager.startMonitoring(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onStop() {
        Log.d(TAG, "停止");

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                //开始扫描
                Log.d("MainActivity", "开始扫描");


                connectToService();

            } else {
                showMSG("开启蓝牙失败,无法巡更,请重新开启蓝牙",3);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private  class NetChangReceiver extends BroadcastReceiver {

        //重写onReceive方法，该方法的实体为，接收到广播后的执行代码；
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("guanbiyemian")){
                finish();
            }
        }
    }


    private void showMSG(final String s,final int i){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast tastyToast= TastyToast.makeText(MainActivity.this,s,TastyToast.LENGTH_LONG,i);
                tastyToast.setGravity(Gravity.CENTER,0,0);
                tastyToast.show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (call!=null)
            call.cancel();
        try {
            myBeacons.clear();
            beaconManager.stopRanging(ALL_BEACONS_REGION);
            beaconManager.disconnect();
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        unregisterReceiver(netChangReceiver);
        if ( EventBus.getDefault().isRegistered(MainActivity.this)){
            EventBus.getDefault().unregister(this);//解除订阅
            Log.d(TAG, "解除订阅");
        }
        super.onDestroy();
    }

    private void initViewPager() {

        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);
        updateBottomLinearLayoutSelect(0);
    }

    private void initFragmetList() {
        Fragment fragment1 = new Fragment1();
        Fragment fragment2 = new Fragment2();
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.viewpage);
        r1= (RelativeLayout) findViewById(R.id.homeLayout);
      //  r1.setOnClickListener(this);
        r2= (RelativeLayout) findViewById(R.id.chosenLayout);
       // r2.setOnClickListener(this);

        tabIm= (ImageView) findViewById(R.id.tabImg);
        tabIm2= (ImageView) findViewById(R.id.tabImg2);

        tabText= (TextView) findViewById(R.id.tabText);
        tabText2= (TextView) findViewById(R.id.tabText2);

        SpringEffect.doEffectSticky(findViewById(R.id.homeLayout), new Runnable() {
            @Override
            public void run() {

                mViewPager.setCurrentItem(0);
                updateBottomLinearLayoutSelect(0);

            }
        });
        SpringEffect.doEffectSticky(findViewById(R.id.chosenLayout), new Runnable() {
            @Override
            public void run() {

                mViewPager.setCurrentItem(1);
                updateBottomLinearLayoutSelect(1);

            }
        });
    }




    private void  updateBottomLinearLayoutSelect(int position) {
        tabText.setTextColor(Color.parseColor("#b4b2b2"));
        tabText2.setTextColor(Color.parseColor("#b4b2b2"));


        switch (position){
            case 0:
                tabIm.setImageResource(R.drawable.ic_home_p);
                tabIm2.setImageResource(R.drawable.ic_my);
                tabText.setTextColor(Color.parseColor("#00C196"));
                break;
            case 1:
                tabText2.setTextColor(Color.parseColor("#00C196"));
                tabIm.setImageResource(R.drawable.ic_home);
                tabIm2.setImageResource(R.drawable.ic_my_p);

                break;

        }

    }
    private class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d(TAG,"onPageScrooled");
        }
        @Override
        public void onPageSelected(int position) {

            updateBottomLinearLayoutSelect(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("home","onPageScrollStateChanged");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            TastyToast.makeText(getApplicationContext(), "再按一次退出程序",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {

            finish();
            System.exit(0);
        }
    }
}
