package fr.lasalle.btssenger.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.security.auth.callback.CallbackHandler;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.Message;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.MessageViewHolder;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;
import fr.lasalle.btssenger.service.ChatService;
import fr.lasalle.btssenger.service.FirebaseAdapter;


public class ChatsFragment extends Fragment {
    private ChatService chatService = new ChatService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView chats = view.findViewById(R.id.chats_fragment_chats);
        chats.setHasFixedSize(true);
        chats.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseAdapter<User, UserViewHolder> adapter = new FirebaseAdapter<User, UserViewHolder>() {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
                holder.setAvatar(entities.get(position).getImage());
                holder.setFullname(entities.get(position).getName());

            }
        };
        chats.setAdapter(adapter);
        chatService.fetchChats(adapter);
    }
}