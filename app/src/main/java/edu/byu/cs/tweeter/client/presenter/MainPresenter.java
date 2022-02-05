package edu.byu.cs.tweeter.client.presenter;

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
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter<MainView>{
    private final FollowService followService;
    private final StatusService statusService;
    private final AuthService authService;

    public MainPresenter(MainView view) {
        super(view, "MainPresenter");
        this.followService = new FollowService();
        this.statusService = new StatusService();
        this.authService = new AuthService();
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


    public abstract class EmptySuccessObserver implements edu.byu.cs.tweeter.client.model.service.observer.EmptySuccessObserver {
        private String action;
        public EmptySuccessObserver(String action) { this.action = action; }

        @Override
        public void handleSuccess() {
            success();
        }

        @Override
        public void handleFailure(String message) {
            showFailure(action, message);
        }

        @Override
        public void handleException(Exception exception) {
            showError(action, exception);
        }

        public abstract void success();
    }

    public abstract class GetCountObserver implements edu.byu.cs.tweeter.client.model.service.observer.GetCountObserver {
        private String action;
        public GetCountObserver(String action) { this.action = action; }

        @Override
        public void handleSuccess(int count) {
            success(count);
        }

        @Override
        public void handleFailure(String message) {
            showFailure(action, message);
        }

        @Override
        public void handleException(Exception exception) {
            showError(action, exception);
        }

        public abstract void success(int count);
    }

    public class LogoutObserver extends EmptySuccessObserver {
        public LogoutObserver() { super("logout"); }

        @Override
        public void success() {
            view.logout();
        }
    }

    public class PostStatusObserver extends EmptySuccessObserver {
        public PostStatusObserver() { super("post status"); }

        @Override
        public void success() {
            view.post();
        }
    }

    public class GetFollowingCountObserver extends GetCountObserver {
        public GetFollowingCountObserver() { super("get following count"); }

        @Override
        public void success(int count) {
            view.setFollowingCount(count);
        }
    }

    public class GetFollowersCountObserver extends GetCountObserver {
        public GetFollowersCountObserver() { super("get followers count"); }

        @Override
        public void success(int count) {
            view.setFollowersCount(count);
        }
    }

    public class IsFollowingObserver implements FollowService.IsFollowingObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.isFollower(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            showFailure("determine following relationship", message);
        }

        @Override
        public void handleException(Exception exception) {
            showError("determine following relationship", exception);
        }
    }

    public class UnfollowObserver extends EmptySuccessObserver {
        public UnfollowObserver() { super("unfollow"); }

        @Override
        public void success() {
            updateSelectedUserFollowingAndFollowers();
            view.setFollowing(false);
        }
    }

    public class FollowObserver extends EmptySuccessObserver {
        public FollowObserver() { super("follow"); }

        @Override
        public void success() {
            updateSelectedUserFollowingAndFollowers();
            view.setFollowing(true);
        }
    }
}
