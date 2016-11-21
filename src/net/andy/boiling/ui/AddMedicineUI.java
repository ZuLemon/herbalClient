package net.andy.boiling.ui;

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
import android.widget.ListView;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.util.BigDecimalUtil;
import net.andy.dispensing.ui.ValidationUI;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/10/28.
 */
@ContentView(R.layout.addmedicine)
public class AddMedicineUI extends Activity {
    private DecimalFormat df1 = new DecimalFormat("#.##");
    @ViewInject(R.id.addmedicine_gridView)
    private GridView addmedicine_listView;
    private Integer presNum;
    private List list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent =getIntent();
        list = (List) intent.getSerializableExtra("addList");
        presNum=intent.getIntExtra("presNum",0);
        GridAdapter gridAdapter=new GridAdapter(this);
        addmedicine_listView.setAdapter(gridAdapter);
        Log.e("List",list.size()+""+presNum);
    }
    @Event(R.id.addmedicine_linearLayout)
    private void btn(View v){
        this.finish();
    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Dis dis;
            if (view == null) {
                view = inflater.inflate(R.layout.addmedicineitem, viewGroup, false);
                dis = new Dis();
                dis.addmedicineItem_name= (TextView) view.findViewById(R.id.addmedicineItem_name);
                dis.addmedicineItem_num = (TextView) view.findViewById(R.id.addmedicineItem_num);
                view.setTag(dis);
            } else {
                dis = (Dis) view.getTag();
            }
            Map map = (Map) list.get(i);
             dis.addmedicineItem_name.setText(map.get("herbName").toString());
//            if("蜂蜜".equals(map.get("herbName").toString())){
//                dis.addmedicineItem_num.setText(df1.format(new BigDecimal(map.get("quantity").toString())) + map.get("herbUnit").toString());
//            }else {
                dis.addmedicineItem_num.setText(df1.format(BigDecimalUtil.multiply(new BigDecimal(map.get("quantity").toString()), BigDecimal.valueOf(presNum))) + map.get("herbUnit").toString());
//            }
            return view;
        }


        private class Dis {
            private TextView addmedicineItem_name;
            private TextView addmedicineItem_num;
        }
    }
}
