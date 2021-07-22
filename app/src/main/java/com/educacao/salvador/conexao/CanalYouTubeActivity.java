package com.educacao.salvador.conexao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.educacao.salvador.conexao.adapters.PlaylistsAdapter;
import com.educacao.salvador.conexao.model.PlaylistDataModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CanalYouTubeActivity extends AppCompatActivity {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDVCDZhq5SUJij4gmY_GvQRXzVCZPwuSig";
    private static String CHANNEL_ID = "UCp4lXgM1CYK6GxmztIT-bOA";
    private static String PLAYLISTS_GET_URL = "https://www.googleapis.com/youtube/v3/playlists/?part=snippet&maxResults=99&channelId="+CHANNEL_ID+"&key="+GOOGLE_YOUTUBE_API_KEY;
    private RecyclerView playlistList = null;
    private PlaylistsAdapter adapter = null;
    private ArrayList<PlaylistDataModel> listData = new ArrayList<>();
    private PlaylistsAdapter.RecycleViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canal_youtube);

        getSupportActionBar().setTitle("Voltar ao início");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playlistList = (RecyclerView) findViewById(R.id.playlists);
        initList(listData);
        new RequestYouTubeAPI().execute();
    }

    private void initList(ArrayList<PlaylistDataModel> listData){
        setOnClickListener();
        playlistList.setLayoutManager(new LinearLayoutManager(CanalYouTubeActivity.this));
        adapter = new PlaylistsAdapter(CanalYouTubeActivity.this, listData, listener);
        playlistList.setAdapter(adapter);
    }

    private void setOnClickListener() {

        listener = new PlaylistsAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), PlaylistVideosActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("titulo", listData.get(position).getTitulo());
                startActivity(intent);
            }
        };
    }

    private class RequestYouTubeAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(PLAYLISTS_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    listData = parsePlayListFromResponse(jsonObject);
                    initList(listData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public ArrayList<PlaylistDataModel> parsePlayListFromResponse(JSONObject jsonObject) {
            ArrayList<PlaylistDataModel> playlistList = new ArrayList<>();
            if (jsonObject.has("items")) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        if (json.has("kind")) {
                            String id = json.getString("id");
                            if (json.getString("kind").equals("youtube#playlist")) {
                                PlaylistDataModel playlistObject = new PlaylistDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                                playlistObject.setId(id);
                                playlistObject.setTitulo(title);
                                playlistObject.setThumbnailURL(thumbnail);
                                playlistList.add(playlistObject);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return playlistList;
        }
    }
}
