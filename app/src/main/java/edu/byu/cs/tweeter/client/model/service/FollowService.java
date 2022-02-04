package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BaseObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedTaskObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowHandler(getFollowersObserver));
        runTask(getFollowersTask);
    }
    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedTaskObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowHandler(getFollowingObserver));
        runTask(getFollowingTask);
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, GetCountObserver getCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getCountObserver));
        runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, GetCountObserver getCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getCountObserver));
        runTask(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User selectedUser, IsFollowingObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        runTask(isFollowerTask);
    }

    public void follow(AuthToken authToken, User selectedUser, FollowUnfollowObserver observer) {
        FollowTask followTask = new FollowTask(authToken,
                selectedUser, new FollowUnfollowHandler(observer));
        runTask(followTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, FollowUnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                selectedUser, new FollowUnfollowHandler(observer));
        runTask(unfollowTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowTask.
     */
    private class GetFollowHandler extends BackgroundTaskHandler<PagedTaskObserver> {
        public GetFollowHandler(PagedTaskObserver observer) { super(observer); }

        @Override
        protected void handleSuccessMessage(PagedTaskObserver observer, Bundle data) {
            List<User> followers = (List<User>) data.getSerializable(PagedUserTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.handleSuccess(followers, hasMorePages);
        }
    }

    public interface GetCountObserver extends BaseObserver {
        void handleSuccess(int count);
    }

    private class GetCountHandler extends BackgroundTaskHandler<GetCountObserver> {
        public GetCountHandler(GetCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetCountObserver observer, Bundle data) {
            int count = data.getInt(GetCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }
    }

    public interface IsFollowingObserver extends BaseObserver {
        void handleSuccess(boolean isFollower);
    }

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

    private class FollowUnfollowHandler extends BackgroundTaskHandler<FollowUnfollowObserver> {
        public FollowUnfollowHandler(FollowUnfollowObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(FollowUnfollowObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }
}
