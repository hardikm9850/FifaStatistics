package com.example.kevin.fifastatistics.managers;

import android.app.Activity;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.utils.ResourceUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import lombok.Getter;
import retrofit2.adapter.rxjava.HttpException;

public class RetrofitErrorManager {

    private static final String TAG = "Error Manager";

    public enum ErrorCode {
        SERVER_UNRESPONSIVE(-1, ResourceUtils.getStringFromResourceId(R.string.unable_to_contact_server)),
        NO_NETWORK(-2, ResourceUtils.getStringFromResourceId(R.string.no_network)),
        UNKNOWN_ERROR(-3, ResourceUtils.getStringFromResourceId(R.string.unknown_error)),
        NOT_FOUND(404, ResourceUtils.getStringFromResourceId(R.string.not_found));

        @Getter private int code;
        @Getter private String message;

        ErrorCode(int value, String message) {
            this.code = value;
            this.message = message;
        }

        public static ErrorCode fromValue(int value) {
            for (ErrorCode code : ErrorCode.values()) {
                if (code.getCode() == value) {
                    return code;
                }
            }
            return UNKNOWN_ERROR;
        }

        public static ErrorCode fromThrowable(Throwable t) {
            if (t instanceof HttpException) {
                HttpException ex = (HttpException) t;
                return fromValue(ex.code());
            } else if (t instanceof SocketTimeoutException) {
                return SERVER_UNRESPONSIVE;
            } else if (t instanceof IOException) {
                return NO_NETWORK;
            } else {
                return UNKNOWN_ERROR;
            }
        }
    }

    public static ErrorCode showToastForError(Throwable t, Activity activity) {
        String s = Log.getStackTraceString(t);
        Log.e("ERRORHANDLER", s);
        if (t instanceof HttpException) {
            return handleHttpException((HttpException) t, activity);
        } else if (t instanceof SocketTimeoutException) {
            return handleServerUnresponsive(activity);
        } else if (t instanceof IOException) {
            return handleNoNetwork(activity);
        } else {
            return handleUnknownError(t, activity);
        }
    }

    private static ErrorCode handleHttpException(HttpException t, Activity activity) {
        ErrorCode code = ErrorCode.fromValue(t.code());
        switch (code) {
            case NOT_FOUND :
                ToastUtils.showLongToast(activity, ErrorCode.NOT_FOUND.getMessage());
                break;
            default :
                handleUnknownError(t, activity);
        }

        return code;
    }

    private static ErrorCode handleServerUnresponsive(Activity activity) {
        ToastUtils.showLongToast(activity, ErrorCode.SERVER_UNRESPONSIVE.getMessage());
        return ErrorCode.SERVER_UNRESPONSIVE;
    }

    private static ErrorCode handleNoNetwork(Activity activity) {
        ToastUtils.showLongToast(activity, ErrorCode.NO_NETWORK.getMessage());
        return ErrorCode.NO_NETWORK;
    }

    private static ErrorCode handleUnknownError(Throwable t, Activity activity) {
        Log.e(TAG, "Unknown error: " + t.getMessage() + " |||| " + t.getClass().getName(), t);
        ToastUtils.showLongToast(activity, ErrorCode.UNKNOWN_ERROR.getMessage());

        return ErrorCode.UNKNOWN_ERROR;
    }

}
