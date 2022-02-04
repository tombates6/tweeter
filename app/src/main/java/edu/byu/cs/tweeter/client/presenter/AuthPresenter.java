package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthPresenter extends Presenter<AuthView>{
    public AuthPresenter(AuthView view, String logTag, String subject) {
        super(view, logTag, subject);
    }

    public class PresenterAuthObserver implements AuthObserver {
        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            view.login(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            view.displayErrorMessage("Failed to " + subject + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            view.displayErrorMessage("Failed to " + subject + " because of exception: " + exception.getMessage());

        }
    }
}
