package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BaseObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowersObserver));
        runTask(getFollowersTask);
    }
    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetFollowObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowingObserver));
        runTask(getFollowingTask);
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, GetCountObserver getCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetFollowersCountHandler(getCountObserver));
        runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, GetCountObserver getCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetFollowingCountHandler(getCountObserver));
        runTask(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User selectedUser, IsFollowingObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        runTask(isFollowerTask);
    }

    public void follow(AuthToken authToken, User selectedUser, FollowUnfollowObserver observer) {
        FollowTask followTask = new FollowTask(authToken,
                selectedUser, new FollowHandler(observer));
        runTask(followTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, FollowUnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                selectedUser, new UnfollowHandler(observer));
        runTask(unfollowTask);
    }

    public interface GetFollowObserver extends BaseObserver {
        void handleSuccess(List<User> users, boolean hasMorePages);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends BackgroundTaskHandler<GetFollowObserver> {

        public GetFollowersHandler(GetFollowObserver observer) { super(observer); }

        @Override
        protected void handleSuccessMessage(GetFollowObserver observer, Bundle data) {
            List<User> followers = (List<User>) data.getSerializable(GetFollowersTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.handleSuccess(followers, hasMorePages);
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends BackgroundTaskHandler<GetFollowObserver> {

        public GetFollowingHandler(GetFollowObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetFollowObserver observer, Bundle data) {
            List<User> followees = (List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
            observer.handleSuccess(followees, hasMorePages);
        }
    }

    public interface GetCountObserver extends BaseObserver {
        void handleSuccess(int count);
    }

    // GetFollowersCountHandler
    private class GetFollowersCountHandler extends BackgroundTaskHandler<GetCountObserver> {

        public GetFollowersCountHandler(GetCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }
    }

    // GetFollowingCountHandler
    private class GetFollowingCountHandler extends BackgroundTaskHandler<GetCountObserver> {

        public GetFollowingCountHandler(GetCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }

    }


    public interface IsFollowingObserver extends BaseObserver {
        void handleSuccess(boolean isFollower);
    }

    // IsFollowerHandler
    private class IsFollowerHandler extends BackgroundTaskHandler<IsFollowingObserver> {
        public IsFollowerHandler(IsFollowingObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IsFollowingObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }
    }

    public interface FollowUnfollowObserver extends BaseObserver {
        void handleSuccess();
    }

    // FollowHandler
    private class FollowHandler extends BackgroundTaskHandler<FollowUnfollowObserver> {
        public FollowHandler(FollowUnfollowObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(FollowUnfollowObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }

    // UnfollowHandler
    private class UnfollowHandler extends BackgroundTaskHandler<FollowUnfollowObserver> {
        public UnfollowHandler(FollowUnfollowObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(FollowUnfollowObserver observer, Bundle data) {
            observer.handleSuccess();
        }

    }
}
