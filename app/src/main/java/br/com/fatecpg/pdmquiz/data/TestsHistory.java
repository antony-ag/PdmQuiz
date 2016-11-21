package br.com.fatecpg.pdmquiz.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

/**
 * Created by tony on 19/11/16.
 */

public class TestsHistory{

    private static TasksSQLiteHelp dbHelper = null;
    private static SQLiteDatabase db = null;
    private static ArrayList<TestsList> testsHistory = new ArrayList<>();
    private static TestsList newEntry = new TestsList();

    public TestsHistory(TasksSQLiteHelp dbHelper){
        TestsHistory.dbHelper = dbHelper;
    }

    public static void setNewEntry(int testSize, int points, String sDate){
        newEntry = new TestsList();
        newEntry.testSize = testSize;
        newEntry.points = points;
        newEntry.sDate = sDate;
    }

    public static int getID(int i) {
        return testsHistory.get(i).id;
    }

    public static ArrayList<TestsList> getTestsHistory() {
        return testsHistory;
    }
    public static List<String> getHistoryList(){
        // Teste -> XXX ||| XXX questões ||| xxx acertos ||| data
        List<String> r = new ArrayList<>();
        for(int i = 0; i < testsHistory.size(); i++) {
            r.add("" +
                    "Teste "+testsHistory.get(i).id         +" -> " +
                    testsHistory.get(i).sDate               +"\n" +
                    testsHistory.get(i).testSize            +" quest"+(testsHistory.get(i).testSize == 1 ? "ão" : "ões")+" - " +
                    (testsHistory.get(i).points == 0 ? "Nenhuma correta" : testsHistory.get(i).points+" correta"+(testsHistory.get(i).points == 1 ? "" : "s"))+".");
        }
        return r;
    }


    public static int writeDB(){
        int r = 0;
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO TESTS_HISTORY(T_SIZE, T_POINTS, T_DATE) VALUES(" +
                    +newEntry.testSize+", " +
                    +newEntry.points+", " +
                    "'"+newEntry.sDate+"'" +
                    ")");
            newEntry = null;

            db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT LAST_INSERT_ROWID() AS ID", null);
            cursor.moveToFirst(); //O cursor começa antes do primeiro registro, isso posiciona o cursor no primeiro registro

            r = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID")));

            db.close();
            dbHelper.close();

        } catch (Exception ex){
            Log.d("ERRO:::", ""+ex);
        }
        return r;
    }

    public static void readDB(){
        //this.questions = new ArrayList<>();
        testsHistory = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        //Cursor para percorrer os dados do select
        Cursor cursor = db.rawQuery("SELECT * FROM TESTS_HISTORY", null);
        cursor.moveToFirst(); //O cursor começa antes do primeiro registro, isso posiciona o cursor no primeiro registro

        while (!cursor.isAfterLast()){ // Verifica se todos os registros recuperados do banco já foram percorridos pelo cursor
            TestsList t = new TestsList();

            //Recupera coluna por coluna do banco na posição atual do cursor
            t.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID")));
            t.testSize = Integer.parseInt(cursor.getString(cursor.getColumnIndex("T_SIZE")));
            t.points = Integer.parseInt(cursor.getString(cursor.getColumnIndex("T_POINTS")));
            t.sDate = cursor.getString(cursor.getColumnIndex("T_DATE"));

            testsHistory.add(t);
            //questions.add(q); //Adiciona os dados na variável de retorno

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();
    }

    public static int size(){
        return testsHistory.size();
    }

    public static float testRate(){
        float r=0, c;
        int a, b;
        try{
            for(int i = 0; i < testsHistory.size(); i++) {
                a = testsHistory.get(i).points;
                b = testsHistory.get(i).testSize;
                c = ((float)a/(float)b);
                r += c*100;
            }
            r = r / testsHistory.size();
        }catch (Exception ex){}
        return r;
    }

    public static void delete(int id){
        db = dbHelper.getReadableDatabase();
        db.delete("TESTS_HISTORY", "ID="+id, null);
        db.close();
        dbHelper.close();
    }
}
