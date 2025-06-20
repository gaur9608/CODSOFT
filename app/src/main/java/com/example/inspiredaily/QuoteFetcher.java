package com.example.inspiredaily;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuoteFetcher extends AsyncTask<Void, Void, String> {

    public interface QuoteCallback {
        void onQuoteFetched(String quote, String author);
    }

    private final QuoteCallback callback;

    public QuoteFetcher(QuoteCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://zenquotes.io/api/random");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            JSONArray array = new JSONArray(response);
            JSONObject obj = array.getJSONObject(0);
            String quote = obj.getString("q");
            String author = obj.getString("a");
            callback.onQuoteFetched(quote, author);
        } catch (Exception e) {
            callback.onQuoteFetched("Failed to load quote.", "");
        }
    }
}
