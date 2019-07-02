package com.example.oemscandemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.oemscandemo.R;
import com.example.oemscandemo.common.SaveToExcelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by GORDENyou on 2019/6/29.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public class ReadExcelActivity extends BaseActivity {

    @BindView(R.id.read_listview)
    ListView listView;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_readexcel);
        ButterKnife.bind(this);
    }

    @Override
    public void initValues() {

    }

    @Override
    public void LogicMethod() {
        readExcel(this);
    }

    public void readExcel(Context context) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String logFilePath = SaveToExcelUtil.getExcelDir() + File.separator + "核对记录.xls";
        File file = new File(logFilePath);
        Log.e("yy", "file=" + file.getAbsolutePath());
        try {
            InputStream is = new FileInputStream(file);
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();

            for (int i = 3; i < Rows; ++i) {
                String zhijia = (sheet.getCell(0, i)).getContents();
                String system = (sheet.getCell(1, i)).getContents();
                String jieguo = (sheet.getCell(2, i)).getContents();

                Map<String, String> map = new HashMap<String, String>();

                map.put("zhijia", zhijia);
                map.put("system", system);
                map.put("jieguo", jieguo);
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(context,list,R.layout.listviewitem,
                    new String[]{"zhijia","system","jieguo"},
                    new int[]{R.id.list_zhijia, R.id.list_system, R.id.list_jieguo});
            listView.setAdapter(adapter);
            book.close();

        } catch (Exception e) {

            Log.e("yy", "e" + e);
        }
    }
}
