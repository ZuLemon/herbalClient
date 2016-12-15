package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import net.andy.boiling.Login;
import net.andy.boiling.R;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.DispensingUtil;
import net.andy.dispensing.util.PhotoImageView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class PhotoViewUI extends Activity {
    @ViewInject(R.id.photoImageView)
    private PhotoImageView photoImageView;
//    @ViewInject(R.id.progressBar1)
//    private ProgressBar progressBar1;
    private Bitmap bitmap;
    private Integer pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);
        x.view().inject(this);
        Intent intent=getIntent();
        pid=intent.getIntExtra("pId",0);
        if(pid==0){
             new CoolToast(getBaseContext()).show("参数传递错误");
            finish();
        }else {
            //正在加载
            startActivity(new Intent(PhotoViewUI.this, LoadingUI.class));
        }
        //创建并启动一个新线程用于从网络上下载图片
        new Thread(){
            @Override
            public void run() {
                try {
                    bitmap = new DispensingUtil().getImageByPId(pid);
                    //发送消息，通知UI组件显示图片
                    handler.sendEmptyMessage(0x9527);
                    //关闭输入流
                } catch (Exception e) {
                    new CoolToast(getBaseContext()).show("未上传照片！"+e.getMessage());
                    finish();
//                    e.printStackTrace();
                }
            }
        }.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x9527) {
                //显示从网上下载的图片
//                progressBar1.setVisibility(View.GONE);
                LoadingUI.instance.finish();
                if(bitmap!=null) {
//                    photoImageView.setVisibility(View.VISIBLE);
                    Log.e(">>","显示图片");
                    photoImageView.setImageBitmap(bitmap);
                    Log.e(">>","完成");
                }else{
                    new CoolToast(getBaseContext()).show("未上传照片！");
                    finish();
                }
            }
        }
    };
}
