package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.client.presenter.view.UserView;
import edu.byu.cs.tweeter.model.domain.User;

public class UserPresenter {

    private User user;
    private final UserService userService;
    private final UserView view;

    public UserPresenter(UserView view) {
        this.userService = new UserService();
        this.view = view;
    }

    public void getUserProfile(String userAlias) {
        userService.getUserProfile(userAlias, new GetUserObserver());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class GetUserObserver implements UserService.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            view.switchUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
