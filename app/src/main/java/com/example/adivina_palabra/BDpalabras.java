package com.example.adivina_palabra;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class BDpalabras extends SQLiteOpenHelper {
    final Context context;
    final static String NOMBRE_BD="palabras.db3";
    final static int VERSION=1;
    SQLiteDatabase db=null;


    public BDpalabras(@Nullable Context context) {
        super(context, NOMBRE_BD,null, VERSION);
        this.context = context;

    }

    public void abreBD() {
        if (db==null) {
            db = this.getReadableDatabase();
        }
    }

    public  void cierraBD() {
        if (db!=null) {
            db.close();
            db = null;
        }
    }
	
    @Override
    public void onCreate(SQLiteDatabase db) {
        Scanner s;
        try {
            String todasLasSetencias = Utilidades.obtentTextoFicheroCarpetaAssets(context.getAssets().open("palabras5c.sql"));
            s = new Scanner(todasLasSetencias);
            s.useDelimiter(";");
            while (s.hasNext()) {
                String statement = s.next();
                db.execSQL(statement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public String obtenPalabraAleatoria(){
        abreBD();
		String palabraAleatoria="";
        Cursor cursor=db.rawQuery("SELECT palabra FROM tbpalabras",null);
        int count=cursor.getCount();
        Random random=new Random();
        int num_fila = random.nextInt(count);
        cursor.moveToPosition(num_fila);

        palabraAleatoria=cursor.getString(0);

        return palabraAleatoria.toUpperCase(Locale.ROOT);
    }

    public boolean palabraEnDiccionario(String palabraBuscar) {
        abreBD();
		boolean existe=false;
         palabraBuscar = palabraBuscar.toLowerCase(Locale.ROOT);
           Cursor cursor = db.rawQuery("SELECT palabra FROM tbpalabras WHERE palabra ='"+palabraBuscar+"'",null);
           int filas= cursor.getCount();
           if(filas==0){
               existe=false;
           }
           else{
               existe=true;
           }
	   
        return existe;
    }
}
