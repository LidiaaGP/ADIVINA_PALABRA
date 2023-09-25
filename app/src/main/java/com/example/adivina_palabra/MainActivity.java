package com.example.adivina_palabra;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int NUM_PALABRAS= 6;
    public static final int NUM_LETRAS_EN_PALABRA=5;
    private TextView palabras[][] = new TextView[NUM_PALABRAS][NUM_LETRAS_EN_PALABRA];
    BDpalabras BD;
    String palabraAAdivinar="";
    int intento;
    int indiceletraActual;

    TableLayout tableLayout_teclado;
    TextView tv_resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout_teclado = findViewById(R.id.tableLayout_teclado);
        tv_resultado = findViewById(R.id.tv_resultado);
        inicializaArrayPalabras();

        BD=new BDpalabras(MainActivity.this);
        BD.abreBD();


		// Completar. Aquí debemos obtener y asignar a palabraAAdivinar una palabra aleatoria de la base de datos
        palabraAAdivinar= BD.obtenPalabraAleatoria();

		intento = 0;
        indiceletraActual = 0;
        Toast.makeText(getApplicationContext(),palabraAAdivinar,Toast.LENGTH_SHORT).show();
    }


    void inicializaArrayPalabras() {
        palabras[0][0] = findViewById(R.id.textView_0_0);
        palabras[0][1] = findViewById(R.id.textView_0_1);
        palabras[0][2] = findViewById(R.id.textView_0_2);
        palabras[0][3] = findViewById(R.id.textView_0_3);
        palabras[0][4] = findViewById(R.id.textView_0_4);

        palabras[1][0] = findViewById(R.id.textView_1_0);
        palabras[1][1] = findViewById(R.id.textView_1_1);
        palabras[1][2] = findViewById(R.id.textView_1_2);
        palabras[1][3] = findViewById(R.id.textView_1_3);
        palabras[1][4] = findViewById(R.id.textView_1_4);

        palabras[2][0] = findViewById(R.id.textView_2_0);
        palabras[2][1] = findViewById(R.id.textView_2_1);
        palabras[2][2] = findViewById(R.id.textView_2_2);
        palabras[2][3] = findViewById(R.id.textView_2_3);
        palabras[2][4] = findViewById(R.id.textView_2_4);

        palabras[3][0] = findViewById(R.id.textView_3_0);
        palabras[3][1] = findViewById(R.id.textView_3_1);
        palabras[3][2] = findViewById(R.id.textView_3_2);
        palabras[3][3] = findViewById(R.id.textView_3_3);
        palabras[3][4] = findViewById(R.id.textView_3_4);

        palabras[4][0] = findViewById(R.id.textView_4_0);
        palabras[4][1] = findViewById(R.id.textView_4_1);
        palabras[4][2] = findViewById(R.id.textView_4_2);
        palabras[4][3] = findViewById(R.id.textView_4_3);
        palabras[4][4] = findViewById(R.id.textView_4_4);

        palabras[5][0] = findViewById(R.id.textView_5_0);
        palabras[5][1] = findViewById(R.id.textView_5_1);
        palabras[5][2] = findViewById(R.id.textView_5_2);
        palabras[5][3] = findViewById(R.id.textView_5_3);
        palabras[5][4] = findViewById(R.id.textView_5_4);
        
    }


    public void click_letra(View view) {

        if (indiceletraActual<=NUM_LETRAS_EN_PALABRA-1 && palabras[intento][indiceletraActual].getText().length()==0 ) {
            Button b = (Button) view;
            char letra = b.getText().charAt(0);
            palabras[intento][indiceletraActual].setText(letra+"");
            palabras[intento][indiceletraActual].setBackgroundResource(R.drawable.cuadrado_resaltado);
            if (indiceletraActual < NUM_LETRAS_EN_PALABRA-1) indiceletraActual++;
        }

    }

    public void click_enviar(View view) {

        if (indiceletraActual == NUM_LETRAS_EN_PALABRA-1 && palabras[intento][indiceletraActual].getText().length()>0) {
            String palabraUsuario = "";
            for (int i = 0; i < NUM_LETRAS_EN_PALABRA; i++) {
                palabraUsuario = palabraUsuario + palabras[intento][i].getText();
            }

            if (palabraUsuario.equals(palabraAAdivinar)) {
                colorearPalabraIntento(palabraUsuario);
                Toast.makeText(getApplicationContext(),"HAS GANADO!!!!", Toast.LENGTH_SHORT).show();
                tableLayout_teclado.setVisibility(View.GONE);
            }
            else {
				boolean existeEnDiccionario=false;
				// Completar
                existeEnDiccionario=BD.palabraEnDiccionario(palabraUsuario);

                if (!existeEnDiccionario) {
                    Toast.makeText(getApplicationContext(),"La palabra no está en el diccionario", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Ha fallado la palabra", Toast.LENGTH_SHORT).show();
                    colorearPalabraIntento(palabraUsuario);
                    if (intento<NUM_PALABRAS-1) {
                        intento++;
                        indiceletraActual = 0;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Ha perdido la partida", Toast.LENGTH_SHORT).show();
                        tableLayout_teclado.setVisibility(View.GONE);
                        tv_resultado.setText("PALABRA: " + palabraAAdivinar);
                    }
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Debe introducir todas las letras",Toast.LENGTH_SHORT).show();
        }

    }

    public void click_borrar(View view) {
        if (palabras[intento][indiceletraActual].getText().length()==0) {
            if (indiceletraActual>0) indiceletraActual--;
        }
        palabras[intento][indiceletraActual].setText("");
        palabras[intento][indiceletraActual].setBackgroundResource(R.drawable.cuadrado);

    }

    void colorearPalabraIntento(String palabraUsuario) {
        for(int i=0; i<palabraAAdivinar.length(); i++ ) {
            if (palabraAAdivinar.charAt(i) == palabraUsuario.charAt(i)) {
                palabras[intento][i].setBackgroundResource(R.drawable.cuadrado_verde);
            }
            else if (palabraAAdivinar.contains(palabraUsuario.charAt(i)+"")){
                palabras[intento][i].setBackgroundResource(R.drawable.cuadrado_amarillo);
            }
            else {
                palabras[intento][i].setBackgroundResource(R.drawable.cuadrado_gris);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        BD.cierraBD();
        // Completar
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        BD.abreBD();
        // Completar
    }
}