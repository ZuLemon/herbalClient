package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import net.andy.boiling.R;
import net.andy.dispensing.domain.DispensingDetailDomain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guang on 2016/3/18.
 */
public class DisHistoryUI extends Activity{
    private LinearLayout adjust_history_linearLayout;
    private GridView adjust_history_gridView;
    private SimpleAdapter simpleAdapter;
    private Integer preNum;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<DispensingDetailDomain> dispensingDetailDomainList;
    private List<HashMap<String, Object>> historyData = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_history);
        adjust_history_linearLayout= (LinearLayout) findViewById(R.id.adjust_history_linearLayout);
        adjust_history_gridView = (GridView) findViewById(R.id.adjust_history_gridView);
//        adjust_history_gridView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        adjust_history_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent in=getIntent();
        dispensingDetailDomainList= (List<DispensingDetailDomain>) in.getSerializableExtra("dises");
        preNum=in.getIntExtra("preNum",0);
        simpleAdapter = new SimpleAdapter(this,
                historyData, R.layout.dis_historygriditem, new String[]{"ItemNameView", "ItemNumView"}, new int[]{R.id.adjust_name_textView, R.id.adjust_num_textView});
        adjust_history_gridView.setAdapter(simpleAdapter);
        historyPre();
    }
    private void historyPre() {
        boolean isEnd=true;
        HashMap<String, Object> map;
        int count=dispensingDetailDomainList.size();
        int m=0;
        if(count%2==0){m=count/2;}else{m=count/2+1;}
        for (int i=count-1; i >=0 ; i--) {
            map = new HashMap<String, Object>();
            map.put("ItemNameView", dispensingDetailDomainList.get(i).getHerbName());
            map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get(i).getQuantity())) +dispensingDetailDomainList.get(i).getHerbUnit());
            historyData.add(map);
            simpleAdapter.notifyDataSetChanged();
            if((i-m)<0){break;}
                map = new HashMap<String, Object>();
                map.put("ItemNameView", dispensingDetailDomainList.get((i - m)).getHerbName());
                map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get((i - m)).getQuantity())) + dispensingDetailDomainList.get((i - m)).getHerbUnit());
                historyData.add(map);
                simpleAdapter.notifyDataSetChanged();
                if((i-m)==0&&count%2==0){break;}
//            }

        }

    }
}
