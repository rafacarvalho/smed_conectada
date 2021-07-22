package com.educacao.salvador.conexao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDVCDZhq5SUJij4gmY_GvQRXzVCZPwuSig";
    private YouTubePlayerView youtube_player;
    private YouTubePlayer.OnInitializedListener youtube_initialized_listener;
    private Button voltar;
    private String videoId;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        TextView titulo = (TextView) findViewById(R.id.video_title);
        TextView descricao = (TextView) findViewById(R.id.descricao);
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
        youtube_player = (YouTubePlayerView) findViewById(R.id.youtube_player);
        Button voltar = (Button) findViewById(R.id.voltar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            videoId = extras.getString("id");
            titulo.setText(extras.getString("titulo"));
            descricao.setText(extras.getString("descricao"));

            youtube_player.initialize(GOOGLE_YOUTUBE_API_KEY, this);

        }

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.super.onBackPressed();
            }
        });
    }

    private Object getActivity() {
        return super.getCallingActivity();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoId);
        youTubePlayer.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e("SMED", "Falha ao inicializar");
    }
}
