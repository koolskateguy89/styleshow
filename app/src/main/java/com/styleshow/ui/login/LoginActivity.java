package com.styleshow.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.R;
import com.styleshow.databinding.ActivityLoginBinding;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.ui.MainNavigationActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        EditText emailEditText = binding.email;
        EditText passwordEditText = binding.password;
        Button loginButton = binding.btnLogin;
        //final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null)
                return;

            loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getEmailError() != null) {
                emailEditText.setError(getString(loginFormState.getEmailError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            Timber.d("login result: %s", loginResult);
            if (loginResult == null)
                return;

            if (loginResult.getError() != null) {
                Timber.d("login failed: %s", loginResult.getError());
                showLoginFailed(loginResult.getError());

                // re-enable button if login failed
                loginButton.setEnabled(true);
            }
            if (loginResult.getSuccess() != null) {
                var userProfile = loginResult.getSuccess();
                updateUiWithUser(userProfile);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        Runnable login = () -> {
            Timber.d("logging in...");

            // disable button while logging in
            loginButton.setEnabled(false);

            loginViewModel.login(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
        };

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login.run();
            }
            return false;
        });

        loginButton.setOnClickListener(v -> login.run());
    }

    // TODO?: rename
    private void updateUiWithUser(UserProfile user) {
        Timber.d("login success, user: %s", user.getUsername());

        String welcome = getString(R.string.welcome) + " " + user.getUsername();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
