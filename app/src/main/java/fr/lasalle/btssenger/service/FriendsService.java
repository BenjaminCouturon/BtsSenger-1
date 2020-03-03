package fr.lasalle.btssenger.service;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;

public class FriendsService {
    private FirebaseAuth auth;
    private DatabaseReference friendRequestsRef;
    private DatabaseReference friendsRef;
    private DatabaseReference usersRef;
    private FirebaseRecyclerOptions<User> optionsAllFriends;
    private FirebaseRecyclerOptions<User> optionsAllRequests;

    public FriendsService() {
        auth = FirebaseAuth.getInstance();
        friendRequestsRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("=====user: " + dataSnapshot.getKey());
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
        friendsRef.child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("=====my: " + dataSnapshot.getKey());
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

        optionsAllFriends = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(friendsRef.child(auth.getUid()), usersRef, new SnapshotParser<User>() {
                    @NonNull
                    @Override
                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                        System.out.printf("===========friend:" + snapshot.getKey());
                        User user = new User();
                        user.setId(snapshot.getKey());
                        user.setName(snapshot.child("name").getValue().toString());
                        user.setStatus(snapshot.child("status").getValue().toString());
                        user.setImage(Uri.parse(snapshot.child("image").getValue().toString()));
                        return user;
                    }
                })
                .build();
    }

    public FirebaseRecyclerAdapter<User, UserViewHolder> getFriends() {
        return new FirebaseRecyclerAdapter<User, UserViewHolder>(optionsAllFriends) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_friend, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
                holder.setFullname(user.getName());
                holder.setStatus(user.getStatus());
                holder.setAvatar(user.getImage());
            }
        };
    }
}

