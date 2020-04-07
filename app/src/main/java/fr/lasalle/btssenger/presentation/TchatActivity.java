package fr.lasalle.btssenger.presentation;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

        import fr.lasalle.btssenger.R;
        import fr.lasalle.btssenger.service.ChatService;
        import fr.lasalle.btssenger.service.OnCompleteListener;

public class TchatActivity extends AppCompatActivity {
    private ChatService chatService = new ChatService();
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
    }
}
