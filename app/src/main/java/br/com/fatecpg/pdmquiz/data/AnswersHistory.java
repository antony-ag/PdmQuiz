package br.com.fatecpg.pdmquiz.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

/**
 * Created by tony on 19/11/16.
 */

public class AnswersHistory {


    private TasksSQLiteHelp dbHelper = null;
    private SQLiteDatabase db = null;
    private AnswersList answers = new AnswersList();

    public void setDbHelper(TasksSQLiteHelp dbHelper) {
        this.dbHelper = dbHelper;
    }


    public void add(String question, String answer, String userAnswer){
        answers.add(question, answer, userAnswer);
    }

    public String getUserAnswerByID(int id){
        return answers.userAnswer.get(id);
    }

    public void setAnswersByID(int id, String userAnswer){
        answers.userAnswer.set(id, userAnswer);
    }

    public void clear(){
        answers = new AnswersList();
    }

    public int getPoints(){ return answers.getPoints(); }

    public int getSize(){ return answers.size(); }

    public void writeDB(int id){
        try{
            db = dbHelper.getWritableDatabase();
            for(int i = 0; i < answers.id.size(); i++){
                answers.id.set(i, String.valueOf(id));
                db.execSQL("INSERT INTO ANSWERS_HISTORY(ID_TEST, QUESTION, ANSWER, USER_ANSWER) VALUES(" +
                        "'"+answers.id.get(i)+"', " +
                        "'"+answers.question.get(i)+"', " +
                        "'"+answers.answer.get(i)+"'," +
                        "'"+answers.userAnswer.get(i)+"'" +
                        ")");
            }

            db.close();
            dbHelper.close();

        } catch (Exception ignored){}
    }

    public List<String> readDB_BY_ID(int testID){
        AnswersList answersID = new AnswersList();

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM ANSWERS_HISTORY WHERE ID_TEST="+testID, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){

            answersID.id.add(cursor.getString(cursor.getColumnIndex("ID_TEST")));
            answersID.question.add(cursor.getString(cursor.getColumnIndex("QUESTION")));
            answersID.answer.add(cursor.getString(cursor.getColumnIndex("ANSWER")));
            answersID.userAnswer.add(cursor.getString(cursor.getColumnIndex("USER_ANSWER")));

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();

        List<String> r = new ArrayList<>();
        for(int i = 0; i < answersID.id.size(); i++) {
            r.add("" +
                    "Pergunta "+(i+1)+": " + answersID.question.get(i).toString() + "\n" +
                    "Resposta certa: " + answersID.answer.get(i).toString() + "\n" +
                    "Sua resposta: " +  answersID.userAnswer.get(i).toString() + "\n" +
                    "\n");
        }
        return r;
    }

    public void delete(int id){
        db = dbHelper.getReadableDatabase();
        db.delete("ANSWERS_HISTORY", "ID_TEST="+id, null);
        db.close();
        dbHelper.close();
    }


}
