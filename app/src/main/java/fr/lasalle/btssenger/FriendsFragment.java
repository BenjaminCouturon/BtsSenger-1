package fr.lasalle.btssenger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;
import fr.lasalle.btssenger.service.FirebaseAdapter;
import fr.lasalle.btssenger.service.FriendsService;


public class FriendsFragment extends Fragment {
    private FriendsService friendsService = new FriendsService();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView friends = view.findViewById(R.id.all_friends);
        friends.setHasFixedSize(true);
        friends.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAdapter<User, UserViewHolder> adapter = new FirebaseAdapter<User, UserViewHolder>() {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
                holder.setFullname(entities.get(position).getName());
                holder.setStatus(entities.get(position).getStatus());
                holder.setAvatar(entities.get(position).getImage());


            }
        };
        friends.setAdapter(adapter);
        friendsService.getFriends();
    }
}
