package fr.lasalle.btssenger.entity;

import android.net.Uri;

public class Account {
    private String id;
    private String fullname;
    private String status;
    private Uri avatar;

    public Account(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }
}
