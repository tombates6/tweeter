package edu.byu.cs.tweeter.client.presenter.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface AuthView extends BaseView {
    void login(User loggedInUser);
}
