package fr.lasalle.btssenger.presentation;

        import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.lasalle.btssenger.R;
        import fr.lasalle.btssenger.entity.Account;
        import fr.lasalle.btssenger.entity.Message;
import fr.lasalle.btssenger.presentation.adapter.MessageViewHolder;
        import fr.lasalle.btssenger.service.AccountService;
        import fr.lasalle.btssenger.service.ChatService;
import fr.lasalle.btssenger.service.FirebaseAdapter;
import fr.lasalle.btssenger.service.OnCompleteListener;

public class TchatActivity extends AppCompatActivity {
    private ChatService chatService = new ChatService();
    private AccountService accountService = new AccountService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchat);

        final String friendId = getIntent().getStringExtra("friendId");

        final TextView message = (TextView)findViewById(R.id.Name_message);
        findViewById(R.id.Send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatService.sendMessage(friendId, message.getText().toString(), new OnCompleteListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onLoad(boolean loading) {

                    }
                });
            }
        });

        RecyclerView messages = findViewById(R.id.List_message);
        messages.setHasFixedSize(true);
        messages.setLayoutManager(new LinearLayoutManager((this)));
        FirebaseAdapter<Message, MessageViewHolder> adapter = new FirebaseAdapter<Message, MessageViewHolder>() {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new MessageViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
                holder.setMessage(accountService.getCurrentAccountId(), entities.get(position));

            }
        };
        messages.setAdapter(adapter);
        chatService.fetchMessages(friendId, adapter);
    }
}
