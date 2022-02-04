package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements AuthView {

    private Toast loginInToast;
    private TextView errorView;
    private LoginPresenter loginPresenter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginPresenter = new LoginPresenter(this);

        EditText alias = view.findViewById(R.id.loginUsername);
        EditText password = view.findViewById(R.id.loginPassword);
        errorView = view.findViewById(R.id.loginError);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view1 -> {
            // Login and move to MainActivity.
            try {
                loginPresenter.validateLogin(alias.getText().toString(), password.getText().toString());
                errorView.setText(null);
                loginInToast = Toast.makeText(getContext(), "Logging In...", Toast.LENGTH_LONG);
                loginInToast.show();
                loginPresenter.sendLogin(alias.getText().toString(), password.getText().toString());
            } catch (Exception e) {
                errorView.setText(e.getMessage());
            }
        });

        return view;
    }

    @Override
    public void displayErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void login(User loggedInUser) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, loggedInUser);

        loginInToast.cancel();

        Toast.makeText(getContext(), "Hello " + loggedInUser.getName(), Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
