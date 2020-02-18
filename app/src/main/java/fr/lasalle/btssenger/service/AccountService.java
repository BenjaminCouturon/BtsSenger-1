package fr.lasalle.btssenger.service;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import fr.lasalle.btssenger.entity.Account;

public class AccountService {
    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private StorageReference imagesStorage;

    public AccountService() {
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        imagesStorage = FirebaseStorage.getInstance().getReference().child("profile_images");
    }

    public boolean isAuthenticate() {
        return auth.getCurrentUser() != null;
    }

    public void loadCurrentAccount(final OnCompleteListener<Account> listener) {
        final Account account = getCurrentAccount();
        if (account != null) {
            listener.onLoad(true);
            usersRef.child(account.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listener.onLoad(false);
                    if (dataSnapshot.child("name") != null && dataSnapshot.child("name").getValue() != null) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        account.setFullname(name);
                    }

                    if (dataSnapshot.child("status") != null && dataSnapshot.child("status").getValue() != null) {
                        String status = dataSnapshot.child("status").getValue().toString();
                        account.setStatus(status);
                    }

                    if (dataSnapshot.child("image") != null && dataSnapshot.child("image").getValue() != null) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        account.setAvatar(Uri.parse(image));
                    }
                    listener.onSuccess(account);
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

            /*{

    }*/

    public void loginWithEmailAndPassword(String email, String password, final OnCompleteListener<Account> listener) {
        listener.onLoad(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onLoad(false);
                            Account account = new Account(task.getResult().getUser().getUid());
                            listener.onSuccess(account);
                        } else {
                            listener.onLoad(false);
                            listener.onError();
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public void createNewAccount(final String fullname, String email, String password, final OnCompleteListener<Account> listener) {
        listener.onLoad(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onLoad(false);
                            final Account account = getCurrentAccount();
                            DatabaseReference userRef = usersRef.child(account.getId());

                            Map<String, String> userMap = new HashMap<>();
                            userMap.put("name", fullname);
                            userMap.put("status", "Hi there, I'm using BtsSenger App.");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");

                            userRef.setValue(userMap)
                                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            listener.onLoad(false);
                                            listener.onSuccess(account);
                                        }
                                    });
                        } else {
                            listener.onLoad(false);
                            listener.onError();
                        }
                    }
                });
    }

    public void editCurrentAccount(final Account account, final OnCompleteListener<Account> listener) {
        Account currentAccount = getCurrentAccount();
        if (currentAccount != null) {
            final DatabaseReference userRef = usersRef.child(currentAccount.getId());
            final Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", account.getFullname());
            userMap.put("status", account.getStatus());
            userMap.put("image", "default");

            listener.onLoad(true);
            if (account.getAvatar() != null) {
                final StorageReference imageRef = imagesStorage.child(currentAccount.getId() + ".jpg");
                imageRef.putFile(account.getAvatar())
                        .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    imageRef.getDownloadUrl()
                                            .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    userMap.put("image", task.getResult().toString());
                                                    account.setAvatar(task.getResult());
                                                    userRef.setValue(userMap)
                                                            .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    listener.onLoad(false);
                                                                    if (task.isSuccessful()) {
                                                                        listener.onSuccess(account);
                                                                    } else {
                                                                        listener.onError();
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }
                        });
            } else {
                userRef.updateChildren(userMap)
                        .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                listener.onLoad(false);
                                if (task.isSuccessful()) {
                                    listener.onSuccess(account);
                                } else {
                                    listener.onError();
                                }
                            }
                        });
            }

        }
    }

    private Account getCurrentAccount() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            return new Account(currentUser.getUid());
        }
        return null;
    }
}
