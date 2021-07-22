package com.educacao.salvador.conexao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.educacao.salvador.conexao.adapters.VideoListAdapter;
import com.educacao.salvador.conexao.model.VideoListDataModel;

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

public class PlaylistVideosActivity extends AppCompatActivity {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDVCDZhq5SUJij4gmY_GvQRXzVCZPwuSig";
    private String PLAYLISTS_VIDEOS_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems/?part=snippet&maxResults=99&key="+GOOGLE_YOUTUBE_API_KEY;
    private String playlistId = "";
    private RecyclerView videolistList = null;
    private VideoListAdapter adapter = null;
    private ArrayList<VideoListDataModel> listData = new ArrayList<>();
    private VideoListAdapter.RecycleViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_videos);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            playlistId = extras.getString("id");
            getSupportActionBar().setTitle(extras.getString("titulo"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            videolistList = (RecyclerView) findViewById(R.id.videos_list);
            initList(listData);
            new RequestYouTubeAPI().execute();
        }
    }

    private void initList(ArrayList<VideoListDataModel> listData){
        setOnClickListener();
        videolistList.setLayoutManager(new LinearLayoutManager(PlaylistVideosActivity.this));
        adapter = new VideoListAdapter(PlaylistVideosActivity.this, listData, listener);
        videolistList.setAdapter(adapter);
    }

    private void setOnClickListener() {

        listener = new VideoListAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent videoIntent = new Intent(getApplicationContext(), VideoPlayActivity.class);
                videoIntent.putExtra("id", listData.get(position).getId());
                videoIntent.putExtra("titulo", listData.get(position).getTitulo());
                videoIntent.putExtra("descricao", listData.get(position).getDescricao());
                videoIntent.putExtra("thumbnail", listData.get(position).getThumbnailURL());
                videoIntent.putExtra("publicacao", listData.get(position).getPublishedAt());
                startActivity(videoIntent);
            }
        };
    }

    private class RequestYouTubeAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(PLAYLISTS_VIDEOS_GET_URL+"&playlistId="+playlistId);
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
                    listData = parseVideoListFromResponse(jsonObject);
                    initList(listData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public ArrayList<VideoListDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
            ArrayList<VideoListDataModel>videolistList = new ArrayList<>();
            if (jsonObject.has("items")) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        if (json.has("kind")) {
                            if (json.getString("kind").equals("youtube#playlistItem")) {
                                VideoListDataModel videolistObject = new VideoListDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String id =  jsonSnippet.getJSONObject("resourceId").getString("videoId");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                //String publicacao = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                                videolistObject.setId(id);
                                videolistObject.setTitulo(title);
                                //videolistObject.setPublishedAt(publicacao);
                                videolistObject.setDescricao(description);
                                videolistObject.setThumbnailURL(thumbnail);
                                videolistList.add(videolistObject);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return videolistList;
        }

    }
}
