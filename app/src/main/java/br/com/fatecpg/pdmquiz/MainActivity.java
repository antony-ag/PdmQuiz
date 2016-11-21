package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.fatecpg.pdmquiz.data.Questions;
import br.com.fatecpg.pdmquiz.data.TestsHistory;
import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class MainActivity extends AppCompatActivity {
    Integer minQuestoes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        //Instancia o banco de dados
        new Questions(new TasksSQLiteHelp(getApplicationContext()));
        new TestsHistory(new TasksSQLiteHelp(getApplicationContext()));

        //Carrega a tabela QUESTIONS na classe
        Questions.readDB();
        TestsHistory.readDB();

        TextView tvQtdQuestoes = (TextView) findViewById(R.id.txtQtdQuestoes);
        tvQtdQuestoes.setText(Questions.questions.size() == 0 ?
                "Cadastre algumas questões antes de iniciar um teste." :
                "Informe o numero de questões para o seu teste(Entre "
                +minQuestoes+" e "+ Questions.questions.size()+"):");


        int qtdTestes = TestsHistory.size() > 0 ? TestsHistory.size() : 0;
        float estatistica = TestsHistory.testRate() > 0 ? TestsHistory.testRate() : 0;

        TextView txtqtdTestes = (TextView) findViewById(R.id.txtValor);
        txtqtdTestes.setText(String.valueOf(qtdTestes));

        TextView txtrendimento = (TextView) findViewById(R.id.txtRendimentoValor);
        txtrendimento.setText(String.format("%.2f", estatistica) + " %");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Questions.readDB();
        TestsHistory.readDB();
        int qtdTestes = TestsHistory.size() > 0 ? TestsHistory.size() : 0;
        float estatistica = TestsHistory.testRate() > 0 ? TestsHistory.testRate() : 0;

        TextView txtqtdTestes = (TextView) findViewById(R.id.txtValor);
        txtqtdTestes.setText(String.valueOf(qtdTestes));

        TextView txtrendimento = (TextView) findViewById(R.id.txtRendimentoValor);
        txtrendimento.setText(String.format("%.2f", estatistica) + " %");
        TextView tvQtdQuestoes = (TextView) findViewById(R.id.txtQtdQuestoes);

        tvQtdQuestoes.setText(Questions.questions.size() == 0 ?
                "Cadastre algumas questões antes de iniciar um teste." :
                "Informe o numero de questões para o seu teste(Entre "
                +minQuestoes+" e "+ Questions.questions.size()+"):");


        }

    public void maintenance(View View) {
        Intent i = new Intent(getApplicationContext(), MaintenanceActivity.class);
        finish();
        startActivity(i);
    }

    public void initTest(View View) {
        EditText e = getEditText(R.id.editQtd);
        if (Questions.questions.size() == 0) {
            e.setText("");
            e.setError("Você não tem questões cadastradas.");
            e.requestFocus();
        } else if (e.getText().toString().length() <= 0) {
            e.setError("Preencha o campo quantidade de questões.");
            e.requestFocus();
        } else if (Integer.parseInt(e.getText().toString()) < minQuestoes
                || Integer.parseInt(e.getText().toString()) > Questions.questions.size()) {
            e.setText("");
            e.setError("Preencha no mínimo "+minQuestoes+" e no máximo "+ Questions.questions.size());
            e.requestFocus();
        } else {
            Intent i = new Intent(getApplicationContext(), TestActivity.class);
            i.putExtra("qtdQuestions", Integer.parseInt(e.getText().toString()));
            finish();
            startActivity(i);

        }
    }

    public void listar(View View) {
        Intent i = new Intent(getApplicationContext(), HistoricActivity.class);
        finish();
        startActivity(i);
    }

    public EditText getEditText(int id) {
        return (EditText) findViewById(id);
    }
}
