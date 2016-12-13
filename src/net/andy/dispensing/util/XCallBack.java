package net.andy.dispensing.util;

import net.andy.com.Application;
import net.andy.com.CoolToast;
import org.xutils.common.Callback;

public class XCallBack<ResultType> implements Callback.CommonCallback<ResultType>{
    @Override
    public void onSuccess(ResultType result) {
        //可以根据需求进行统一的请求成功的逻辑处理
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //可以根据需求进行统一的请求网络失败的逻辑处理
        new CoolToast(Application.getContext()).show("网络请求错误，请稍候再试或联系管理员");
    }
    @Override
    public void onCancelled(CancelledException cex) {

    }
    @Override
    public void onFinished() {

    }

}