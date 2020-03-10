package fr.lasalle.btssenger.presentation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.Account;

public class AccountActivity extends AppCompatActivity {
    private AccountService accountService;

    private ProgressDialog accountProgress;
    private TextView userName;
    private EditText userNameEdit;
    private TextView userStatus;
    private EditText userStatusEdit;
    private Button userEdit;
    private CircleImageView avatar;
    private Uri avatarUri;
    private ImageView avatarEdit;

    private Account currentAccount;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountService = new AccountService();

        Toolbar toolbar = findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountProgress = new ProgressDialog(this);
        accountProgress.setTitle("Saving changes");
        accountProgress.setMessage("Please wait while we save the changes");
        accountProgress.setCanceledOnTouchOutside(false);

        userName = findViewById(R.id.account_activity_fullname);
        userNameEdit = findViewById(R.id.account_activity_fullname_edit);
        userStatus = findViewById(R.id.account_activity_status);
        userStatusEdit = findViewById(R.id.account_activity_status_edit);
        userEdit = findViewById(R.id.account_activity_edit);
        avatar = findViewById(R.id.account_activity_avatar);
        avatarEdit = findViewById(R.id.account_activity_avatar_change);

        userEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    currentAccount.setFullname(userNameEdit.getText().toString());
                    currentAccount.setStatus(userStatusEdit.getText().toString());
                    currentAccount.setAvatar(avatarUri);
                    accountService.editCurrentAccount(currentAccount, new OnCompleteListener<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            userName.setText(account.getFullname());
                            userStatus.setText(account.getStatus());
                            editMode(false);
                        }

                        @Override
                        public void onLoad(boolean loading) {
                            if (loading) {
                                accountProgress.show();
                            } else {
                                accountProgress.hide();
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(AccountActivity.this, "There was some error in saving changes.", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    editMode(true);
                }
            }
        });

        avatarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(AccountActivity.this);
            }
        });

        accountService.loadCurrentAccount(new OnCompleteListener<Account>() {
            @Override
            public void onSuccess(Account account) {
                currentAccount = account;

                userName.setText(account.getFullname());
                userNameEdit.setText(account.getFullname());
                userStatus.setText(account.getStatus());
                userStatusEdit.setText(account.getStatus());
                Picasso.get().load(account.getAvatar()).placeholder(R.drawable.ic_user).into(avatar);
            }

            @Override
            public void onError() {
                finish();
            }

            @Override
            public void onLoad(boolean loading) {
                if (loading) {
                    accountProgress.show();
                } else {
                    accountProgress.hide();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                avatarUri = result.getUri();
                Picasso.get().load(avatarUri).into(avatar);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void editMode(boolean editable) {
        editMode = editable;

        if (editable) {
            userName.setVisibility(View.GONE);
            userNameEdit.setVisibility(View.VISIBLE);
            userStatus.setVisibility(View.GONE);
            userStatusEdit.setVisibility(View.VISIBLE);
            avatarEdit.setVisibility(View.VISIBLE);
            userEdit.setText(R.string.save);
        } else {
            userName.setVisibility(View.VISIBLE);
            userNameEdit.setVisibility(View.GONE);
            userStatus.setVisibility(View.VISIBLE);
            userStatusEdit.setVisibility(View.GONE);
            avatarEdit.setVisibility(View.GONE);
            userEdit.setText(R.string.edit_account);
        }
    }
}
