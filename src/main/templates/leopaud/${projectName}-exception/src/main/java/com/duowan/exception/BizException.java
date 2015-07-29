package com.duowan.exception;

/**
 * 业务错误
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
