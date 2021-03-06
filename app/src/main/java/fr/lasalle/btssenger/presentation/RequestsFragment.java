package fr.lasalle.btssenger.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.lasalle.btssenger.R;
import fr.lasalle.btssenger.entity.User;
import fr.lasalle.btssenger.presentation.adapter.UserViewHolder;
import fr.lasalle.btssenger.service.FirebaseAdapter;
import fr.lasalle.btssenger.service.FriendsService;


public class RequestsFragment extends Fragment {
    private FriendsService friendsService = new FriendsService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView friends = view.findViewById(R.id.friends_fragment_friends);
        friends.setHasFixedSize(true);
        friends.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAdapter<User, UserViewHolder> adapter = new FirebaseAdapter<User, UserViewHolder>() {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_friend, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
                holder.setFullname(entities.get(position).getName());
                holder.setStatus(entities.get(position).getStatus());
                holder.setAvatar(entities.get(position).getImage());
                holder.onClickRequest(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), RequestProfil.class);
                        intent.putExtra("PROFIL_ID", entities.get(position).getId());
                        startActivity(intent);
                        System.out.println("Yo !");
                    }
                });
            }
        };

        friends.setAdapter(adapter);
        friendsService.fetchFriendRequests(adapter);
    }
}
