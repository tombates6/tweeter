package edu.byu.cs.tweeter.client.model.service.observer;

public interface IIsFollowingObserver extends BaseObserver {
    void handleSuccess(boolean isFollower);
}
