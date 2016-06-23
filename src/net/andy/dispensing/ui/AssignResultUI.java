package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import net.andy.boiling.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/6/22.
 */
public class AssignResultUI extends Activity {
    private LinearLayout assignresult_linearLayout;
    private GridView assignresult_gridView;
    private List resultList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignresult);
        assignresult_linearLayout= (LinearLayout) findViewById(R.id.assignresult_linearLayout);
        assignresult_gridView= (GridView) findViewById(R.id.assignresult_gridView);
        Intent in=getIntent();
        resultList = JSONArray.parseArray(in.getStringExtra("result"));

        assignresult_gridView.setAdapter(new GridAdapter(this));
        assignresult_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        LinearLayout.LayoutParams params;

        public GridAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return resultList.size();
        }

        public Object getItem(int position) {
            return resultList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Hold hold;
            if (convertView == null)
            {
                hold=new Hold();
                convertView = mInflater.inflate(R.layout.assignresultitem, null);
                hold.assignresult_name_textView= (TextView) convertView.findViewById(R.id.assignresult_name_textView);
                hold.assignresult_num_textView= (TextView) convertView.findViewById(R.id.assignresult_num_textView);
                convertView.setTag(hold);
            } else
            {
                hold = (Hold) convertView.getTag();
            }
            Map map=(Map)resultList.get(position);
            hold.assignresult_name_textView.setText(map.get("uname").toString());
            hold.assignresult_num_textView.setText(map.get("presNumber").toString()+"付");
            return convertView;
        }
        class Hold
        {
            protected TextView assignresult_name_textView;
            protected TextView assignresult_num_textView;
        }

    }
}
