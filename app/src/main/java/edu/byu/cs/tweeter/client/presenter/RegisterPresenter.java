package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.AuthService;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;

public class RegisterPresenter extends AuthPresenter {
    private final AuthService registerService;

    public RegisterPresenter(AuthView view) {
        super(view, "RegisterPresenter", "register");
        this.registerService = new AuthService();
    }

    public void register(String firstName, String lastName, String alias, String password, BitmapDrawable imageToUpload) {

        // Convert image to byte array.
        Bitmap image = imageToUpload.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        registerService.register(firstName, lastName, alias, password, imageBytesBase64, new AuthObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password, BitmapDrawable imageToUpload
    ) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
}
