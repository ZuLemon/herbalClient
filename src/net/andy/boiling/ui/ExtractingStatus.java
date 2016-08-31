package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.SolutionDomain;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 *
 * Created by Guang on 2016/8/19.
 */
@ContentView(R.layout.extractstatus)
public class ExtractingStatus extends Activity{
    @ViewInject(R.id.extractStatus_listView)
    private ListView extractStatus_listView;
    private SolutionDomain solutionDomainList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

    }

//    public class SolutionAdapter extends BaseAdapter {
//        private LayoutInflater inflater;
//
//        public SolutionAdapter(Context context) {
//            this.inflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }

//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            final SolutionItem solutionItem;

//            if (view == null) {
//                view = inflater.inflate(R.layout.extractsolutionitem, viewGroup, false);
//                solutionItem = new SolutionItem();
//                solutionItem.solutionItem_id = (TextView) view.findViewById(R.id.solutionItem_id);
//                solutionItem.solutionItem_name = (TextView) view.findViewById(R.id.solutionItem_name);
//                solutionItem.solutionItem_mode = (TextView) view.findViewById(R.id.solutionItem_mode);
//                solutionItem.solutionItem_efficacy = (TextView) view.findViewById(R.id.solutionItem_efficacy);
//                solutionItem.solutionItem_soakTime = (TextView) view.findViewById(R.id.solutionItem_soakTime);
//                solutionItem.solutionItem_extractTime1 = (TextView) view.findViewById(R.id.solutionItem_extractTime1);
//                solutionItem.solutionItem_extractTime2 = (TextView) view.findViewById(R.id.solutionItem_extractTime2);
//                solutionItem.solutionItem_extractTime3 = (TextView) view.findViewById(R.id.solutionItem_extractTime3);
//                solutionItem.solutionItem_count = (TextView) view.findViewById(R.id.solutionItem_count);
//                solutionItem.solutionItem_linearLayout = (LinearLayout) view.findViewById(R.id.solutionItem_linearLayout);
//                view.setTag(solutionItem);
//            } else {
//                solutionItem = (SolutionItem) view.getTag();
//            }
//            SolutionDomain temp = solutionDomainList.get(i);
//            solutionItem.solutionItem_id.setText(String.valueOf(temp.getId()));
//            solutionItem.solutionItem_name.setText(temp.getName());
//            solutionItem.solutionItem_mode.setText(temp.getMode());
//            solutionItem.solutionItem_efficacy.setText(temp.getEfficacy()==null?"    ":temp.getEfficacy());
//            solutionItem.solutionItem_soakTime.setText(temp.getSoakTime()==null?"    ":(temp.getSoakTime() + "分"));
//            solutionItem.solutionItem_extractTime1.setText(temp.getExtractTime1()==null?"    ":(temp.getExtractTime1() + "分"));
//            solutionItem.solutionItem_extractTime2.setText(temp.getExtractTime2()==null?"    ":(temp.getExtractTime2() + "分"));
//            solutionItem.solutionItem_extractTime3.setText(temp.getExtractTime3()==null?"    ":(temp.getExtractTime3() + "分"));
//            solutionItem.solutionItem_count.setText(i + 1 + "");
//            if (i == cur_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
//                solutionItem.solutionItem_linearLayout.setBackground(ltgraydrawable);// 更改整行的背景色
//            } else {
//                solutionItem.solutionItem_linearLayout.setBackground(whitedrawable);// 更改整行的背景色
//            }
//            return view;
//        }
//
//        private class SolutionItem {
//            private TextView solutionItem_count;
//            private TextView solutionItem_name;
//            private TextView solutionItem_mode;
//            private TextView solutionItem_efficacy;
//            private TextView solutionItem_soakTime;
//            private TextView solutionItem_extractTime1;
//            private TextView solutionItem_extractTime2;
//            private TextView solutionItem_extractTime3;
//            private TextView solutionItem_id;
//            private LinearLayout solutionItem_linearLayout;
//        }
}
