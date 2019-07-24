package com.ecommerce.order.common.distributedlock;

import com.ecommerce.order.common.exception.AppException;
import com.ecommerce.order.common.exception.DefaultErrorCode;

import static com.google.common.collect.ImmutableMap.of;

public class LockAlreadyOccupiedException extends AppException {
    public LockAlreadyOccupiedException(String lockKey) {
        super(DefaultErrorCode.LOCK_OCCUPIED, of("lockKey", lockKey));
    }
}
