package ezbus.mit20550588.conductor.data.viewModel;

import static ezbus.mit20550588.conductor.util.Constants.Log;
import static ezbus.mit20550588.conductor.util.PasswordHash.hashPasswordSHA;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ezbus.mit20550588.conductor.R;
import ezbus.mit20550588.conductor.data.model.UserModel;
import ezbus.mit20550588.conductor.data.network.ApiServiceAuthentication;
import ezbus.mit20550588.conductor.data.network.RetrofitClient;
import ezbus.mit20550588.conductor.data.repository.UserRepository;
import ezbus.mit20550588.conductor.util.Validator;


public class AuthViewModel extends ViewModel {
    private final UserRepository userRepository;
    private LiveData<AuthResult> authResultLiveData;
    private MutableLiveData<String> errorMessageLiveData;

    private LiveData<String> verificationCodeLiveData;


    public AuthViewModel() {
        // Create a default constructor
        this.userRepository = new UserRepository(new RetrofitClient().getClient().create(ApiServiceAuthentication.class));
        this.authResultLiveData = userRepository.getAuthResultLiveData();
        this.errorMessageLiveData = userRepository.getErrorMessageLiveData();
        this.verificationCodeLiveData = userRepository.getVerificationCodeLiveData();
    }

    // Existing constructor for dependency injection
    public AuthViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authResultLiveData = userRepository.getAuthResultLiveData();
        this.errorMessageLiveData = userRepository.getErrorMessageLiveData();
        this.verificationCodeLiveData = userRepository.getVerificationCodeLiveData();
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public LiveData<String> getVerificationCodeLiveData() {
        return verificationCodeLiveData;
    }

    public LiveData<AuthResult> getAuthResultLiveData() {
        return authResultLiveData;
    }

    public void loginUser(String email, String password, TextInputEditText emailTextInput, TextInputEditText passwordTextInput, TextInputLayout passwordTextInputLayout) {

        emailTextInput.setError(null);
        passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        errorMessageLiveData.setValue(null);

        // Validate email and password before proceeding
        if (Validator.isValidEmail(email) && !password.isEmpty()) {
            String hashedPassword = hashPasswordSHA(password);

            userRepository.loginUser(email, hashedPassword);


        } else if (email.isEmpty() || password.isEmpty()) {
            // Empty Fields
            errorMessageLiveData.setValue("Please fill in all the required fields");
            if (email.isEmpty()) {
                emailTextInput.setError("Email cannot be empty");
            }
            if (password.isEmpty()) {
                passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                passwordTextInput.setError("Password cannot be empty");
            }

        } else if (!Validator.isValidEmail(email)) {
            // Email is invalid
            emailTextInput.setError("Invalid email format");
            errorMessageLiveData.setValue("Invalid email format");
        }
    }

    public void registerUser(
            String name, String email, String password, String confirmPassword,
            TextInputEditText nameTextInput, TextInputEditText emailTextInput, TextInputEditText passwordTextInput, TextInputEditText confirmPasswordTextInput,
            TextInputLayout passwordTextInputLayout, TextInputLayout confirmPasswordTextInputLayout) {
        // Validate name, email, and password before proceeding

        emailTextInput.setError(null);
        nameTextInput.setError(null);
        passwordTextInput.setError(null);
        confirmPasswordTextInput.setError(null);
        passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        confirmPasswordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        errorMessageLiveData.setValue(null);

        if (Validator.isValidName(name) && Validator.isValidEmail(email) && Validator.isValidPassword(password) && password.equals(confirmPassword)) {
            String hashedPassword = hashPasswordSHA(password);
            userRepository.registerUser(name, email, hashedPassword);
            //  userRepository.registerUser(name, email, password);
        } else {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Empty Fields
                errorMessageLiveData.setValue("Please fill in all the required fields");
            } else {
                if (!Validator.isValidName(name)) {
                    errorMessageLiveData.setValue("Please enter a valid name. Names should contain only letters and cannot be empty.");
                } else if (!Validator.isValidEmail(email)) {
                    errorMessageLiveData.setValue("Please enter a valid email address. The format should be user@example.com.");
                } else if (!Validator.isValidPassword(password)) {
                    errorMessageLiveData.setValue("Please enter a valid password. Passwords must be at least 8 characters long.");
                } else if (!password.equals(confirmPassword)) {
                    errorMessageLiveData.setValue("Passwords do not match. Please make sure the confirmation password matches your chosen password.");
                }
            }

            if (name.isEmpty() || !Validator.isValidName(name)) {
                nameTextInput.setError("Invalid name.");
            }
            if (email.isEmpty() || !Validator.isValidEmail(email)) {
                emailTextInput.setError("Invalid email");
            }
            if (password.isEmpty() || !Validator.isValidPassword(password)) {
                passwordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                passwordTextInput.setError("Invalid password");
            }
            if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                confirmPasswordTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                confirmPasswordTextInput.setError("Passwords do not match");
            }
        }

    }

    public void verifyUser(
            UserModel newUser) {
        userRepository.verifyUser(newUser);
    }

}
