package com.example.inspiredaily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView quoteText;
    private ImageButton btnShare, btnFavorite, btnRefresh;
    private String currentQuote;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quoteText = findViewById(R.id.quote_text);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnRefresh = findViewById(R.id.btn_refresh);

        showRandomQuote();

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, currentQuote);
            startActivity(Intent.createChooser(shareIntent, "Share Quote via"));
        });

        btnFavorite.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("InspireDailyPrefs", Context.MODE_PRIVATE);
            Set<String> favorites = new HashSet<>(prefs.getStringSet("favorites", new HashSet<>()));

            if (!favorites.contains(currentQuote)) {
                favorites.add(currentQuote);
                prefs.edit().putStringSet("favorites", favorites).apply();
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already in favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> showRandomQuote());
    }

    private void showRandomQuote() {
        String url = "https://zenquotes.io/api/random";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject quoteObject = response.getJSONObject(0);
                        String quote = quoteObject.getString("q");
                        String author = quoteObject.getString("a");

                        currentQuote = quote + "\n\n— " + author;
                        quoteText.setText(currentQuote);
                    } catch (JSONException e) {
                        quoteText.setText("Failed to parse quote.");
                        e.printStackTrace();
                    }
                },
                error -> {
                    quoteText.setText("Failed to load quote");
                    error.printStackTrace();
                });

        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
