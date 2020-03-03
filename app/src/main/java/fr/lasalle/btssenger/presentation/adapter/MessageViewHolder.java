package fr.lasalle.btssenger.presentation.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.lasalle.btssenger.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public MessageViewHolder(@NonNull final View itemView){
        super(itemView);

        this.view = itemView;
    }
    public void setMessage(String message){
        ((TextView) view.findViewById(R.id.message_item_message)).setText(message);
    }
}
