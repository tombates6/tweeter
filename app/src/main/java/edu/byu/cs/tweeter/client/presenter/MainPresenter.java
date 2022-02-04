package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AuthService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private static final String LOG_TAG = "MainActivity";
    private View view;
    private FollowService followService;
    private StatusService statusService;
    private AuthService authService;

    public MainPresenter(View view) {

        this.view = view;
        this.followService = new FollowService();
        this.statusService = new StatusService();
        this.authService = new AuthService();
    }


    public interface View extends BaseView {
        void logout();
        void setFollowing(boolean following);
        void post();
        void isFollower(boolean isFollower);
        void setFollowingCount(int count);
        void setFollowersCount(int count);
    }


    public void getIsFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new IsFollowingObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowObserver());
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new UnfollowObserver());
    }

    public void postStatus(String post) throws ParseException {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
        statusService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());
    }

    public void logout() {
        authService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public void updateSelectedUserFollowingAndFollowers() {
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), new GetFollowersCountObserver());
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), new GetFollowingCountObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public class LogoutObserver implements AuthService.LogoutObserver {

        @Override
        public void handleSuccess() {
            view.logout();
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            view.post();
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to post status because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowService.GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFollowingCount(count);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowersCountObserver implements FollowService.GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFollowersCount(count);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    public class IsFollowingObserver implements FollowService.IsFollowingObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.isFollower(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    public class UnfollowObserver implements FollowService.FollowUnfollowObserver {

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers();
            view.setFollowing(false);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());
        }
    }

    public class FollowObserver implements FollowService.FollowUnfollowObserver {

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers();
            view.setFollowing(true);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to follow because of exception: " + exception.getMessage());
        }
    }
}
