package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BaseObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStatusObserver
            getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        runTask(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStatusObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        runTask(getFeedTask);
    }

    public void postStatus(AuthToken token, Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(token,
                newStatus, new PostStatusHandler(observer));
        runTask(statusTask);
    }

    public interface GetStatusObserver extends BaseObserver {
        void handleSuccess(List<Status> statuses, boolean hasMorePages);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends BackgroundTaskHandler<GetStatusObserver> {
        public GetStoryHandler(GetStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetStatusObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends BackgroundTaskHandler<GetStatusObserver> {
        public GetFeedHandler(GetStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetStatusObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    public interface PostStatusObserver extends BaseObserver {
        void handleSuccess();
    }

    // PostStatusHandler
    private class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {
        public PostStatusHandler(PostStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(PostStatusObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }
}
