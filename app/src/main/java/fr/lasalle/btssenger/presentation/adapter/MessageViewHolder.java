package fr.lasalle.btssenger.presentation.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.Message;


public class MessageViewHolder extends RecyclerView.ViewHolder{
    private View view;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }
    public void setMessage(String accountId, Message message) {
        if (message.getAuthor().equals(accountId)){
            view.findViewById(R.id.item_message_receipt).setVisibility(view.GONE);
            ((TextView) view.findViewById(R.id.item_message_sender)).setText(message.getMessage());
        }else{
            view.findViewById(R.id.item_message_sender).setVisibility(view.GONE);
            ((TextView) view.findViewById(R.id.item_message_receipt)).setText(message.getMessage());
        }
    }
}
