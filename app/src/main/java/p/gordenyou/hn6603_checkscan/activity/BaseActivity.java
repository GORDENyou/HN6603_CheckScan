package p.gordenyou.hn6603_checkscan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.ScanDevice;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import p.gordenyou.hn6603_checkscan.R;

public abstract class BaseActivity extends Activity {

    private ScanDevice mScanDevice;
    private MediaPlayer mmediaplayer;
    private Vibrator mvibrator;
    private EditText showScanResult;

    int Scantime;

    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            SystemClock.sleep(1000);
            String action = intent.getAction();
            if (action != null && action.equals("ACTION_BAR_SCAN")) {
                String str = intent.getStringExtra("EXTRA_SCAN_DATA");
                if (str != null) {
                    mmediaplayer.start();
                    mvibrator.vibrate(50);
                }
                View rootview = BaseActivity.this.getWindow().getDecorView();
                View focus = rootview.findFocus();
                if (focus != null) {
                    ((EditText) focus).setText(str);
                }
                ScanEvent();
            }
        }
    };

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

    public void SetScantime(int scantime){
        mScanDevice.TimeScan(scantime);
    }


    // 监听扫描键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_INFO:
                // scanning
                if (event.getRepeatCount() == 0) {
                    //		mScannerH.openScan();
                    mScanDevice.ContinousStart();
                    mScanDevice.startScan();
                }
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //注销数据广播
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(mScanDataReceiver);
        mmediaplayer.release();
        mScanDevice.stopScan();
    }

    //注册数据广播
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        mScanDevice = new ScanDevice(getBaseContext());  //初始化接口

        IntentFilter scanDataIntentFilter = new IntentFilter();
        scanDataIntentFilter.addAction("ACTION_BAR_SCAN");
        registerReceiver(mScanDataReceiver, scanDataIntentFilter);

        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //震动
        mmediaplayer = new MediaPlayer(); //初始化声音
        mmediaplayer = MediaPlayer.create(this, R.raw.scanoknew);
        mmediaplayer.setLooping(false);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScanDevice != null) {
            mScanDevice.closeScan();
        }
    }
}
