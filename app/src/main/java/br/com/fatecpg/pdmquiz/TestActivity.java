package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        createTest();
        clearAnswers();
        refreshQuestion();

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }
    //Lista de perguntas
    private void createTest(){
        Question q = new Question();
        q.question="1+1";
        q.answer="2";
        //Adaptada para 4 repostas
        q.options = new String[]{"1","2","0","2"};
        questions.add(q);

        q = new Question();
        q.question="2+3";
        q.answer="5";
        q.options = new String[]{"5","1","-1","23"};
        questions.add(q);

    }
    //Limpa os campos de resposta
    private void clearAnswers(){
        for(Question question: questions){
            userAnswers.add("");
        }
    }
    //Seta as perguntas na tela
    private void refreshQuestion(){
        //Define a posicao
        Question q = questions.get(position);
        TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
        posTextView.setText((position+1)+" de "+questions.size());

        //Seta as perguntas na tela
        TextView qTextView = (TextView)findViewById(R.id.tvQuestoes);
        qTextView.setText(q.question);

        //Seta as opções na Tela
        RadioButton opt1 = (RadioButton)findViewById(R.id.rbOpcao1);
        opt1.setText(q.options[0]);
        RadioButton opt2 = (RadioButton)findViewById(R.id.rbOpcao2);
        opt2.setText(q.options[1]);
        RadioButton opt3 = (RadioButton)findViewById(R.id.rbOpcao3);
        opt3.setText(q.options[2]);
        RadioButton opt4 = (RadioButton)findViewById(R.id.rbOpcao4);
        opt4.setText(q.options[3]);
        RadioGroup group = (RadioGroup)findViewById(R.id.rgRespostas);
        group.check(0);

        //Compara se a pergunta
        if(userAnswers.get(position).equals(opt1.getText()))
            group.check(R.id.rbOpcao1);
        else if(userAnswers.get(position).equals(opt2.getText()))
            group.check(R.id.rbOpcao2);
        else if(userAnswers.get(position).equals(opt3.getText()))
            group.check(R.id.rbOpcao3);
        else if(userAnswers.get(position).equals(opt4.getText()))
            group.check(R.id.rbOpcao4);

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
            refreshQuestion();
        }
    }
    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        userAnswers.set(position, opt.getText().toString());
    }

    public void finalizar (View view){
        int sum = 0;
        for(int i=0; i<questions.size(); i++){
            if(questions.get(i).answer.equals(userAnswers.get(i))){
                sum++;
            }
        }
        double result = 100.0 * (double)sum / (double)questions.size();
        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        i.putExtra("result", result);
        startActivity(i);
        finish();
    }

    public void cancelTest(View View) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
