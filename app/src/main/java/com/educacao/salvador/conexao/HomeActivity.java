package com.educacao.salvador.conexao;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button escola_mais;
    private Button arvore;

    public static final String EXTRA_URL = "com.educacao.salvador.conexao.EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        escola_mais = findViewById(R.id.acessar_escola_mais);
        escola_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEscolaMais();
            }
        });

        arvore = findViewById(R.id.acessar_arvore);
        arvore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirArvore();
            }
        });
    }

    public void abrirEscolaMais(){
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_URL, "https://www.escolamais.com/salvador");
        startActivity(intent);
    }

    public void abrirArvore(){
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_URL, "https://app.arvore.com.br/login");
        startActivity(intent);
    }
}
