package org.seckill.dto;

/**
 * Created by think on 2016-05-28-0028.
 */
//所有ajax请求放回类型，封装json结果
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillResult(T data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public SeckillResult(String error, boolean success) {
        this.error = error;
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
