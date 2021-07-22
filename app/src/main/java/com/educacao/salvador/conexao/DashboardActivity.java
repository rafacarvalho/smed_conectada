package com.educacao.salvador.conexao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton arvore;
    private ImageButton musica;
    private ImageButton youtube;
    private ImageButton secretaria;

    public static final String EXTRA_URL = "com.educacao.salvador.conexao.EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        arvore = findViewById(R.id.botao_arvore);
        arvore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirArvore();
            }
        });

        musica = findViewById(R.id.botao_musica);
        musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMusica();
            }
        });

        youtube = findViewById(R.id.botao_youtube);
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirYoutube();
            }
        });

        secretaria = findViewById(R.id.botao_secretaria);
        secretaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSecretaria();
            }
        });
    }

    public void abrirArvore(){
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_URL, "https://app.arvore.com.br/login");
        startActivity(intent);
    }

    public void abrirMusica(){
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_URL, "http://musicanaescola.educacao.salvador.ba.gov.br/");
        startActivity(intent);
    }

    public void abrirYoutube(){
        Intent intent = new Intent(DashboardActivity.this, CanalYouTubeActivity.class);
        startActivity(intent);
    }

    public void abrirSecretaria(){
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_URL, "http://educacao.salvador.ba.gov.br/");
        startActivity(intent);
    }

}
