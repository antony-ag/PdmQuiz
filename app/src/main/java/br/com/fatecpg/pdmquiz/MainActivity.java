package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    public void initTest(View View) {
        EditText e = getEditText(R.id.editQtd);
        if (e.getText().toString().length() <= 0) {
            e.setError("Preencha o campo Quantidade de questões.");
            e.requestFocus();
        } else if (Integer.parseInt(e.getText().toString()) < 10 || Integer.parseInt(e.getText().toString()) > 30) {
            e.setError("Preencha no minimo 10 e no máximo 30");
            e.requestFocus();
        } else {
            Intent i = new Intent(getApplicationContext(), TestActivity.class);
            i.putExtra("qtdQuestions", e.getText().toString());
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
