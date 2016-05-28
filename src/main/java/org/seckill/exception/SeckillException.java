package org.seckill.exception;

/**
 * 秒杀相关异常
 * Created by think on 2016-05-28-0028.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
