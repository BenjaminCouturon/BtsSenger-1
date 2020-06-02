package fr.lasalle.btssenger.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.service.FriendsService;
import fr.lasalle.btssenger.service.OnCompleteListener;
import fr.lasalle.btssenger.service.RequestType;
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
                intent.putExtra("friendId", profileId);
                startActivity(intent);
            }
        });


        findViewById(R.id.removefriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendService.removeFriend(profileId, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onError() {
                        setResult(2);
                        finish();
                    }

                    @Override
                    public void onLoad(boolean loading) {

                    }
                });


            }
        });

        findViewById(R.id.invitfriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendService.inviteFriend(profileId, new OnCompleteListener<Boolean>() {
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

        friendService.getFriendRequestType(profileId, new OnCompleteListener<RequestType>() {
            @Override
            public void onSuccess(RequestType result) {
                switch (result) {
                    case FRIEND:
                        findViewById(R.id.invitfriend).setVisibility(View.GONE);
                        break;

                    case NOT_FRIEND:
                        findViewById(R.id.tchat).setVisibility(View.GONE);
                        findViewById(R.id.removefriend).setVisibility(View.GONE);
                        break;

                    case INVITE_SENT:
                    case INVITE_RECEIVED:
                        findViewById(R.id.invitfriend).setVisibility(View.GONE);
                        findViewById(R.id.tchat).setVisibility(View.GONE);
                        findViewById(R.id.removefriend).setVisibility(View.GONE);
                        break;
                }
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
