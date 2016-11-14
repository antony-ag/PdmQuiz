package br.com.fatecpg.pdmquiz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class MaintenanceActivity extends AppCompatActivity {
    private ArrayList<BdQuestion> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int position = 0;
    TasksSQLiteHelp dbHelper = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        try {
            dbHelper = new TasksSQLiteHelp(getApplicationContext());

            questions = getQuestions();
            refreshQuestion();
        }catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try3->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
        }


        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    public boolean addDB(String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO QUESTIONS(QUESTION, ANSWER, OPT1, OPT2, OPT3) VALUES(" +
                    "'"+question+"', " +
                    "'"+answer+"', " +
                    "'"+opt1+"', " +
                    "'"+opt2+"', " +
                    "'"+opt3+"'" +
                    ")");
            db.close();
            dbHelper.close();
            return true;
        } catch (Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage("try1->"+ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
            return false;
        }
    }

    public boolean changeDB(String id, String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("QUESTION",question);
            cv.put("ANSWER",answer);
            cv.put("OPT1",opt1);
            cv.put("OPT2",opt2);
            cv.put("OPT3",opt3);
            db.update("QUESTIONS", cv, "ID="+id, null);

            /*db.execSQL("UPDATE QUESTIONS SET" +
                    " QUESTION='"+question+
                    "', ANSWER='"+answer+
                    "', OPT1='"+opt1+
                    "', OPT2='"+opt2+
                    "', OPT3='"+opt3+
                    " WHERE ID="+id);*/
            db.close();
            dbHelper.close();
            return true;
        } catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try1->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
            return false;
        }
    }

    public void delDB(View view){
        try{
            if(position<questions.size()) {
                BdQuestion q = questions.get(position);
                db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM QUESTIONS WHERE ID = '" + q._id + "'");
                db.close();
                dbHelper.close();
                position = (position > 0) ? (position - 1) : 0;
                questions = getQuestions();
                refreshQuestion();
            }else if (position == questions.size()){
                refreshQuestion();
            }
            Toast.makeText(getApplicationContext(), "EXCLUIDO!", Toast.LENGTH_SHORT).show();

        } catch (Exception ex){
            newQuestion();
            new AlertDialog.Builder(this)
                    .setMessage("try2->"+ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    protected ArrayList<BdQuestion> getQuestions(){
        ArrayList<BdQuestion> qlist = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        //Cursor para percorrer os dados do select
        Cursor cursor = db.rawQuery("SELECT * FROM QUESTIONS", null);
        cursor.moveToFirst(); //O cursor começa antes do primeiro registro, isso posiciona o cursor no primeiro registro

        while (!cursor.isAfterLast()){
            BdQuestion q = new BdQuestion();
            q.alternative = new ArrayList();
            //q.alternative.clear();

            q._id = cursor.getString(cursor.getColumnIndex("ID"));
            q.title = cursor.getString(cursor.getColumnIndex("QUESTION"));
            q.answer = cursor.getString(cursor.getColumnIndex("ANSWER"));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("ANSWER")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT1")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT2")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT3")));
            //Collections.shuffle(q.alternative);
            qlist.add(q);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();

        return qlist;
    }


    //Seta as perguntas na tela
    private void refreshQuestion(){
        TextView tvp = (TextView)findViewById(R.id.tvPerg);
        TextView tvr = (TextView)findViewById(R.id.tvResposta);
        TextView tva = (TextView)findViewById(R.id.tvAlternativa);
        tvp.setBackgroundColor(0x00000000);
        tvr.setBackgroundColor(0x00000000);
        tva.setBackgroundColor(0x00000000);

        if((questions.size() == 0) || (position == questions.size()-1)) {
            Button btnNext = (Button) findViewById(R.id.btProximo);
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
        }else{
            Button btnNext = (Button)findViewById(R.id.btProximo);
            btnNext.setBackgroundResource(R.drawable.btn_5);
            btnNext.setText("PRÓXIMO");
        }

        if(questions.size()>0 && position<questions.size()){
            //Define a posicao
            BdQuestion q = questions.get(position);
            TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
            posTextView.setText((position+1)+" de "+questions.size());

            //Seta as pergunta na tela
            TextView qTextView = (TextView)findViewById(R.id.tvQuestoes);
            qTextView.setText(q.title);

            //Seta as opções na Tela
            RadioButton opt1 = (RadioButton)findViewById(R.id.rbOpcao1);
            opt1.setText(q.alternative.get(0).toString());
            RadioButton opt2 = (RadioButton)findViewById(R.id.rbOpcao2);
            opt2.setText(q.alternative.get(1).toString());
            RadioButton opt3 = (RadioButton)findViewById(R.id.rbOpcao3);
            opt3.setText(q.alternative.get(2).toString());
            RadioButton opt4 = (RadioButton)findViewById(R.id.rbOpcao4);
            opt4.setText(q.alternative.get(3).toString());
            RadioGroup group = (RadioGroup)findViewById(R.id.rgRespostas);
            group.check(0);

            //Compara se a pergunta
            if(q.answer.equals(opt1.getText()))
                group.check(R.id.rbOpcao1);
            else if(q.answer.equals(opt2.getText()))
                group.check(R.id.rbOpcao2);
            else if(q.answer.equals(opt3.getText()))
                group.check(R.id.rbOpcao3);
            else if(q.answer.equals(opt4.getText()))
                group.check(R.id.rbOpcao4);
        }else{
            Button btnNext = (Button) findViewById(R.id.btProximo);
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
            newQuestion();
        }

    }

    public void anterior(View view){
        if(position>0) {
            position--;
            refreshQuestion();
        }
    }
    public void proximo(View view){

        if(position<questions.size()-1) {
            position++;
            //Carregar o próximo
            refreshQuestion();
            //se não houver proximo entrar no modo nova questão
        }else {
            newQuestion();
        }

    }

    public void newQuestion(){
        position = questions.size();

        TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
        posTextView.setText("Nova questão");

        //Seta as pergunta na tela
        TextView qTextView = (TextView) findViewById(R.id.tvQuestoes);
        qTextView.setText("Clique para editar a pergunta");

        //Seta as opções na Tela
        RadioButton opt1 = (RadioButton) findViewById(R.id.rbOpcao1);
        opt1.setText("Clique para editar a resposta");
        RadioButton opt2 = (RadioButton) findViewById(R.id.rbOpcao2);
        opt2.setText("Clique para editar a alternativa 1");
        RadioButton opt3 = (RadioButton) findViewById(R.id.rbOpcao3);
        opt3.setText("Clique para editar a alternativa 2");
        RadioButton opt4 = (RadioButton) findViewById(R.id.rbOpcao4);
        opt4.setText("Clique para editar a alternativa 3");
        RadioGroup group = (RadioGroup) findViewById(R.id.rgRespostas);
        group.check(0);
    }

    //Chama os popups
    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        abrePopup(opt.getText().toString(), "Opção", view);
        //userAnswers.set(position, opt.getText().toString());
    }
    public void tvQuest(View view){
        TextView tv = (TextView)findViewById(view.getId());
        abrePopupTv(tv.getText().toString(), "Pergunta", view);
    }
    //Popups
    public void abrePopup(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set dialog message
        alertDialogBuilder
                .setMessage("Entre com o novo texto")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Alterar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioButton opt = (RadioButton)findViewById(view.getId());
                        String oldText = opt.getText().toString();
                        String newText = input.getText().toString();
                        opt.setText((newText.equals("")) ? oldText : newText);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // show it
        alertDialogBuilder.show();
    }
    public void abrePopupTv(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set dialog message
        alertDialogBuilder
                .setMessage("Entre com o novo texto")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Alterar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextView tv = (TextView)findViewById(view.getId());
                        String oldText = tv.getText().toString();
                        String newText = input.getText().toString();
                        tv.setText((newText.equals("")) ? oldText : newText);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // show it
        alertDialogBuilder.show();
    }


    public void gravar(View view){

        //Nova Questão
        TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
        String tvNew = posTextView.getText().toString();
        boolean newQuestion = tvNew.equals("Nova questão");

        //Pergunta
        TextView tv = (TextView)findViewById(R.id.tvQuestoes);
        String pergunta = tv.getText().toString();

        //Resposta
        RadioButton rb = (RadioButton)findViewById(R.id.rbOpcao1);
        String answer = rb.getText().toString();

        //Alternativas
        RadioButton rb1 = (RadioButton)findViewById(R.id.rbOpcao2);
        String opt1 = rb1.getText().toString();
        RadioButton rb2 = (RadioButton)findViewById(R.id.rbOpcao3);
        String opt2 = rb2.getText().toString();
        RadioButton rb3 = (RadioButton)findViewById(R.id.rbOpcao4);
        String opt3 = rb3.getText().toString();

        boolean ck1 = !(pergunta.equals("Clique para editar a pergunta"));
        boolean ck2 = !(answer.equals("Clique para editar a resposta"));
        boolean ck3 = !(opt1.equals("Clique para editar a alternativa 1"));
        boolean ck4 = !(opt2.equals("Clique para editar a alternativa 2"));
        boolean ck5 = !(opt3.equals("Clique para editar a alternativa 3"));

        TextView tvp = (TextView)findViewById(R.id.tvPerg);
        TextView tvr = (TextView)findViewById(R.id.tvResposta);
        TextView tva = (TextView)findViewById(R.id.tvAlternativa);

        if(ck1&&ck2&&ck3&&ck4&&ck5){
            tvp.setBackgroundColor(0x00000000);
            tvr.setBackgroundColor(0x00000000);
            tva.setBackgroundColor(0x00000000);
            if(newQuestion){
                addDB(pergunta, answer, opt1, opt2, opt3);
            }else{
                BdQuestion q = questions.get(position);
                changeDB(q._id, pergunta, answer, opt1, opt2, opt3);
            }
            questions = getQuestions();
            refreshQuestion();
            Toast.makeText(getApplicationContext(), "GRAVADO!", Toast.LENGTH_SHORT).show();
        }else{
            if(!ck1){tvp.setBackgroundColor(Color.parseColor("#FF0000"));}else{tvp.setBackgroundColor(0x00000000);}
            if(!ck2){tvr.setBackgroundColor(Color.parseColor("#FF0000"));}else{tvr.setBackgroundColor(0x00000000);}
            if(!(ck3||ck4||ck5)){tva.setBackgroundColor(Color.parseColor("#FF0000"));}else{tva.setBackgroundColor(0x00000000);}
            new AlertDialog.Builder(this)
                    .setMessage("Você deve editar todos os campos antes de gravar.")
                    .setPositiveButton("Ok", null)
                    .show();
        }

    }

//metodos teste
    private void clearDB(){
        db = dbHelper.getReadableDatabase();
        //Será criado um cursor para percorrer os dados do select
        db.delete("QUESTIONS", "id is not null", null);
        db.close(); // Objeto banco---
        dbHelper.close();
    }

}
