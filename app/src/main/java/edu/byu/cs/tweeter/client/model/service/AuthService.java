package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BaseObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthService {
    public void login(String alias, String password, AuthObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(loginObserver));
        runTask(loginTask);
    }

    public void logout(AuthToken token, LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(token, new LogoutHandler(observer));
        runTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthObserver registerObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(registerObserver));
        runTask(registerTask);
    }

    public interface AuthObserver extends BaseObserver {
        void handleSuccess(User loggedInUser, AuthToken authToken);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends BackgroundTaskHandler<AuthObserver> {
        public LoginHandler(AuthObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(AuthObserver observer, Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    // RegisterHandler
    private class RegisterHandler extends BackgroundTaskHandler<AuthObserver> {
        public RegisterHandler(AuthObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(AuthObserver observer, Bundle data) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            observer.handleSuccess(registeredUser, authToken);
        }
    }

    public interface LogoutObserver extends BaseObserver {
        void handleSuccess();
    }

    // LogoutHandler
    private class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {
        public LogoutHandler(LogoutObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(LogoutObserver observer, Bundle data) {
            observer.handleSuccess();
            Cache.getInstance().clearCache();
        }
    }
}
