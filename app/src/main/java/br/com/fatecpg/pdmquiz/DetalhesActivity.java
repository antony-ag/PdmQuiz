package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.fatecpg.pdmquiz.data.AnswersHistory;
import br.com.fatecpg.pdmquiz.data.TestsHistory;
import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Intent i = getIntent();
        int postition = i.getIntExtra("position", 0);

        TestsHistory.readDB();

        AnswersHistory ah = new AnswersHistory();
        ah.setDbHelper(new TasksSQLiteHelp(this));
        List l = ah.readDB_BY_ID(TestsHistory.getID(postition));



        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);
        ListView list = (ListView) findViewById(R.id.optionsListView);
        list.setAdapter(aa);

    }

    public void voltar(View View){
        super.onBackPressed();
    }

}
