package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public abstract class Presenter<T extends BaseView> {
    protected final T view;
    protected final String LOG_TAG;
    protected final String subject;

    public Presenter(T view, String logTag, String subject) {
        this.view = view;
        this.LOG_TAG = logTag;
        this.subject = subject;
    }
}
