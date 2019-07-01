package p.gordenyou.hn6603_checkscan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import p.gordenyou.hn6603_checkscan.R;

/**
 * Created by GORDENyou on 2019/6/29.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public class ListViewAdapter extends ArrayAdapter {

    TextView zhijia, system, jieguo;

    public ListViewAdapter(Context context, int resource, String[] values) {
        super(context, resource, values);
        View view = LayoutInflater.from(context).inflate(R.layout.listviewitem,null);
        zhijia = view.findViewById(R.id.list_zhijia);
        system = view.findViewById(R.id.list_system);
        jieguo = view.findViewById(R.id.list_jieguo);
    }
}
