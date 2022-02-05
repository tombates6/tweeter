package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.IAuthObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IEmptySuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthService {
    public void login(String alias, String password, IAuthObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthHandler(loginObserver));
        runTask(loginTask);
    }

    public void logout(AuthToken token, IEmptySuccessObserver observer) {
        LogoutTask logoutTask = new LogoutTask(token, new LogoutHandler(observer));
        runTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, IAuthObserver registerObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthHandler(registerObserver));
        runTask(registerTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class AuthHandler extends BackgroundTaskHandler<IAuthObserver> {
        public AuthHandler(IAuthObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IAuthObserver observer, Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    // LogoutHandler
    private class LogoutHandler extends BackgroundTaskHandler<IEmptySuccessObserver> {
        public LogoutHandler(IEmptySuccessObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IEmptySuccessObserver observer, Bundle data) {
            observer.handleSuccess();
            Cache.getInstance().clearCache();
        }
    }
}
