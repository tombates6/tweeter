package edu.byu.cs.tweeter.client.presenter;

public interface BaseObserver {
    void handleFailure(String message);

    void handleException(Exception exception);
}
