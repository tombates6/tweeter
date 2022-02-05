package edu.byu.cs.tweeter.client.model.service.observer;

public interface IGetCountObserver extends BaseObserver {
    void handleSuccess(int count);
}
