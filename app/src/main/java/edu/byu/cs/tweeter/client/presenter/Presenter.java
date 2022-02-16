package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public abstract class Presenter<T extends BaseView> {
    protected final T view;
    protected final String LOG_TAG;

    public Presenter(T view, String logTag) {
        this.view = view;
        this.LOG_TAG = logTag;
    }

    public void showFailure(String action, String message) {
        Log.e(LOG_TAG, message);
        view.displayErrorMessage("Failed to " + action + ": " + message);
    }

    public void showError(String action, String ex) {
        Log.e(LOG_TAG, ex);
        view.displayErrorMessage("Failed to " + action + " because of exception: " + ex);
    }
}
