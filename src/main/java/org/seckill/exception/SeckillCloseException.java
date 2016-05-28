package org.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by think on 2016-05-28-0028.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
