package fr.lasalle.btssenger.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.Account;
import fr.lasalle.btssenger.service.AccountService;
import fr.lasalle.btssenger.service.OnCompleteListener;

public class LoginActivity extends AppCompatActivity {
    private AccountService accountService;

    private ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountService = new AccountService();

        loginProgress = new ProgressDialog(this);
        loginProgress.setTitle("Logging In");
        loginProgress.setMessage("Please wait while we check your credentials.");
        loginProgress.setCanceledOnTouchOutside(false);

        findViewById(R.id.login_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout emailLayout = findViewById(R.id.login_activity_email);
                TextInputLayout passwordLayout = findViewById(R.id.login_activity_password);

                String email = emailLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {


                    accountService.loginWithEmailAndPassword(email, password, new OnCompleteListener<Account>() {
                        @Override
                        public void onSuccess(Account result) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError() {

                            Toast.makeText(LoginActivity.this, "Cannot Sign in. Please check your credentials and try again", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onLoad(boolean loading) {
                            if (loading) {
                                loginProgress.show();
                            } else {
                                loginProgress.hide();
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.login_activity_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (accountService.isAuthenticate()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
