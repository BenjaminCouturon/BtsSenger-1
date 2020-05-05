package fr.lasalle.btssenger.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;
import fr.lasalle.btssenger.service.FirebaseAdapter;
import fr.lasalle.btssenger.service.UsersService;

public class AllUsersActivity extends AppCompatActivity {
    private UsersService usersService = new UsersService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        RecyclerView Users = findViewById(R.id.all_users_activity_users);
        Users.setHasFixedSize(true);
        Users.setLayoutManager(new LinearLayoutManager((this)));


        FirebaseAdapter<User, UserViewHolder> adapter = new FirebaseAdapter<User, UserViewHolder>() {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
                holder.setFullname(entities.get(position).getName());
                holder.setStatus(entities.get(position).getStatus());
                holder.setAvatar(entities.get(position).getImage());
                holder.onClickRequest(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllUsersActivity.this, FriendProfil.class);
                        intent.putExtra("PROFIL_ID", entities.get(position).getId());
                        startActivity(intent);
                        System.out.println("Yo !");
                    }
                });


            }
        };
        Users.setAdapter(adapter);
        usersService.fetchUsers(adapter);

    }
}


