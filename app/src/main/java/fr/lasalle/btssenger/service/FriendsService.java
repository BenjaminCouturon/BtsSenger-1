package fr.lasalle.btssenger.service;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;

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

        optionsAllFriends = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(friendsRef.child(auth.getUid()), usersRef, new SnapshotParser<User>() {
                    @NonNull
                    @Override
                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
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
}

    public FirebaseRecyclerAdapter<User, UserViewHolder> getFriends(final OnItemSelectedListener listener) {
        return new FirebaseRecyclerAdapter<User, UserViewHolder>(optionsAllFriends) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
                holder.setFullname(user.getFullname());
                holder.setStatus(user.getStatus());
                holder.setAvatar(user.getImage());
            }
        };
    }

