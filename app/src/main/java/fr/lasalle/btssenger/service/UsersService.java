package fr.lasalle.btssenger.service;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.Account;
import fr.lasalle.btssenger.entity.Message;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;

public class UsersService {
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    public UsersService() {
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void fetchUsers(final FirebaseAdapter adapter) {
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = new User();
                if (dataSnapshot.child("name") != null && dataSnapshot.child("name").getValue() != null) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    user.setName(name);
                }

                if (dataSnapshot.child("status") != null && dataSnapshot.child("status").getValue() != null) {
                    String status = dataSnapshot.child("status").getValue().toString();
                    user.setStatus(status);
                }

                if (dataSnapshot.child("image") != null && dataSnapshot.child("image").getValue() != null) {
                    String image = dataSnapshot.child("image").getValue().toString();
                    user.setImage(Uri.parse(image));
                }
                if (dataSnapshot.getKey() != null) {
                    user.setId(dataSnapshot.getKey());
                }

                if (!user.getId().equalsIgnoreCase(auth.getUid())) {
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

    public void loadUserProfile(String profileId, final OnCompleteListener<User> listener) {
        if (profileId != null) {
            final User user = new User();
            listener.onLoad(true);
            usersRef.child(profileId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listener.onLoad(false);
                    if (dataSnapshot.child("name") != null && dataSnapshot.child("name").getValue() != null) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        user.setName(name);
                    }

                    if (dataSnapshot.child("status") != null && dataSnapshot.child("status").getValue() != null) {
                        String status = dataSnapshot.child("status").getValue().toString();
                        user.setStatus(status);
                    }

                    if (dataSnapshot.child("image") != null && dataSnapshot.child("image").getValue() != null) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        user.setImage(Uri.parse(image));
                    }
                    if (dataSnapshot.getKey() != null) {
                        user.setId(dataSnapshot.getKey());
                    }
                    listener.onSuccess(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onLoad(false);
                    listener.onError();
                }
            });
        } else {
            listener.onError();
        }
    }
}
