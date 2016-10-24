package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    double result;
    double nAcertos;
    double nQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        nQuestions = Armazenador.nQuestions;
        nAcertos = Armazenador.nAcertos;

        if (nAcertos != 0 && nQuestions != 0){
            result = 100.0 * nAcertos / nQuestions;
        }else{
            result = 0;
            nAcertos = 0;
            nQuestions = 0;
        }
        TextView txtqtdTestes = (TextView)findViewById(R.id.txtValor);
        txtqtdTestes.setText(String.valueOf(Armazenador.historico.size()));

        TextView txtrendimento = (TextView)findViewById(R.id.txtRendimentoValor);
        txtrendimento.setText(String.format("%.2f",result)+" %");
    }

    public void initTest(View View) {
        EditText e = getEditText(R.id.editQtd);
        if (e.getText().toString().length() <= 0) {
            e.setError("Preencha o campo Quantidade de questões.");
            e.requestFocus();
        } else if (Integer.parseInt(e.getText().toString()) < 0 || Integer.parseInt(e.getText().toString()) > 30) {
            e.setError("Preencha no minimo 10 e no máximo 30");
            e.requestFocus();
        } else {
            Intent i = new Intent(getApplicationContext(), TestActivity.class);
            i.putExtra("qtdQuestions", Integer.parseInt(e.getText().toString()));
            startActivity(i);
        }
    }

    public void listar(View View) {
        Intent i = new Intent(getApplicationContext(), HistoricActivity.class);
        startActivity(i);
    }

    public EditText getEditText(int id) {
        EditText e = (EditText) findViewById(id);
        return e;
    }
}
