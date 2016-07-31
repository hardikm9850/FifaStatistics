package com.example.kevin.fifastatistics.managers;

import android.app.Activity;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

public class RetrofitErrorManager {

    private static final String TAG = "Error Manager";

    public enum ErrorCode {
        SERVER_UNRESPONSIVE(-1), NO_NETWORK(-2), UNKNOWN_ERROR(-3), NOT_FOUND(404);

        private int value;

        ErrorCode(int value) {
            this.value = value;
        }

        public static ErrorCode fromValue(int value) {
            for (ErrorCode code : ErrorCode.values()) {
                if (code.getValue() == value) {
                    return code;
                }
            }
            return UNKNOWN_ERROR;
        }

        public int getValue() {
            return value;
        }
    }

    public static ErrorCode showToastForError(Throwable t, Activity activity) {
        if (t instanceof HttpException) {
            return handleHttpException((HttpException) t, activity);
        } else if (t instanceof SocketTimeoutException) {
            return handleServerUnresponsive(activity);
        } else if (t instanceof IOException) {
            return handleNoNetwork(activity);
        } else {
            return handleUnkownError(t, activity);
        }
    }

    private static ErrorCode handleHttpException(HttpException t, Activity activity) {
        ErrorCode code = ErrorCode.fromValue(t.code());
        switch (code) {
            case NOT_FOUND :
                ToastUtils.showLongToast(activity, activity.getString(R.string.not_found));
                break;
            default :
                handleUnkownError(t, activity);
        }

        return code;
    }

    private static ErrorCode handleServerUnresponsive(Activity activity) {
        ToastUtils.showLongToast(activity, activity.getString(R.string.unable_to_contact_server));
        return ErrorCode.SERVER_UNRESPONSIVE;
    }

    private static ErrorCode handleNoNetwork(Activity activity) {
        ToastUtils.showLongToast(activity, activity.getString(R.string.no_network));
        return ErrorCode.NO_NETWORK;
    }

    private static ErrorCode handleUnkownError(Throwable t, Activity activity) {
        Log.e(TAG, "Unknown error: " + t.getMessage() + " |||| " + t.getClass().getName(), t);
        ToastUtils.showLongToast(activity, activity.getString(R.string.unknown_error));

        return ErrorCode.UNKNOWN_ERROR;
    }

}
