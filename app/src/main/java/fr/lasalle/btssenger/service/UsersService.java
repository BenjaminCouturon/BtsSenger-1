package fr.lasalle.btssenger.service;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.lasalle.btssenger.entity.User;

public class UsersService {
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    public UsersService() {
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }
    public void recupUsers(final FirebaseAdapter adapter) {
        usersRef .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = new User();
                user.setName(dataSnapshot.child("name").getValue().toString());
                user.setStatus(dataSnapshot.child("status").getValue().toString());
                user.setId(dataSnapshot.getKey());
                user.setImage(Uri.parse(dataSnapshot.child("image").getValue().toString()));
                adapter.addEntity(user);
                if (!user.getId().equalsIgnoreCase(auth.getUid())){
                    adapter.addEntity(user);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
