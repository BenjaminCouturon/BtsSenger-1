package fr.lasalle.btssenger.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.service.FriendsService;
import fr.lasalle.btssenger.service.OnCompleteListener;
import fr.lasalle.btssenger.service.UsersService;

public class FriendProfil extends AppCompatActivity {
    private  UsersService usersService = new UsersService();
    private  FriendsService friendService = new FriendsService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profil);
        final String profileId = getIntent().getStringExtra("PROFIL_ID");
        findViewById(R.id.tchat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendProfil.this,TchatActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.removefriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendService.removeFriend(profileId, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onLoad(boolean loading) {

                    }
                });

            }
        });



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
