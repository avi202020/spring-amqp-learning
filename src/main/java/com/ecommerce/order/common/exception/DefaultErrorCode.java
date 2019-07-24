package com.ecommerce.order.common.exception;

public enum DefaultErrorCode implements ErrorCode {
    LOCK_OCCUPIED(409, "任务已被锁定，请稍后重试"),
    SYSTEM_ERROR(500, "系统错误"),
    REQUEST_VALIDATION_FAILED(400, "请求数据格式验证失败"),
    ORDER_NOT_FOUND(404, "没有找到订单");

    private int status;
    private String message;

    DefaultErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
