package fr.lasalle.btssenger.service;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fr.lasalle.btssenger.entity.Message;
import fr.lasalle.btssenger.entity.User;

public class ChatService {
    private FirebaseAuth auth;
    private DatabaseReference messagesRef;
    private DatabaseReference usersRef;
    private FirebaseRecyclerOptions<User> optionsAllChats;

    public ChatService() {
        auth = FirebaseAuth.getInstance();
        messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        optionsAllChats = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(messagesRef.child(auth.getUid()), usersRef, new SnapshotParser<User>() {
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

    public void sendMessage(String friendId, String message, final OnCompleteListener<Boolean> listener) {
        DatabaseReference messageRef = messagesRef.child(auth.getUid()).child(friendId)
                .push();

        Map messageBody = new HashMap();
        messageBody.put("author", auth.getUid());
        messageBody.put("message", message);
        messageBody.put("seen", false);
        messageBody.put("type", Message.MessageType.TEXT);
        messageBody.put("time", Calendar.getInstance().getTime().getTime());

        Map messageDetails = new HashMap();
        messageDetails.put(auth.getUid() + "/" + friendId + "/" + messageRef.getKey(), messageBody);
        messageDetails.put(friendId + "/" + auth.getUid() + "/" + messageRef.getKey(), messageBody);

        messagesRef.updateChildren(messageDetails, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    listener.onError();
                } else {
                    listener.onSuccess(true);
                }
            }
        });
    }

    public void fetchMessages(String friendId, final FirebaseAdapter adapter) {
        messagesRef.child(auth.getUid()).child(friendId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message message = new Message();
                        message.setAuthor(dataSnapshot.child("author").getValue().toString());
                        message.setMessage(dataSnapshot.child("message").getValue().toString());
                        message.setSeen(Boolean.parseBoolean(dataSnapshot.child("seen").getValue().toString()));
                        message.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
                        //message.setType(snapshot.child("message").getValue().toString());
                        adapter.addEntity(message);
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

    public void fetchChats(final FirebaseAdapter adapter) {
        optionsAllChats.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
                if (type == ChangeEventType.ADDED) {
                    adapter.addEntity(optionsAllChats.getSnapshots().get(newIndex));
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
}
