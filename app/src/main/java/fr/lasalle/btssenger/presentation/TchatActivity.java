package fr.lasalle.btssenger.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fr.lasalle.btssenger.R;

public class TchatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchat);

        String friendId = getIntent().getStringExtra("friendId");
    }
}
