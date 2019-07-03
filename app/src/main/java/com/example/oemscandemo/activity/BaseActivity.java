package com.example.oemscandemo.activity;

import android.app.Activity;
import android.os.Bundle;


public abstract class BaseActivity extends Activity {

//    private ScanDevice mScanDevice;
//    private MediaPlayer mmediaplayer;
//    private Vibrator mvibrator;
//    private EditText showScanResult;
//
//    int Scantime = 0;
//
//    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            SetScantime(1500);
//            mScanDevice.ContinousStart();
//            String action = intent.getAction();
//            if (action != null && action.equals("ACTION_BAR_SCAN")) {
//                String str = intent.getStringExtra("EXTRA_SCAN_DATA");
//                if (str != null) {
//                    mmediaplayer.start();
//                    mvibrator.vibrate(50);
//                }
//                View rootview = BaseActivity.this.getWindow().getDecorView();
//                View focus = rootview.findFocus();
//                if (focus != null) {
//                    ((EditText) focus).setText(str);
//                }
//                ScanEvent();
//            }
//        }
//    };

    protected void ScanEvent() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews(savedInstanceState);
        initValues();
        LogicMethod();

    }

    /**
     * 视图初始化
     *
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 数据初始化
     */
    public abstract void initValues();

    /**
     * 逻辑初始化
     */
    public abstract void LogicMethod();

//    public void SetScantime(int scantime){
//        Scantime = scantime;
//        mScanDevice.TimeScan(scantime);
//    }
//
//
//    // 监听扫描键
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_INFO:
//                // scanning
//                if(Scantime == 0){
//                    new AlertDialog.Builder(BaseActivity.this).setTitle("提示").setMessage("请先设置扫描头读取时间间隔！").setPositiveButton("确定", null).show();
//                }else{
//                    if (event.getRepeatCount() == 0) {
//                        //		mScannerH.openScan();
//                        mScanDevice.ContinousStart();
////                    mScanDevice.startScan();
//                        //SetScantime(1500);
//                    }
//                }
//                return true;
//            case 4:
//                ((Activity) getBaseContext()).finish();
//                return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    //注销数据广播
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        if (mScanDevice != null) {
//            mScanDevice.stopScan();
//        }
//        unregisterReceiver(mScanDataReceiver);
//        mmediaplayer.release();
//
//    }
//
//    //注册数据广播
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//
//        mScanDevice = new ScanDevice(this);  //初始化接口
//
//        IntentFilter scanDataIntentFilter = new IntentFilter();
//        scanDataIntentFilter.addAction("ACTION_BAR_SCAN");
//        registerReceiver(mScanDataReceiver, scanDataIntentFilter);
//
//        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //震动
//        mmediaplayer = new MediaPlayer(); //初始化声音
//        mmediaplayer = MediaPlayer.create(this, R.raw.scanoknew);
//        mmediaplayer.setLooping(false);
//
////        mScanDevice.ContinousStart();
////        if(Scantime != 0){
////            mScanDevice.TimeScan(Scantime);
////        }else {
////            mScanDevice.TimeScan(1500);
////        }
//        super.onResume();
//    }

}
