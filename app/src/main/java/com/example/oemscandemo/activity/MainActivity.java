package com.example.oemscandemo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.ScanDevice;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.common.SaveToExcelUtil;
import com.example.oemscandemo.view.HeaderTitle;
import com.example.oemscandemo.view.ScannerView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends Activity {
    @BindView(R.id.header)
    HeaderTitle headerTitle;
    @BindView(R.id.barcode_zhijia)
    EditText barcode_zhijia;
    @BindView(R.id.barcode_system)
    EditText barcode_system;
    @BindView(R.id.show_zhijia)
    TextView show_zhijia;
    @BindView(R.id.show_system)
    TextView show_system;

    @BindView(R.id.shezhi)
    ScannerView shezhi;

    @BindView(R.id.btn_result)
    Button btn_result;

    SaveToExcelUtil util;

    private ScanDevice mScanDevice;
    private MediaPlayer mmediaplayer;
    private Vibrator mvibrator;
    private EditText showScanResult;

    int Scantime = 0;

    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action != null && action.equals("ACTION_BAR_SCAN")) {
                String str = intent.getStringExtra("EXTRA_SCAN_DATA");
                if (str != null) {
                    mmediaplayer.start();
                    mvibrator.vibrate(50);
                }
                View rootview = MainActivity.this.getWindow().getDecorView();
                View focus = rootview.findFocus();
                if (focus != null) {
                    ((EditText) focus).setText(str);
                }
                ScanEvent();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //SavefiletoCSV.open();
        String excelPath = SaveToExcelUtil.getExcelDir() + File.separator + "核对记录.xls";
        util = new SaveToExcelUtil(MainActivity.this, excelPath);


        SetFocus(shezhi.getEditTextView());
        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ContectActivity.class);
                startActivity(intent);
            }
        });
        shezhi.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetScantime(Integer.parseInt(shezhi.getText()));
                new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage("扫描间隔成功设置为" + shezhi.getText() +
                        "毫秒！").setPositiveButton("确定", null).show();
                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
//                Toast.makeText(MainActivity.this, "扫描间隔成功设置为" + shezhi.getText() +
//                        "毫秒！", Toast.LENGTH_SHORT).show();
                SetFocus(barcode_zhijia);
            }
        });
    }

    public void SetScantime(int scantime) {
        Scantime = scantime;
        mScanDevice.TimeScan(scantime);
    }

    // 监听扫描键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_INFO:
                // scanning
                if (Scantime == 0) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage("请先设置扫描头读取时间间隔！").setPositiveButton("确定", null).show();
                } else {
                    if (event.getRepeatCount() == 0) {
                        //		mScannerH.openScan();
                        mScanDevice.ContinousStart();
                        //SetScantime(1500);
                    }
                }
                return true;
            case 4:
//                finish();
//                unregisterReceiver(mScanDataReceiver);
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void SetFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }


    protected void ScanEvent() {
        if (barcode_zhijia.isFocused() && barcode_system.getText().toString().equals("")) {
            SetFocus(barcode_system);
        }
        if (barcode_system.isFocused() && barcode_zhijia.getText().toString().equals("")) {
            SetFocus(barcode_zhijia);
        }
        if ((!barcode_zhijia.getText().toString().equals("")) && (!barcode_system.getText().toString().equals(""))) {
            SaveData();
        }
    }

    private void SaveData() {
        String str_product = barcode_zhijia.getText().toString();
        String str_package = barcode_system.getText().toString();
        if (str_product.equals(str_package)) {
            btn_result.setText("一致");
            btn_result.setBackgroundColor(getResources().getColor(R.color.OK));
            String[] temp = new String[]{barcode_zhijia.getText().toString(), barcode_system.getText().toString(), "是"};
            util.writeToExcel(temp);
        } else {
            btn_result.setText("不一致");
            btn_result.setBackgroundColor(getResources().getColor(R.color.NG));
            String[] temp = new String[]{barcode_zhijia.getText().toString(), barcode_system.getText().toString(), "否"};
            util.writeToExcel(temp);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
            mediaPlayer.start();
        }
        show_zhijia.setText(barcode_zhijia.getText().toString());
        show_system.setText(barcode_system.getText().toString());
        barcode_system.setText("");
        barcode_zhijia.setText("");
        SetFocus(barcode_zhijia);
    }

    //注销数据广播
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mScanDevice != null) {
            mScanDevice.stopScan();
        }
        try {
//            unregisterReceiver(mScanDataReceiver);
        } catch (Exception e) {

        }
        mmediaplayer.release();

    }

    //注册数据广播
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        mScanDevice = new ScanDevice(this);  //初始化接口

        IntentFilter scanDataIntentFilter = new IntentFilter();
        scanDataIntentFilter.addAction("ACTION_BAR_SCAN");
        registerReceiver(mScanDataReceiver, scanDataIntentFilter);

        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //震动
        mmediaplayer = new MediaPlayer(); //初始化声音
        mmediaplayer = MediaPlayer.create(this, R.raw.scanoknew);
        mmediaplayer.setLooping(false);

//        mScanDevice.ContinousStart();
//        if(Scantime != 0){
//            mScanDevice.TimeScan(Scantime);
//        }else {
//            mScanDevice.TimeScan(1500);
//        }
        super.onResume();
    }

}
