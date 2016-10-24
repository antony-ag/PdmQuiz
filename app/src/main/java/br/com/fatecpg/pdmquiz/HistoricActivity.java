package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        /*
        options.add("Teste 1 (18 questões) 04/10/2016 12:00 - 10 Pontos");
        options.add("Teste 2 (10 questões) 04/10/2016 13:00 - 10 Pontos");
        options.add("Teste 3 (30 questões) 04/10/2016 13:10 - 25 Pontos");
        options.add("Teste 4 (25 questões) 04/10/2016 14:00 - 5 Pontos");
        */
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Armazenador.historico);
        ListView list = (ListView) findViewById(R.id.optionsListView);
        list.setAdapter(aa);
    }
    public void voltar(View View){
        Intent i =  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
