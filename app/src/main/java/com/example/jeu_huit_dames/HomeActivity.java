package com.example.jeu_huit_dames;
import com.example.jeu_huit_dames.MainActivity.Case;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {


    private MainActivity.Case[][] grille;

    private int en_cours;

    private Jeu jeu;

    MediaPlayer mediaPlayer;

    Chronometer simpleChronometer;

    class Jeu {
        static final int taille_grille = 8;
        private static final int nb_reines = 8;
        private int[][] positions_reines;
        private int a = 0;
        boolean menace;

        Jeu() {
            int i, j;
            positions_reines = new int[taille_grille][taille_grille];
            for (i = 0; i < taille_grille; i++) {
                for (j = 0; j < taille_grille; j++) {
                    positions_reines[i][j] = 0;
                }

            }

        }

        int get_A() {
            return a;
        }

        boolean isMenace() {
            return menace;
        }

        void setA(int a) {
            this.a = a;
        }

        int position_actuelle(int lig, int col) {
            return positions_reines[lig][col];
        }

        boolean a_gagner() {
            int cpt = 0, i, j;
            for (i = 0; i < taille_grille; i++) {
                for (j = 0; j < taille_grille; j++) {
                    if (positions_reines[i][j] == 1) {
                        cpt++;
                    }
                }
            }
            if (cpt == nb_reines) {
                return true;

            } else return false;
        }


        void affecter_reine(int lig, int col) {
            // Vérifier si une reine est déjà présente à cette position
            if (positions_reines[lig][col] == 1) {
                positions_reines[lig][col] = 0;
                return;
            } else {
                // Vérifier s'il y a une menace avant de placer la reine
                positions_reines[lig][col] = 1; // On place temporairement la reine pour effectuer la vérification
                if (verification1()) {
                    // Il y a une menace, donc retirer la reine et afficher un message d'alerte
                    positions_reines[lig][col] = 0; // Retirer la reine placée temporairement
                    menace = true;


                    // Créer une boîte de dialogue d'alerte
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogStylealerte);
                    builder.setTitle("Alerte! Vous ne pouvez pas jouer ici, une reine menace une autre.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Action à effectuer lorsque l'utilisateur appuie sur le bouton OK
                                    dialog.dismiss(); // Fermer la boîte de dialogue
                                }
                            });
                    // Afficher la boîte de dialogue
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Toast.makeText(HomeActivity.this, "Il y a une menace ici", Toast.LENGTH_LONG).show();
                    return;
                }
                // Si aucune menace n'est détectée, la reine peut être placée en toute sécurité
                return;
            }
        }

        private boolean isInvalidLine(int row) {
            int count = 0;
            for (int j = 0; j < 8; j++) {
                if (positions_reines[row][j] == 1) {
                    count++;
                }
            }
            return count > 1;
        }

        private boolean isInvalidColumn(int col) {
            int count = 0;
            for (int i = 0; i < 8; i++) {
                if (positions_reines[i][col] == 1) {
                    count++;
                }
            }
            return count > 1;
        }

        private boolean isInvalidMainDiagonal(int row) {
            int count = 0;
            for (int i = 0; i < 8; i++) {
                if (row + i < 8 && positions_reines[row + i][i] == 1) {
                    count++;
                }
            }
            return count > 1;
        }

        private boolean isInvalidSecondaryDiagonal(int row) {
            int count = 0;
            for (int i = 0; i < 8; i++) {
                if (row - i >= 0 && positions_reines[row - i][i] == 1) {
                    count++;
                }
            }
            return count > 1;
        }


        public boolean verification1() {
            int i, j, res1, res2, res3, res4, res5, res6;
            for (i = 0; i < 8; i++) {

                res1 = 0;
                res2 = 0;
                res3 = 0;
                res4 = 0;
                res5 = 0;
                res6 = 0;
                for (j = 0; j < 8; j++) {
                    if (positions_reines[i][j] == 1)
                        res1++;
                    if (positions_reines[j][i] == 1)
                        res2++;
                }
                for (j = 0; j <= i; j++) {
                    if (positions_reines[i - j][j] == 1)
                        res3++;
                    if (positions_reines[7 - i + j][j] == 1)
                        res5++;
                }
                for (j = 0; j < i; j++) {
                    if (positions_reines[8 - i + j][8 - j - 1] == 1)
                        res4++;
                    if (positions_reines[j][8 - i + j] == 1)
                        res6++;
                }
                if (res1 > 1 || res2 > 1 || res3 > 1 || res4 > 1 || res5 > 1 || res6 > 1)
                    return true;
            }
            return false;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mediaPlayer = MediaPlayer.create(this, R.raw.la_musique);
        jeu = new Jeu();
        en_cours = 1;

        creer_grille();


        simpleChronometer = findViewById(R.id.simpleChronometer);
        simpleChronometer.start();
        Button restart = (Button) findViewById(R.id.resetChronometer);
        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                raffrichir_jeu();
                simpleChronometer.setBase(SystemClock.elapsedRealtime());

            }
        });


    }

    public void event_raffrichir(View view) {
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        raffrichir_jeu();
    }

    public void event_quitter(View view) {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

    public void interface_agagner() {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        dialogue.setTitle(R.string.gagner);
        dialogue.setPositiveButton(R.string.Rejouer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                simpleChronometer.start();
                raffrichir_jeu();
            }
        });
        dialogue.setNegativeButton(R.string.Fermer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                dialogInterface.dismiss();

            }
        });
        AlertDialog dialog = dialogue.create();
        dialog.show();


    }


    private void creer_grille() {
        int i, j;

        Display affichage = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        affichage.getSize(size);


        int largeur = size.x;
        int zone_clice = largeur / Jeu.taille_grille;

        GridLayout grillelayout = (GridLayout) findViewById(R.id.grille_grid_layout);

        grille = new MainActivity.Case[Jeu.taille_grille][Jeu.taille_grille];

        for (i = 0; i < Jeu.taille_grille; i++) {
            for (j = 0; j < Jeu.taille_grille; j++) {
                MainActivity.Case case1 = new Case(this, j, i);
                case1.setOnClickListener(new event_clic_case());

                GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams();
                cellParams.width = zone_clice;
                cellParams.height = zone_clice;

                grillelayout.addView(case1, cellParams);

                grille[j][i] = case1;
            }
        }
    }


    private void dessiner_grille() {
        int i, j;
        for (i = 0; i < Jeu.taille_grille; i++) {
            for (j = 0; j < Jeu.taille_grille; j++) {
                grille[j][i].dessiner_reine(jeu.position_actuelle(j, i));
            }
        }
    }
    // Méthodes pour vérifier si une ligne, colonne ou diagonale est menacée

    private void interface_affecter_reine(int lig, int col) {
        jeu.affecter_reine(lig, col);
        dessiner_grille();

        if (jeu.a_gagner()) {
            interface_agagner();
            simpleChronometer.stop();
            en_cours = 0;

        }
    }

    private void raffrichir_jeu() {
        jeu = new Jeu();
        en_cours = 1;
        dessiner_grille();


    }

    private class event_clic_case implements ImageButton.OnClickListener {
        @Override
        public void onClick(View view) {
            if (en_cours == 1) {
                Case case1 = (Case) view;
                interface_affecter_reine(case1.getColonne(), case1.getLigne());
            }
        }
    }
}