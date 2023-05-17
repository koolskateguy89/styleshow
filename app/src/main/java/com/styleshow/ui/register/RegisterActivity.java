package com.styleshow.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.R;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.ActivityRegisterBinding;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.ui.MainNavigationActivity;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * The register screen, allowing the user to register a new account.
 */
@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            openLoginActivity();
        });

        viewModel.getRegisterFormState().observe(this, formState -> {
            if (formState == null)
                return;

            binding.btnRegister.setEnabled(formState.isDataValid());

            if (formState.getEmailError() != null)
                binding.layoutEmail.setError(getString(formState.getEmailError()));
            else
                binding.layoutEmail.setError(null);

            if (formState.getPasswordError() != null)
                binding.layoutPassword.setError(getString(formState.getPasswordError()));
            else
                binding.layoutPassword.setError(null);
        });

        viewModel.getRegisterResult().observe(this, result -> {
            if (result == null)
                return;

            if (result.getError() != null) {
                Timber.i("Error registering");
                Toast.makeText(getApplicationContext(), result.getError(), Toast.LENGTH_SHORT).show();

                // re-enable button if login failed
                binding.btnRegister.setEnabled(true);
            }

            if (result.getSuccess() != null) {
                updateUiWithUser(result.getSuccess());
            }
        });

        TextWatcher afterTextChangedListener = new AfterTextChangedTextWatcher(s -> {
            viewModel.registerDataChanged(binding.etEmail.getText().toString(),
                    binding.etPassword.getText().toString());
        });
        binding.etPassword.addTextChangedListener(afterTextChangedListener);
        binding.etEmail.addTextChangedListener(afterTextChangedListener);

        binding.btnRegister.setOnClickListener(v -> {
            Timber.d("Registering...");

            // Disable while registering
            binding.btnRegister.setEnabled(false);

            viewModel.register(
                    binding.etEmail.getText().toString(),
                    binding.etPassword.getText().toString()
            );
        });
    }

    private void openLoginActivity() {
        var intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(UserProfile user) {
        Timber.d("register success, user: %s", user.getUsername());

        String welcome = getString(R.string.welcome) + " " + user.getUsername();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
