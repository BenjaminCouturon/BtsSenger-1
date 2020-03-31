package fr.lasalle.btssenger.service;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

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

        optionsAllRequests = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(friendRequestsRef.child(auth.getUid()), usersRef, new SnapshotParser<User>() {
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

    public void removeFriend(final String friendId, final OnCompleteListener<Boolean> listener) {
        friendsRef.child(auth.getUid()).child(friendId).removeValue().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onError();
                }
            }
        });
    }


    public void inviteFriend(final String friendId, final OnCompleteListener<Boolean> listener) {
        friendRequestsRef.child(auth.getUid()).child(friendId)
                .child("requestType").setValue("sent")
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequestsRef.child(friendId).child(auth.getUid())
                                    .child("requestType").setValue("received")
                                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onSuccess(true);
                                            } else {
                                                listener.onError();
                                            }
                                        }
                                    });
                        } else {
                            listener.onError();
                        }
                    }
                });
    }

    public void cancelFriendInvitation(final String friendId, final OnCompleteListener<Boolean> listener) {
        friendRequestsRef.child(auth.getUid()).child(friendId).removeValue()
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequestsRef.child(friendId).child(auth.getUid()).removeValue()
                                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onSuccess(true);
                                            } else {
                                                listener.onError();
                                            }
                                        }
                                    });
                        } else {
                            listener.onError();
                        }
                    }
                });
    }

    public void acceptInvitation(final String friendId, final OnCompleteListener<Boolean> listener) {
        final long currentDate = Calendar.getInstance().getTime().getTime();
        friendsRef.child(auth.getUid()).child(friendId).setValue(currentDate)
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendsRef.child(friendId).child(auth.getUid()).setValue(currentDate)
                                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                cancelFriendInvitation(friendId, listener);
                                            } else {
                                                listener.onError();
                                            }
                                        }
                                    });
                        } else {
                            listener.onError();
                        }
                    }
                });
    }


    public void fetchUsers(final FirebaseAdapter adapter) {
        optionsAllFriends.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
                if (type == ChangeEventType.ADDED) {
                    adapter.addEntity(optionsAllFriends.getSnapshots().get(newIndex));
                }
            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetchFriendRequests(final FirebaseAdapter adapter) {
        optionsAllRequests.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
                if (type == ChangeEventType.ADDED) {
                    adapter.addEntity(optionsAllRequests.getSnapshots().get(newIndex));
                }
            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFriendRequestType(final String friendId, final OnCompleteListener<RequestType> listener) {
        friendRequestsRef.child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(friendId)) {
                            String requestType = dataSnapshot.child(friendId).child("requestType").getValue().toString();
                            if (requestType.equalsIgnoreCase("sent")) {
                                listener.onSuccess(RequestType.INVITE_SENT);
                            } else if (requestType.equalsIgnoreCase("received")) {
                                listener.onSuccess(RequestType.INVITE_RECEIVED);
                            }
                        } else {
                            friendsRef.child(auth.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(friendId)) {
                                                listener.onSuccess(RequestType.FRIEND);
                                            } else {
                                                listener.onSuccess(RequestType.NOT_FRIEND);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            listener.onError();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError();
                    }
                });
    }
}
