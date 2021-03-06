package com.example.transportadora;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManipulaDB extends SQLiteOpenHelper {

    private static final String NOME_BD = "LOJISTAS.db"; //Database Name - Nome do Banco de Dados
    private static final int VERSAO_BD = 1;
    private static final String users = "create table users(usuario TEXT primary key, senha TEXT, cnpj TEXT)";
    private static final String pjdata = "create table pjdata(CNPJ TEXT primary key, nome TEXT, sobrenome TEXT, email TEXT, razao TEXT, volume TEXT, regiao TEXT, categoria TEXT)";
 //   private static String pfdata = "create table pfdata(CPF TEXT primary key, nome TEXT, sobrenome TEXT, email TEXT)";

    public ManipulaDB(Context ctx){

        super(ctx, NOME_BD,null,VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL(users);
        bd.execSQL(pjdata);
    //    bd.execSQL(pfdata);
    /* Create of 3 tables, Users (User as PK), Data of Enterprises (CNPJ as PK) and Data of People (CPF as PK)
    Criação de 3 tabelas, Usuários (User como PK), Dados de empresa (CNPJ como PK) e Dados de Pessoa Física (CPF como PK)
     */
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int j) {
        bd.execSQL("drop table if exists users");
        bd.execSQL("drop table if exists pjdata");
  //      bd.execSQL("drop table if exists pfdata");
    }

    //ENTERPRISES REGISTER - CADASTRO DE EMPRESAS
    public Boolean inserirPJ(String nome, String sobrenome, String email, String CNPJ, String razao, String volume, String regiao, String categoria) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("CNPJ", CNPJ);
        content.put("nome", nome);
        content.put("sobrenome", sobrenome);
        content.put("email", email);
        content.put("razao", razao);
        content.put("volume", volume);
        content.put("regiao", regiao);
        content.put("categoria", categoria);

        long resultado = bd.insert("pjdata", null, content);
        if (resultado == -1){
            return false;
        }else
            return  true;
    }

 /* CPF IS A NATIONAL REGISTER FOR PEOPLE - CADASTRO DE PESSOA FÍSICA
    public Boolean inserirPF(String CPF, String nome, String sobrenome, String email) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("CPF", CPF);
        content.put("nome", nome);
        content.put("sobrenome", sobrenome);
        content.put("email", email);

        long resultado = bd.insert("pfdata", null, content);
        if (resultado == -1){
            return false
        }else
            return  true;
    }
    */

    public Boolean inserirDados (String usuario, String senha, String CNPJ)  {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", usuario);
        contentValues.put("senha", senha);
        contentValues.put("CNPJ", CNPJ);
        long resultado = bd.insert("users", null, contentValues);
        if (resultado == -1) {
            return false;
        } else
            return true;
    }

    public Boolean atualizaPJ (String CNPJ, String volume, String regiao, String categoria) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("volume", volume);
        cv.put("regiao", regiao);
        cv.put("categoria", categoria);
        bd.update("pjdata", cv, "CNPJ =?", new String[] {CNPJ});
        return true;
    }

    // CHECK IF CNPJ IS ALREADY ON TABLE - CHECA SE CNPJ JÁ EXISTE
    public Boolean isDataPJ(String CNPJ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from pjdata where CNPJ = ?", new String [] {CNPJ});
        if (cursor.getCount()>0){
            return true;
        }else
            return false;
    }

    /* CHECK IF CPF IS ALREADY ON TABLE - CHECA SE CPF JÁ EXISTE
    public Boolean isDataPF(String CPF) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from pfdata where CPF = ?", new String [] {CPF});
        if (cursor.getCount()>0){
            return true;
        }else
            return false;
    }*/

    public Boolean isUser (String usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where usuario = ?", new String[]{usuario});
        if (cursor.getCount() > 0) {
            return true;
        } else
            return  false;
    }

    public Boolean isUserPass(String usuario, String senha){
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery("Select * from users where usuario = ? and senha = ?", new String []{usuario, senha} );
       if (cursor.getCount()>0){
           return true;
       } else
           return false;
    }

    public Boolean updatePass (String usuario, String senha) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("senha", senha);
        bd.update("users", cv, "usuario = ?", new String[]{usuario});
        return true;
    }

    public String getCNPJ(String usuario) {
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select CNPJ from users where usuario = ?", new String[] {usuario});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("cnpj");
                    String CNPJ = cursor.getString(id);
                    return CNPJ;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getCNPJ(usuario);
        return retorna;
    }

    public String getNome(String CNPJ){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select nome from pjdata where CNPJ = ?", new String[] {CNPJ});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("nome");
                    String Nome = cursor.getString(id);
                    return Nome;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getNome(CNPJ);
        return retorna;
    }

    public String getPass(String usuario){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select senha from users where usuario = ?", new String[] {usuario});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("senha");
                    String Senha = cursor.getString(id);
                    return Senha;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getPass(usuario);
        return retorna;
    }

    public String getEmail (String CNPJ){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select email from pjdata where CNPJ = ?", new String[] {CNPJ});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("email");
                    String Email = cursor.getString(id);
                    return Email;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getEmail(CNPJ);
        return retorna;
    }

    public String getRazao (String CNPJ) {
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select razao from pjdata where CNPJ = ?", new String[] {CNPJ});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("razao");
                    String Razao = cursor.getString(id);
                    return Razao;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getRazao(CNPJ);
        return retorna;
    }

    public String getRegiao (String CNPJ){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select regiao from pjdata where CNPJ = ?", new String[] {CNPJ});
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getColumnIndex("regiao");
                    String Regiao = cursor.getString(id);
                    return Regiao;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String retorna = getRegiao(CNPJ);
        return retorna;
    }

    public boolean updateRegiao (String CNPJ, String regiao) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("regiao", regiao);
        bd.update("pjdata", cv, "CNPJ = ?", new String[]{CNPJ});
        return true;
    }

}


