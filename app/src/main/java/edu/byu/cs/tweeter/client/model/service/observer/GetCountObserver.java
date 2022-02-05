package edu.byu.cs.tweeter.client.model.service.observer;

public interface GetCountObserver extends BaseObserver {
    void handleSuccess(int count);
}
