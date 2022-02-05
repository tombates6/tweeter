package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.AuthService;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;

public class LoginPresenter extends AuthPresenter {
    private AuthService loginService;

    public LoginPresenter(AuthView view) {
        super(view, "LoginPresenter", "login");
        this.loginService = new AuthService();
    }

    public void sendLogin(String alias, String password) {
        loginService.login(alias, password, new AuthObserver());
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
}
