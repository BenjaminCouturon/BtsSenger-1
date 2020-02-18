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

public class RegisterActivity extends AppCompatActivity {
    private AccountService accountService;

    private ProgressDialog registerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accountService = new AccountService();

        registerProgress = new ProgressDialog(this);
        registerProgress.setTitle("Registering User");
        registerProgress.setMessage("Please wait while we create your account !");
        registerProgress.setCanceledOnTouchOutside(false);

        findViewById(R.id.register_activity_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout emailLayout = findViewById(R.id.register_activity_email);
                TextInputLayout passwordLayout = findViewById(R.id.register_activity_password);
                TextInputLayout fullnameLayout = findViewById(R.id.register_activity_fullname);

                String email = emailLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();
                String fullname = fullnameLayout.getEditText().getText().toString();
                if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    accountService.createNewAccount(fullname, email, password, new OnCompleteListener<Account>() {
                        @Override
                        public void onSuccess(Account result) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(RegisterActivity.this, "Cannot Sign in. Please check your credentials and try again", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onLoad(boolean loading) {
                            if (loading) {
                                registerProgress.show();
                            } else {
                                registerProgress.hide();
                            }
                        }
                    });
                }
            }
        });
    }
}
