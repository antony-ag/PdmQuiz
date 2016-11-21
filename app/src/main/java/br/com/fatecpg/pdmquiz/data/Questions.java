package br.com.fatecpg.pdmquiz.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class Questions{
    public static ArrayList<Question> questions;
    public static ArrayList<Question> tempQuestions;

    private static TasksSQLiteHelp dbHelper = null;
    private static SQLiteDatabase db = null;


    public Questions(TasksSQLiteHelp dbHelper){
        Questions.dbHelper = dbHelper;
    }

    public static void readDB(){
        questions = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM QUESTIONS", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Question q = new Question();

            q.id = cursor.getString(cursor.getColumnIndex("ID"));
            q.title = cursor.getString(cursor.getColumnIndex("QUESTION"));
            q.answer = cursor.getString(cursor.getColumnIndex("ANSWER"));

            q.alternative = new ArrayList();
            q.alternative.add(cursor.getString(cursor.getColumnIndex("ANSWER")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT1")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT2")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT3")));

            questions.add(q);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();
    }

    public static void add(String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO QUESTIONS(QUESTION, ANSWER, OPT1, OPT2, OPT3) VALUES(" +
                    "'"+question+"', " +
                    "'"+answer+"', " +
                    "'"+opt1+"', " +
                    "'"+opt2+"', " +
                    "'"+opt3+"'" +
                    ")");

            dbHelper.close();
            db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT LAST_INSERT_ROWID() AS ID", null);
            cursor.moveToFirst();

            Question q = new Question(); //A cada iteração instancia uma nova 'Question' que será incluida na variável de retorno 'qlist'

            q.id = cursor.getString(cursor.getColumnIndex("ID"));
            q.title = question;
            q.answer = answer;

            q.alternative = new ArrayList();
            q.alternative.add(answer);
            q.alternative.add(opt1);
            q.alternative.add(opt2);
            q.alternative.add(opt3);

            Questions.questions.add(q);

            db.close();
            dbHelper.close();

        } catch (Exception ignored){}
    }

    public static void update(int position, String question, String answer, String opt1, String opt2, String opt3){

        db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();//Preparando a Query
        cv.put("QUESTION",question);
        cv.put("ANSWER",answer);
        cv.put("OPT1",opt1);
        cv.put("OPT2",opt2);
        cv.put("OPT3",opt3);

        try{
        db.update("QUESTIONS", cv, "ID="+questions.get(position).id, null);
        } catch (Exception ex){
            Log.d("ERRO:::", ""+ex);
        }

        db.close();
        dbHelper.close();

        questions.get(position).title = question;
        questions.get(position).answer = answer;
        questions.get(position).alternative.set(0, answer);
        questions.get(position).alternative.set(1, opt1);
        questions.get(position).alternative.set(2, opt2);
        questions.get(position).alternative.set(3, opt3);
    }

    public static void del(int position){

        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM QUESTIONS WHERE ID = '" + questions.get(position).id + "'");
        db.close();
        dbHelper.close();

        questions.remove(position);
    }

    public static void clearDB(){
        db = dbHelper.getReadableDatabase();
        db.delete("QUESTIONS", "id is not null", null);
        db.close();
        dbHelper.close();
    }

}
