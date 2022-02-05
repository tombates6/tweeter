package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils.runTask;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ISingleParamSuccessObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public void getUserProfile(String userAlias, ISingleParamSuccessObserver<User> observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        runTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends BackgroundTaskHandler<ISingleParamSuccessObserver<User>> {
        public GetUserHandler(ISingleParamSuccessObserver<User> observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(ISingleParamSuccessObserver<User> observer, Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            observer.handleSuccess(user);
        }
    }
}
