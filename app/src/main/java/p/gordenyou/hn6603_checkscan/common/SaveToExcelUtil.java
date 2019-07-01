package p.gordenyou.hn6603_checkscan.common;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by GORDENyou on 2019/6/28.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public class SaveToExcelUtil {
    private WritableWorkbook wwb;
    private String excelPath;
    private File excelFile;
    private Activity activity;

    public SaveToExcelUtil(Activity activity, String excelPath) {
        this.excelPath = excelPath;
        this.activity = activity;
        excelFile = new File(excelPath);
        createExcel(excelFile);

    }

    // 创建excel表.
    public void createExcel(File file) {
        WritableSheet ws = null;
        try {
            if (!file.exists()) {
                wwb = Workbook.createWorkbook(file);

                ws = wwb.createSheet("sheet1", 0);

                // 在指定单元格插入数据
                Label lbl0 = new Label(0, 0, "预捆绑支架与输送系统条码核对记录");
                Label lbl1 = new Label(0, 1, "批号：");
                Label lbl2 = new Label(1, 1, "数量：");
                Label lbl3 = new Label(2, 1, "操作人：");
                Label lbl4 = new Label(0, 2, "支架条码序号");
                Label lbl5 = new Label(1, 2, "输送系统条码序号");
                Label lbl6 = new Label(2, 2, "核对是否一致（√/×）");

                ws.addCell(lbl0);
                ws.addCell(lbl1);
                ws.addCell(lbl2);
                ws.addCell(lbl3);
                ws.addCell(lbl4);
                ws.addCell(lbl5);
                ws.addCell(lbl6);

                // 从内存中写入文件中
                wwb.write();
                wwb.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将数据存入到Excel表中
    public void writeToExcel(String[] args) {

        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);
            wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);
            // 当前行数
            int row = ws.getRows();
            Label lab1 = new Label(0, row, args[0] + "");
            Label lab2 = new Label(1, row, args[1] + "");
            Label lab3 = new Label(2, row, args[2] + "");
//            Label lab4 = new Label(3, row, args[3] + "");
            ws.addCell(lab1);
            ws.addCell(lab2);
            ws.addCell(lab3);
//            ws.addCell(lab4);

            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();
            Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getExcelDir() {
        // SD卡指定文件夹
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString();
        File dir = new File(sdcardPath + File.separator + "Excel"
                + File.separator + "Person");

        if (dir.exists()) {
            return dir.toString();

        } else {
            dir.mkdirs();
            Log.e("BAG", "保存路径不存在,");
            return dir.toString();
        }
    }
}
