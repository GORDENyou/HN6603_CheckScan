package p.gordenyou.hn6603_checkscan.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.gordenyou.hn6603_checkscan.R;
import p.gordenyou.hn6603_checkscan.common.SaveToExcelUtil;
import p.gordenyou.hn6603_checkscan.view.HeaderTitle;
import p.gordenyou.hn6603_checkscan.view.ScannerView;

public class MainActivity extends BaseActivity {
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

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //SavefiletoCSV.open();
        String excelPath = SaveToExcelUtil.getExcelDir() + File.separator + "核对记录.xls";
        util = new SaveToExcelUtil(MainActivity.this, excelPath);
    }

    @Override
    public void initValues() {

    }

    @Override
    public void LogicMethod() {
        SetScantime(1500);
        SetFocus(barcode_zhijia);
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
            }
        });
    }

    private void SetFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @Override
    protected void ScanEvent() {
        if (barcode_zhijia.isFocused() && barcode_system.getText().toString().equals("")) {
            SetFocus(barcode_system);
        }
        if (barcode_system.isFocused() && barcode_zhijia.getText().toString().equals("")) {
            SetFocus(barcode_zhijia);
        }
        if((!barcode_zhijia.getText().toString().equals("")) && (!barcode_system.getText().toString().equals(""))){
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
}
