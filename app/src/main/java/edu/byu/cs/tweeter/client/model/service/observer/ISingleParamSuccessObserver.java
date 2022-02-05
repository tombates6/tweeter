package edu.byu.cs.tweeter.client.model.service.observer;

public interface ISingleParamSuccessObserver<T> extends BaseObserver {
    void handleSuccess(T resp);
}
