package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ISingleParamSuccessObserver;
import edu.byu.cs.tweeter.client.presenter.view.UserView;
import edu.byu.cs.tweeter.model.domain.User;

public class UserPresenter extends Presenter<UserView> {
    private User user;
    private final UserService userService;

    public UserPresenter(UserView view) {
        super(view, "UserPresenter");
        this.userService = new UserService();
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

    public class GetUserObserver implements ISingleParamSuccessObserver<User> {
        @Override
        public void handleSuccess(User user) {
            view.switchUser(user);
        }

        @Override
        public void handleFailure(String message) {
            showFailure("get user's profile", message);
        }

        @Override
        public void handleException(Exception exception) {
            showError("get user's profile", exception.getMessage());
        }
    }
}
