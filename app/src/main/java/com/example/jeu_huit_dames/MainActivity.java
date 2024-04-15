package com.example.jeu_huit_dames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private android.widget.Button button_facile;
    private android.widget.Button button_moyen;
    private android.widget.Button button_difficile;

    public static class Case extends androidx.appcompat.widget.AppCompatImageButton {

        private int colonne;
        private int ligne;

        public Case(Context context, int colonne, int ligne) {
            super(context);

            this.colonne = colonne;
            this.ligne = ligne;

            if ((ligne + colonne) % 2 == 0) {
                this.setBackgroundColor(Color.parseColor("#ebd0b5"));
            } else {
                this.setBackgroundColor(Color.parseColor("#000000"));
            }
        }

        public int getColonne() {
            return colonne;
        }

        public int getLigne() {
            return ligne;
        }

        public void dessiner_reine(int dessiner) {
            if (dessiner == 1) {
                setImageResource(R.drawable.reine);
            } else {
                setImageResource(0);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_facile = (findViewById(R.id.buttonNiveaufacile));
        button_moyen = (findViewById(R.id.buttonNiveauMoyen));
        button_difficile = (findViewById(R.id.buttonNiveaudifficile));
        button_facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        button_moyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        button_difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });

    }

    public void openActivity2() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);


    }
    public void openActivity3() {
        Intent intent = new Intent(this, MoyenActivity.class);
        startActivity(intent);


    }
    public void openActivity4() {
        Intent intent = new Intent(this, DifficileActivity.class);
        startActivity(intent);

    }


}