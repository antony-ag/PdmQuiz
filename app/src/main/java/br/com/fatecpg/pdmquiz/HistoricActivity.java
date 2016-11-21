package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.fatecpg.pdmquiz.data.AnswersHistory;
import br.com.fatecpg.pdmquiz.data.TestsHistory;
import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class HistoricActivity extends AppCompatActivity {
    TextView dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);


        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        TestsHistory.readDB();

        dados = (TextView)findViewById(R.id.txtDados);

        if(TestsHistory.size() > 0) {  //Verificar aqui o tamanho da lista do histórico
            ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TestsHistory.getHistoryList());
            final ListView list = (ListView) findViewById(R.id.optionsListView);
            list.setAdapter(aa);

            list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    HistoricActivity.super.onCreateContextMenu(menu, v, menuInfo);

                    AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo; //com o acmi.position eu tenho retorno de qual foi o id clicado no list view

                    menu.add(Menu.NONE,acmi.position,0,"Excluir");
                }


            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    detalhesActivity(position);
                }
            });


            dados.setVisibility(View.INVISIBLE);
        }else{
            dados.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        excluir(item.getItemId());
        return super.onContextItemSelected(item);
    }

    private void excluir(int position){
        TestsHistory.readDB();
        int idTest = TestsHistory.getID(position);
        TestsHistory.delete(idTest);
        AnswersHistory ah = new AnswersHistory();
        ah.setDbHelper(new TasksSQLiteHelp(this));
        ah.delete(idTest);
        Intent i = new Intent(getApplicationContext(), HistoricActivity.class);
        finish();
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void detalhesActivity(int position){
        Intent i = new Intent(getApplicationContext(), DetalhesActivity.class);
        i.putExtra("position", position);
        startActivity(i);
        //finish();
    }

    public void voltar(View View){
        Intent i =  new Intent(getApplicationContext(),MainActivity.class);
        finish();
        startActivity(i);
    }
}
