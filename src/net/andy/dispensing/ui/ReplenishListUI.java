package net.andy.dispensing.ui;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.ReplenishUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReplenishListUI extends Activity {
    private List listAll=null;
    private ExpandableListView listView=null;
    private My_Adapter adapter=null;//自定义的适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replenishlist);

        listView=(ExpandableListView)findViewById(R.id.expandablelistview);

        ReplenishListThread(0);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(ReplenishListUI.this,"你点击了:"+adapter.getChild(groupPosition, childPosition).toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
    private void ReplenishListThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        adapter=new My_Adapter(ReplenishListUI.this);
                        listView.setAdapter(adapter);
                        break;
                }
            }
        };

        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    switch (what){
                        case 0:
                            listAll = new ReplenishUtil().getReplenishListByUserAndType("常规","申请");
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    class My_Adapter extends BaseExpandableListAdapter {
        private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private SimpleDateFormat format2 = new SimpleDateFormat("d日 HH:mm");
        private Context context;
        private LayoutInflater father_Inflater=null;
        private LayoutInflater son_Inflater=null;

        private ArrayList<Map> father_array;//父层
        private ArrayList<List<Map>> son_array;//子层

        public My_Adapter(Context context)
        {
            this.context=context;
            father_Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            son_Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Init_data();
        }

        //重写的方法，用于获取子层的内容，这里获取子层的显示字符串
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return son_array.get(groupPosition).get(childPosition);
        }

        //重写的方法，用于获取子层中单项在子层中的位置
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //重写的方法，用于获取子层视图
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            Son_ViewHolder son_ViewHolder=null;
            if(convertView==null)
            {
                convertView=son_Inflater.inflate(R.layout.replenishlist_son, null);
                son_ViewHolder=new Son_ViewHolder();
                son_ViewHolder.son_user=(TextView)convertView.findViewById(R.id.son_user);
                son_ViewHolder.son_id=(TextView)convertView.findViewById(R.id.son_id);
                son_ViewHolder.son_time=(TextView)convertView.findViewById(R.id.son_time);
                son_ViewHolder.son_button= (Button) convertView.findViewById(R.id.son_button);
                convertView.setTag(son_ViewHolder);
            }
            else
            {
                son_ViewHolder=(Son_ViewHolder)convertView.getTag();
            }
                Map sonMap=son_array.get(groupPosition).get(childPosition);
            son_ViewHolder.son_user.setText(sonMap.get("puser").toString());
            son_ViewHolder.son_id.setText(sonMap.get("id").toString());
            try {
                son_ViewHolder.son_time.setText(format2.format(format1.parse(String.valueOf(sonMap.get("sendTime"))))+"申请");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            son_ViewHolder.son_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CoolToast(getBaseContext()).show(sonMap.get("id").toString());
                }
            });
            return convertView;
        }

        //重写的方法，用于获取父层中其中一层的子数目
        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return son_array.get(groupPosition).size();
        }

        //重写的方法，用于获取父层中的一项，返回的是父层的字符串类型
        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return father_array.get(groupPosition);
        }

        //重写的方法，用于获取父层的大小
        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return father_array.size();
        }

        //重写的方法，用于获取父层的位置
        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        //重写的方法，用于获取父层的视图
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Father_ViewHolder father_ViewHolder=null;
            if(convertView==null)
            {
                convertView=father_Inflater.inflate(R.layout.replenishlist_father, null);
                father_ViewHolder=new Father_ViewHolder();

                father_ViewHolder.father_TextView=(TextView)convertView.findViewById(R.id.father_textview);
                father_ViewHolder.image_view=(ImageView)convertView.findViewById(R.id.father_imageview);
                father_ViewHolder.count_view= (TextView) convertView.findViewById(R.id.count_view);
                convertView.setTag(father_ViewHolder);
            }
            else
            {
                father_ViewHolder=(Father_ViewHolder)convertView.getTag();
            }
            Map fatherMap=((Map)father_array.get(groupPosition));
            father_ViewHolder.father_TextView.setText(fatherMap.get("herbName").toString());
            father_ViewHolder.count_view.setText(fatherMap.get("ct").toString());
            if(isExpanded)
            {
                father_ViewHolder.image_view.setImageDrawable(context.getResources().getDrawable(R.drawable.imagetodown));
            }
            else
            {
                father_ViewHolder.image_view.setImageDrawable(context.getResources().getDrawable(R.drawable.imagetoright));
            }
            return convertView;
        }
        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

        //初始化数据，主要是父层和子层数据的初始化
        public void Init_data()
        {
            father_array=new ArrayList<Map>();
            son_array=new ArrayList<List<Map>>();
            List<Map> sonList=null;
            for (int c = 0; c < listAll.size(); c++) {
                Map herbMap= (Map) ((Map)listAll.get(c)).get("herb");
                father_array.add(herbMap);
                List userList=(List)((Map)listAll.get(c)).get("user");
                sonList=new ArrayList<Map>();
                for (int u = 0; u < userList.size(); u++) {
                    sonList.add((Map)userList.get(u));
                }
                son_array.add(sonList);
            }

        }
        public final class Father_ViewHolder
        {
            private TextView father_TextView;
            private ImageView image_view;
            private TextView count_view;
        }
        public final class Son_ViewHolder
        {
            private TextView son_id;
            private TextView son_user;
            private TextView son_time;
            private Button son_button;
        }
    }

}