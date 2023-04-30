package com.styleshow.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.styleshow.ui.MainNavigationActivity;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: /data - loginrepository & logindatasource
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

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
            Log.d(TAG, "login result: " + loginResult);
            if (loginResult == null)
                return;

            if (loginResult.getError() != null) {
                Log.d(TAG, "login failed: " + loginResult.getError());
                showLoginFailed(loginResult.getError());

                loginButton.setEnabled(true);
            }
            if (loginResult.getSuccess()) {
                var user = loginViewModel.getCurrentUser();
                Log.d(TAG, "login success, user: " + user);
                updateUiWithUser(user);
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
            Log.d(TAG, "logging in...");

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
    private void updateUiWithUser(FirebaseUser user) {
        String welcome = getString(R.string.welcome) + " " + user.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
