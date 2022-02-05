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
import edu.byu.cs.tweeter.client.model.service.observer.IEmptySuccessObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IGetCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IIsFollowingObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IPagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, IPagedTaskObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowHandler(getFollowersObserver));
        runTask(getFollowersTask);
    }
    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, IPagedTaskObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowHandler(getFollowingObserver));
        runTask(getFollowingTask);
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, IGetCountObserver getCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getCountObserver));
        runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, IGetCountObserver getCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getCountObserver));
        runTask(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User selectedUser, IIsFollowingObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        runTask(isFollowerTask);
    }

    public void follow(AuthToken authToken, User selectedUser, IEmptySuccessObserver observer) {
        FollowTask followTask = new FollowTask(authToken,
                selectedUser, new FollowUnfollowHandler(observer));
        runTask(followTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, IEmptySuccessObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                selectedUser, new FollowUnfollowHandler(observer));
        runTask(unfollowTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowTask.
     */
    private class GetFollowHandler extends BackgroundTaskHandler<IPagedTaskObserver> {
        public GetFollowHandler(IPagedTaskObserver observer) { super(observer); }

        @Override
        protected void handleSuccessMessage(IPagedTaskObserver observer, Bundle data) {
            List<User> followers = (List<User>) data.getSerializable(PagedUserTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.handleSuccess(followers, hasMorePages);
        }
    }

    private class GetCountHandler extends BackgroundTaskHandler<IGetCountObserver> {
        public GetCountHandler(IGetCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IGetCountObserver observer, Bundle data) {
            int count = data.getInt(GetCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }
    }

    private class IsFollowerHandler extends BackgroundTaskHandler<IIsFollowingObserver> {
        public IsFollowerHandler(IIsFollowingObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IIsFollowingObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }
    }

    private class FollowUnfollowHandler extends BackgroundTaskHandler<IEmptySuccessObserver> {
        public FollowUnfollowHandler(IEmptySuccessObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IEmptySuccessObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }
}
