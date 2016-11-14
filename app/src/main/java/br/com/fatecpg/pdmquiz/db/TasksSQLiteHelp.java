package br.com.fatecpg.pdmquiz.db;

/**
 * Created by tony on 13/11/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import br.com.fatecpg.pdmquiz.BdQuestion;

/**
 * Created by tony on 07/11/16.
 */

public class TasksSQLiteHelp extends SQLiteOpenHelper {

    public TasksSQLiteHelp(Context context){
        super(context, "quiz.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CRIAR A TABELA
        db.execSQL("" +
                "CREATE TABLE QUESTIONS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "QUESTION TEXT, " +
                "ANSWER TEXT, " +
                "OPT1 TEXT, " +
                "OPT2 TEXT, " +
                "OPT3 TEXT" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS QUESTIONS");
        onCreate(db);
    }




}
