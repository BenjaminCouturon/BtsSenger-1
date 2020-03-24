package fr.lasalle.btssenger.presentation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.service.OnCompleteListener;
import fr.lasalle.btssenger.service.UsersService;

public class RequestProfil extends AppCompatActivity {
    private  UsersService usersService = new UsersService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profil);


        String profileId = getIntent().getStringExtra("PROFIL_ID");
        usersService.loadUserProfile(profileId, new OnCompleteListener<User>() {
            @Override
            public void onSuccess(User result) {
                ((TextView)findViewById(R.id.account_activity_fullname)).setText(result.getName());
                Picasso.get().load(result.getImage()).placeholder(R.drawable.ic_user).into((ImageView)findViewById(R.id.account_activity_avatar));
            }


            @Override
            public void onError() {

            }

            @Override
            public void onLoad(boolean loading) {

            }
        });
    }
}
