package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.AuthService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    private static final String LOG_TAG = "LoginFragment";
    private View view;
    private AuthService loginService;

    public LoginPresenter(View view) {
        this.view = view;
        this.loginService = new AuthService();
    }

    public interface View extends BaseView {
        void login(User loggedInUser);
    }

    public void sendLogin(String alias, String password) {
        loginService.login(alias, password, new LoginObserver());
    }

    public void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver implements AuthService.AuthObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            view.login(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to login because of exception: " + exception.getMessage());

        }
    }
}
