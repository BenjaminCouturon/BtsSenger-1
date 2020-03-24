package fr.lasalle.btssenger.presentation.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.lasalle.btssenger.R;


public class UserViewHolder extends RecyclerView.ViewHolder{
    private View view;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }
    public void setFullname(String fullname) {
        ((TextView) view.findViewById(R.id.item_friend_name)).setText(fullname);
    }

    public void setStatus(String status) {
        ((TextView) view.findViewById(R.id.item_friend_status)).setText(status);
    }

    public void setAvatar(Uri uri) {
        CircleImageView avatar = view.findViewById(R.id.item_friend_avatar);
        Picasso.get().load(uri).placeholder(R.drawable.ic_user).into(avatar);
    }
    public void onClickInvit(View.OnClickListener listener) {
        view.findViewById(R.id.item_friend_invit).setOnClickListener(listener);
    }
    public void onClickRequest(View.OnClickListener listener) {
        view.findViewById(R.id.content).setOnClickListener(listener);
    }
}
