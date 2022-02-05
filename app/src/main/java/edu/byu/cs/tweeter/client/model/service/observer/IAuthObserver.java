package edu.byu.cs.tweeter.client.model.service.observer;


import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface IAuthObserver extends BaseObserver {
    void handleSuccess(User loggedInUser, AuthToken authToken);
}
