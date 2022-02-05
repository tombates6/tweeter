package edu.byu.cs.tweeter.client.model.service.observer;


import edu.byu.cs.tweeter.model.domain.User;

public interface IGetUserObserver extends BaseObserver {
    void handleSuccess(User user);
}