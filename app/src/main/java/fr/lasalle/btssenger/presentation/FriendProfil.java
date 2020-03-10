package fr.lasalle.btssenger.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.service.UsersService;

public class FriendProfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profil);


        String profileId = getIntent().getStringExtra("PROFIL_ID");
        
    }
}
