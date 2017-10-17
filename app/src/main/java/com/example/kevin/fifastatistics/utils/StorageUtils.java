package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class StorageUtils {

    private static final String TAG  = "StorageUtils";

    /**
     * Write to disk cache using the IO scheduler
     * This will not call onError, it will just send false to onNext if it fails
     */
    public static Observable<Boolean> writeToDisk(final Object object, final String fileName) {
        if (object != null && !TextUtils.isEmpty(fileName)) {
            final Context context = FifaApplication.getContext();
            return Observable
                    .create((Observable.OnSubscribe<Boolean>) subscriber ->
                            subscriber.onNext(writeToInternalDiskStorage(fileName, object, context)))
                    .compose(ObservableUtils.applySchedulers());
        } else {
            return Observable.just(Boolean.FALSE);
        }
    }

    /**
     * Write an object to Internal Disk storage
     *
     * @param fileName Filename
     * @param o        Object to write to disk
     * @param context  Application context
     * @return true if succeeded
     */
    public static synchronized boolean writeToInternalDiskStorage(String fileName, Object o, Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not create file for caching", e);
        } catch (IOException e) {
            Log.d(TAG, "Error writing file : " + fileName, e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Could not close file streams: " + fileName, e);
            }
        }
        return false;
    }

    /**
     * Read from internal storage, subscribing on an IO thread
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable<T> readFromDisk(final String fileName, @NonNull final Class<T> clazz) {
        return ReplaySubject.create((Observable.OnSubscribe<T>) subscriber -> {
            if (TextUtils.isEmpty(fileName)) {
                subscriber.onError(new IllegalArgumentException("Filename cannot be empty"));
            }
            Object expObject = readFromInternalDiskStorage(fileName, FifaApplication.getContext());
            if (clazz.isInstance(expObject)) {
                subscriber.onNext((T) expObject);
            } else {
                subscriber.onError(new Exception("Failed to read file from disk cache"));
            }
        }).compose(ObservableUtils.applySchedulers());
    }

    /**
     * Read an Object from Disk Internal disk storage
     *
     * @param fileName Filename to read from disk storage
     * @param context  Application context
     * @return the object
     */
    public static synchronized Object readFromInternalDiskStorage(String fileName, Context context) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object o = null;
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            o = ois.readObject();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, fileName + " file not found");
        } catch (Exception e) {
            Log.d(TAG, "Error reading disk file: " + fileName, e);
            //Delete cache file since it probably got corrupted
            deleteFile(fileName, context);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Could not close file streams: " + fileName, e);
            }
        }
        return o;
    }

    public static synchronized boolean deleteFile(String fileName, Context context) {
        return context.getApplicationContext().deleteFile(fileName);
    }
}
