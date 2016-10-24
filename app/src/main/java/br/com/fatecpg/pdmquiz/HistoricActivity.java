package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoricActivity extends AppCompatActivity {
    private ArrayList<String> options = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        TextView dados = (TextView)findViewById(R.id.txtDados);

        if(Armazenador.historico.isEmpty()) {
            dados.setVisibility(View.VISIBLE);
        }else{
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Armazenador.historico);
            ListView list = (ListView) findViewById(R.id.optionsListView);
            list.setAdapter(aa);
            dados.setEnabled(false);
        }
    }
    public void voltar(View View){
        Intent i =  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
