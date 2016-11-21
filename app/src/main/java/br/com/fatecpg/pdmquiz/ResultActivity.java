package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent i = getIntent();
        double resultado = i.getDoubleExtra("result", 0);

        TextView tvResultado = (TextView)findViewById(R.id.tvResultado);
        tvResultado.setText(String.format("%.2f",resultado)+"%");


        // Insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void inicio(View view){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
