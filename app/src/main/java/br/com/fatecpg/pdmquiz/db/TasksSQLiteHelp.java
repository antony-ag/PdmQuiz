package br.com.fatecpg.pdmquiz.db;

/**
 * Created by tony on 13/11/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tony on 07/11/16.
 */

public class TasksSQLiteHelp extends SQLiteOpenHelper {
    public TasksSQLiteHelp(Context context){
        super(context, "tasks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CRIAR A TABELA
        db.execSQL("" +
                "CREATE TABLE TASKS(" +
                "TASK varchar NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TASKS");
        onCreate(db);
    }
}
