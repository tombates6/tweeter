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
import edu.byu.cs.tweeter.client.model.service.observer.EmptySuccessObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedTaskObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus, new GetStatusHandler(getStoryObserver));
        runTask(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedTaskObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize, lastStatus, new GetStatusHandler(getFeedObserver));
        runTask(getFeedTask);
    }

    public void postStatus(AuthToken token, Status newStatus, EmptySuccessObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(token, newStatus, new PostStatusHandler(observer));
        runTask(statusTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStatusHandler extends BackgroundTaskHandler<PagedTaskObserver> {
        public GetStatusHandler(PagedTaskObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(PagedTaskObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(PagedStatusTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(PagedStatusTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    // PostStatusHandler
    private class PostStatusHandler extends BackgroundTaskHandler<EmptySuccessObserver> {
        public PostStatusHandler(EmptySuccessObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(EmptySuccessObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }
}
