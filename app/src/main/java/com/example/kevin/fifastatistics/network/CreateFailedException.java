package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;

import lombok.Getter;

/**
 * Exception to be thrown when creation (POST) fails.
 */
public class CreateFailedException extends Exception {

    @Getter private RetrofitErrorManager.ErrorCode errorCode;

    public CreateFailedException() {
        super();
    }

    public CreateFailedException(String detailMessage) {
        super(detailMessage);
    }

    public CreateFailedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        errorCode = RetrofitErrorManager.ErrorCode.fromThrowable(throwable);
    }

    public CreateFailedException(Throwable throwable) {
        super(throwable);
        errorCode = RetrofitErrorManager.ErrorCode.fromThrowable(throwable);
    }
}
