package edu.byu.cs.tweeter.client.presenter.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserView extends BaseView {
    void switchUser(User user);
}
