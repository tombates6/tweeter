package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BaseObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStatusObserver
            getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus, new GetStatusHandler(getStoryObserver));
        runTask(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStatusObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize, lastStatus, new GetStatusHandler(getFeedObserver));
        runTask(getFeedTask);
    }

    public void postStatus(AuthToken token, Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(token, newStatus, new PostStatusHandler(observer));
        runTask(statusTask);
    }

    public interface GetStatusObserver extends BaseObserver {
        void handleSuccess(List<Status> statuses, boolean hasMorePages);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStatusHandler extends BackgroundTaskHandler<GetStatusObserver> {
        public GetStatusHandler(GetStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetStatusObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(PagedStatusTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(PagedStatusTask.MORE_PAGES_KEY);
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
