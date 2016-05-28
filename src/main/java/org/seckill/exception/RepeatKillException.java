package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 * Spring声明式事务只接受运行时异常的回滚
 * Created by think on 2016-05-28-0028.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
