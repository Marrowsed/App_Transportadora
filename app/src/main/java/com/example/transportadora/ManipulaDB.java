package com.example.transportadora;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManipulaDB extends SQLiteOpenHelper {

    private static final String NOME_BD = "EMBARCADORES.db"; //Database Name - Nome do Banco de Dados
    private static final int VERSAO_BD = 1;

    public ManipulaDB(Context ctx){

        super(ctx, NOME_BD,null,VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table users(usuario TEXT primary key, senha TEXT)");
        //Created a Table named users with usuarios (PK) and senha
        //Criou uma tabela com usuarios (PK) e senha
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int j) {
        bd.execSQL("drop table if exists users");
    }

    public Boolean inserirDados (String usuario, String senha)  {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", usuario);
        contentValues.put("senha", senha);
        long resultado = bd.insert("users", null, contentValues);
        if (resultado == -1) {
            return false;
        } else
            return true;
    }

    public Boolean isUser (String usuario, String senha){
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery("Select * from users where usuario = ? and senha = ?", new String []{usuario, senha} );
       if (cursor.getCount()>0){
           return true;
       } else
           return false;
    }

}
