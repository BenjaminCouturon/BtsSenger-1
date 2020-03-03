package fr.lasalle.btssenger.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.lasalle.btssenger.entity.Message;

public class ChatService {
    private FirebaseAuth auth;
    private DatabaseReference messagesRef;

    public ChatService(){
        auth = FirebaseAuth.getInstance();
        messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
    }
    public void recupMessages(String friendId, final FirebaseAdapter adapter){
        messagesRef.child(auth.getUid()).child(friendId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = new Message();
                message.setMessage(dataSnapshot.child("message").getValue().toString());
                message.setSeen(Boolean.parseBoolean(dataSnapshot.child("seen").getValue().toString()));
                message.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
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
}
