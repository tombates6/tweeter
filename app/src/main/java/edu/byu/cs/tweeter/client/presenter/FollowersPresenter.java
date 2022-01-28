package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private static final int PAGE_SIZE = 10;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private final FollowersPresenter.View view;
    private final FollowService followersService;

    public FollowersPresenter(FollowersPresenter.View view) {
        this.followersService = new FollowService();
        this.view = view;
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
            followersService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersPresenter.GetFollowersObserver());
        }
    }

    public void getIsFollower() {
        followersService.isFollower();
    }

    public interface View extends BaseView {
        void setLoadingFooter(boolean loading);

        void addFollowers(List<User> followees);
    }

    public class GetFollowersObserver implements FollowService.GetFollowObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowers(followers);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }
}
