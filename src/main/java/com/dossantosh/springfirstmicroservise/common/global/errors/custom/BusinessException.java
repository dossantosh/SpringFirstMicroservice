package com.dossantosh.springfirstmicroservise.common.global.errors.custom;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
