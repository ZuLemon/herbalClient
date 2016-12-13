package net.andy.dispensing.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.ReplenishUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReplenishListUI extends Activity {
    private List listAll=null;
    private ListView replenishlist_listView=null;
    private replenishAdapter adapter=null;//自定义的适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replenishlist);
        replenishlist_listView=(ListView)findViewById(R.id.replenishlist_listView);
        ReplenishListThread(0);
        replenishlist_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map replenishMap = ( Map ) ( listAll.get(position) );
         Intent intent = new Intent ();
        Bundle bundle = new Bundle ();
         bundle.putSerializable ( "replenishMap", (Serializable) replenishMap);
        intent.putExtras ( bundle );
        intent.setClass ( ReplenishListUI.this, ReplenishDetailUI.class );
                startActivity ( intent );
            }
        });
    }
    /*    监听ListView      */
//    @Event(value = R.id.replenishlist_listView,type = AdapterView.OnItemClickListener.class)
//    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        new CoolToast(getBaseContext()).show("进入");
//        Map replenishMap = ( Map ) ( listAll.get(position) );
//        List list= (List) replenishMap.get("user");
//        //回传值
//        for (int x = 0; x <list.size() ; x++) {
//            new CoolToast(getBaseContext()).show(((Map)list.get(x)).get("puser").toString());
//        }
////        Intent intent = new Intent ();
////        Bundle bundle = new Bundle ();
////        bundle.putString ( "id", String.valueOf ( map.get ( "equip_id_textView" ) ) );
////        intent.putExtras ( bundle );
////        intent.setClass ( EquipManUI.this, EquipReviseUI.class );
////        EquipManUI.this.startActivity ( intent );
//    }
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
                        adapter=new replenishAdapter(ReplenishListUI.this);
                        replenishlist_listView.setAdapter(adapter);
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
                            listAll = new ReplenishUtil().getReplenishListByUserAndType("常规","申请",null,null);
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
    private class replenishAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public replenishAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listAll.size();
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
            final ReplenishView replenishView;
            if (view == null) {
                view = inflater.inflate(R.layout.replenishlist_item, viewGroup, false);
                replenishView = new ReplenishView();
                replenishView.replenishitem_id  = (TextView) view.findViewById(R.id.replenishitem_id);
                replenishView.replenishitem_name= (TextView) view.findViewById(R.id.replenishitem_name);
                replenishView.replenishitem_count = (TextView) view.findViewById(R.id.replenishitem_count);
                view.setTag(replenishView);
            } else {
                replenishView = (ReplenishView) view.getTag();
            }
            Map map = (Map) listAll.get(i);
            Map herbMap = (Map) map.get("herb");
            replenishView.replenishitem_id.setText(String.valueOf(herbMap.get("herbId")));
            replenishView.replenishitem_name.setText(""+ herbMap.get("herbName"));
            replenishView.replenishitem_count.setText(""+ herbMap.get("ct"));
            return view;
        }
        private class ReplenishView {
            private TextView replenishitem_id;
            private TextView replenishitem_name;
            private TextView replenishitem_count;
        }
    }

}