package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
    private static final String LOG_TAG = "StoryFragment";
    private static final int PAGE_SIZE = 10;
    private final View view;
    private final StoryService storyService;
    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public StoryPresenter(View view) {
        this.view = view;
        this.storyService = new StoryService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            storyService.getStory(Cache.getInstance().getCurrUserAuthToken(),
                    user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    public interface View extends BaseView {
        void setLoadingFooter(boolean loading);

        void addStatuses(List<Status> statuses);
    }

    public class GetStoryObserver implements StoryService.GetStoryObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addStatuses(statuses);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get story because of exception: " + exception.getMessage());
        }
    }
}
