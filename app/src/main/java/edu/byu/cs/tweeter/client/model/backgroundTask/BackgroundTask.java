package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {
    private static final String LOG_TAG = "BackgroundTask";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    public static final String USER_KEY = "user";
    /**
     * Message handler that will receive task results.
     */
    protected final Handler messageHandler;

    public BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            runTask();
            sendSuccessMessage();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendSuccessMessage();
        }
    }

    protected FakeData getFakeData() {
        return new FakeData();
    }

    protected abstract void runTask();


    protected void sendSuccessMessage() {
        Bundle msgBundle = getBundle(true);
        loadMessageBundle(msgBundle);

        sendMessage(msgBundle);
    }

    @NonNull
    private Bundle getBundle(boolean b) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, b);
        return msgBundle;
    }

    protected abstract void loadMessageBundle(Bundle msgBundle);

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        messageHandler.sendMessage(msg);
    }

    private void sendFailedMessage(String message) {
        Bundle msgBundle = getBundle(false);
        loadMessageBundle(msgBundle);
        sendMessage(msgBundle);
    }
    protected void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = getBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendMessage(msgBundle);
    }
}
