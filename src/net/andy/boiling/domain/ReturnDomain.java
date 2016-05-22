package net.andy.boiling.domain;

/**
 * 服务器返回对象
 */
public class ReturnDomain {
    private Boolean success = true;
    private Object object = null;
    private String exception = "";

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        String ret = "success=" + success + ",object=" + object + ",exception=" + exception;
        return ret;
    }
}
