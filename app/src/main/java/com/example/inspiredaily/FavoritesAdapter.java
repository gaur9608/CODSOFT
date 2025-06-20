package com.example.inspiredaily;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<String> favorites;
    private Context context;

    public FavoritesAdapter(Context context, List<String> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quoteText;

        public ViewHolder(View itemView) {
            super(itemView);
            quoteText = itemView.findViewById(R.id.quote_item_text);
        }
    }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_quote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int position) {
        String quote = favorites.get(position);
        holder.quoteText.setText(quote);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < favorites.size()) {
            String quoteToRemove = favorites.get(position);
            favorites.remove(position);
            notifyItemRemoved(position);


            SharedPreferences prefs = context.getSharedPreferences("InspireDailyPrefs", Context.MODE_PRIVATE);
            Set<String> favoritesSet = new HashSet<>(prefs.getStringSet("favorites", new HashSet<>()));
            favoritesSet.remove(quoteToRemove);
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            prefs.edit().putStringSet("favorites", favoritesSet).apply();


        }
    }


    public List<String> getFavorites() {
        return favorites;
    }
}
