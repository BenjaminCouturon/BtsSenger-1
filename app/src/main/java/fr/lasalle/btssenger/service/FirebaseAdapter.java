package fr.lasalle.btssenger.service;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected List<T> entities;

    public FirebaseAdapter() {
        super();

        entities = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void addEntity(T entity) {
        entities.add(entity);
        notifyDataSetChanged();
    }
}
