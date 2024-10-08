package ezbus.mit20550588.conductor.ui.Login;

import static ezbus.mit20550588.conductor.util.Constants.Log;
import static ezbus.mit20550588.conductor.util.Constants.AUTH_SUCCESS_DIALOG_DURATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ezbus.mit20550588.conductor.R;
import ezbus.mit20550588.conductor.data.model.UserModel;
import ezbus.mit20550588.conductor.data.network.ApiServiceAuthentication;
import ezbus.mit20550588.conductor.data.viewModel.AuthResult;
import ezbus.mit20550588.conductor.data.viewModel.AuthViewModel;
import ezbus.mit20550588.conductor.ui.MainActivity;
import ezbus.mit20550588.conductor.ui.PurchaseTicket.RedeemTicket;
import ezbus.mit20550588.conductor.ui.Settings.PrivacyPolicyActivity;
import ezbus.mit20550588.conductor.ui.Settings.TermConditions;
import ezbus.mit20550588.conductor.ui.virtualTicket;
import ezbus.mit20550588.conductor.util.UserStateManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    private AuthViewModel authViewModel;

    private UserModel newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViewModel();
        initializeUI();
        setupClickListeners();

    }



    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe authentication result
        authViewModel.getVerificationCodeLiveData().observe(this, verificationCode -> {
            if (verificationCode != null) {

                // Hide the loading progress bar
                findViewById(R.id.loadingProgressBar).setVisibility(View.GONE);

                handleSignUpResult(verificationCode);
            }
        });

        // Observe the error message LiveData
        authViewModel.getErrorMessageLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                // Update your UI to display the error message (e.g., show a Toast or update a TextView)
                // Hide the loading progress bar
                findViewById(R.id.loadingProgressBar).setVisibility(View.GONE);

                TextView errorTextView = findViewById(R.id.errorMessageTextView);
                errorTextView.setText(errorMessage);
                //  showToast(errorMessage);
            }
        });
    }

    private void initializeUI() {

        // Hide the loading progress bar
        findViewById(R.id.loadingProgressBar).setVisibility(View.GONE);

        // EZBus.conductor.app main text
        TextView textView0 = findViewById(R.id.main_app_name);
        String html = "<font color=#025a66>EZBus</font> <font color=#0A969F.conductor./font>";
        textView0.setText(Html.fromHtml(html));


        // Condition Text
        TextView conditionsTextView = findViewById(R.id.conditionsText);

        // Create the SpannableString
        SpannableString spannableString = new SpannableString("By clicking Sign up, you agree to our Terms of Service and Privacy Policy");

        // Set a ClickableSpan for the "Terms of Service" part
        ClickableSpan clickableSpanTOS = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        TermConditions.class);
                startActivity(intent);
            }
        };

        // Set the ClickableSpan for the specific range
        spannableString.setSpan(clickableSpanTOS, 38, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Terms of Service" part bold
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 38, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Terms of Service" part a different color
        spannableString.setSpan(new ForegroundColorSpan(getColor(R.color.my_primary)), 38, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        conditionsTextView.setText(spannableString);

        // Make the TextView clickable
        conditionsTextView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());


        // Set a ClickableSpan for the "Privacy Policy" part
        ClickableSpan clickableSpanPP = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        };

        // Set the ClickableSpan for the specific range
        spannableString.setSpan(clickableSpanPP, 59, 73, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Privacy Policy" part bold
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 59, 73, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Privacy Policy" part a different color
        spannableString.setSpan(new ForegroundColorSpan(getColor(R.color.my_primary)), 59, 73, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        conditionsTextView.setText(spannableString);

        // Make the TextView clickable
        conditionsTextView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());




        // Login Text
        TextView loginTextView = findViewById(R.id.loginText);

        // Create the SpannableString
        SpannableString spannableStringLogin = new SpannableString("You already have an account? Login");

        // Set a ClickableSpan for the "Login" part
        ClickableSpan clickableSpanLogin = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(intent);
            }
        };

        // Set the ClickableSpan for the specific range
        spannableStringLogin.setSpan(clickableSpanLogin, 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Terms of Service" part bold
        spannableStringLogin.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make the "Terms of Service" part a different color
        spannableStringLogin.setSpan(new ForegroundColorSpan(getColor(R.color.my_primary)), 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        loginTextView.setText(spannableStringLogin);

        // Make the TextView clickable
        loginTextView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    }

    private void setupClickListeners() {
        // Listening to the Signup Button
        findViewById(R.id.SignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Show the loading progress bar
                findViewById(R.id.loadingProgressBar).setVisibility(View.VISIBLE);

                SignupSubmit();
            }
        });
    }



    private void SignupSubmit() {
        TextInputEditText nameText = findViewById(R.id.editTextName);
        TextInputEditText emailText = findViewById(R.id.editTextEmailAddress);
        TextInputEditText passwordText = findViewById(R.id.editTextPassword);
        TextInputEditText confirmPasswordText = findViewById(R.id.editTextConfirmPassword);

        TextInputLayout passwordTextInputLayout = findViewById(R.id.editTextPasswordInputLayout);
        TextInputLayout confirmPasswordTextInputLayout = findViewById(R.id.editTextConfirmPasswordInputLayout);

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        newUser = new UserModel(name, email, password, false);
        Log("Sign Up", "newUser", newUser.toString());


        // Trigger login operation in ViewModel
        authViewModel.registerUser(name, email, password,confirmPassword, nameText, emailText,passwordText, confirmPasswordText, passwordTextInputLayout, confirmPasswordTextInputLayout);

        Log("SignupSubmit", "SignUp button clicked");

    }


    private void handleSignUpResult(String verificationCode) {
        if (verificationCode != null) {
            Log("handleAuthResult", "Sign up successful");

            // Go to verification
            Intent intent = new Intent(SignUp.this, SignUpEmailVerification.class);
            intent.putExtra("verificationCode", verificationCode);
            intent.putExtra("user", newUser);

            Log("Sign up","handleAuthResult: verificationCode", verificationCode);
            Log("Sign up","handleAuthResult: newUser", newUser.toString());

            startActivity(intent);
            finish();
        } else {
            // Authentication failed, show an error message
            showToast("An issue has occurred. No verification code has been sent.");
            Log("handleAuthResult", "Sign up failed");
        }
    }


    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}