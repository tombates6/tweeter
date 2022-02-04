package edu.byu.cs.tweeter.client.presenter.view;

public interface MainView extends BaseView {
    void logout();
    void setFollowing(boolean following);
    void post();
    void isFollower(boolean isFollower);
    void setFollowingCount(int count);
    void setFollowersCount(int count);
}
