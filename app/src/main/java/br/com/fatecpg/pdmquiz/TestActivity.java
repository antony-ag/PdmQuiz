package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import br.com.fatecpg.pdmquiz.data.AnswersHistory;
import br.com.fatecpg.pdmquiz.data.Questions;
import br.com.fatecpg.pdmquiz.data.TestsHistory;
import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class TestActivity extends AppCompatActivity {
    private AnswersHistory userAnswers = new AnswersHistory();

    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent m = getIntent();
        int qtdQuestions = m.getIntExtra("qtdQuestions", 0);

        userAnswers.setDbHelper(new TasksSQLiteHelp(getApplicationContext()));

        createTest(qtdQuestions);
        resize();
        refreshQuestion();

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    private void createTest(int qtdQuestions){


        Questions.readDB();

        Questions.tempQuestions = Questions.questions;

        Collections.shuffle(Questions.tempQuestions);

        for(int i = Questions.tempQuestions.size()-1; i >= qtdQuestions; i--)
            Questions.tempQuestions.remove(i);

        for(int i = 0; i <= Questions.tempQuestions.size()-1; i++)
            Collections.shuffle(Questions.tempQuestions.get(i).alternative);


        }

    private void resize(){
        userAnswers.clear();

        for(int i = 0; i < Questions.tempQuestions.size(); i++){
            userAnswers.add(
                    Questions.tempQuestions.get(i).title,
                    Questions.tempQuestions.get(i).answer,
                    ""
            );
        }
        /*userAnswers = new ArrayList<>();
        for(Question i:Questions.tempQuestions){
            userAnswers.add("", "");
        }*/
    }

    //Seta as perguntas na tela
    private void refreshQuestion(){
        //Define a posicao

        TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
        posTextView.setText((position+1)+" de "+ Questions.tempQuestions.size());

        //Seta as perguntas na tela
        TextView qTextView = (TextView)findViewById(R.id.tvQuestoes);
        qTextView.setText(Questions.tempQuestions.get(position).title);

        //Seta as opções na Tela
        RadioButton opt1 = (RadioButton)findViewById(R.id.rbOpcao1);
        opt1.setText(Questions.tempQuestions.get(position).alternative.get(0).toString());
        RadioButton opt2 = (RadioButton)findViewById(R.id.rbOpcao2);
        opt2.setText(Questions.tempQuestions.get(position).alternative.get(1).toString());
        RadioButton opt3 = (RadioButton)findViewById(R.id.rbOpcao3);
        opt3.setText(Questions.tempQuestions.get(position).alternative.get(2).toString());
        RadioButton opt4 = (RadioButton)findViewById(R.id.rbOpcao4);
        opt4.setText(Questions.tempQuestions.get(position).alternative.get(3).toString());
        RadioGroup group = (RadioGroup)findViewById(R.id.rgRespostas);
        group.check(0);

        //Compara se a pergunta

        if(userAnswers.getUserAnswerByID(position).equals(opt1.getText())){
            group.check(R.id.rbOpcao1);
            userAnswers.setAnswersByID(position, opt1.getText().toString());
        }
        else if(userAnswers.getUserAnswerByID(position).equals(opt2.getText())){
            group.check(R.id.rbOpcao2);
            userAnswers.setAnswersByID(position, opt2.getText().toString());
        }
        else if(userAnswers.getUserAnswerByID(position).equals(opt3.getText())){
            group.check(R.id.rbOpcao3);
            userAnswers.setAnswersByID(position, opt3.getText().toString());
        }
        else if(userAnswers.getUserAnswerByID(position).equals(opt4.getText())){
            group.check(R.id.rbOpcao4);
            userAnswers.setAnswersByID(position, opt4.getText().toString());
        }

    }
    public void anterior(View view){
        if(position>0) {
            position--;
            refreshQuestion();
        }
    }
    public void proximo(View view){
        if(position< Questions.tempQuestions.size()-1) {
            position++;
            refreshQuestion();
        }
    }
    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        userAnswers.setAnswersByID(position, opt.getText().toString());
    }

    public void finalizar (View view){

        TestsHistory.setNewEntry( //Adiciona os dados que serão gravados
                userAnswers.getSize(),
                userAnswers.getPoints(),
                getDateTime());

        int testID = TestsHistory.writeDB(); //Grava os dados adicionados e retorna o ID do banco

        userAnswers.writeDB(testID); //Adiciona os testes no ID do banco especificado


        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        i.putExtra("result", ((double)userAnswers.getPoints()/(double)userAnswers.getSize()*100));
        finish();
        startActivity(i);
    }

    public void cancelTest(View View) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
