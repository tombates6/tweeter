package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.IAuthObserver;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthPresenter extends Presenter<AuthView>{
    private String action;
    public AuthPresenter(AuthView view, String logTag, String action) {
        super(view, logTag);
        this.action = action;
    }

    public class AuthObserver implements IAuthObserver {
        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            view.login(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            showFailure(action, message);
        }

        @Override
        public void handleException(Exception exception) {
            showError(action, exception.getMessage());

        }
    }
}
