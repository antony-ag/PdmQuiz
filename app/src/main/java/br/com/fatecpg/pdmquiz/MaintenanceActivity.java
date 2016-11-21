package br.com.fatecpg.pdmquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fatecpg.pdmquiz.data.Questions;

public class MaintenanceActivity extends AppCompatActivity {
    private int position = 0; //cursor para identificar a questão à ser exibida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        try {
            Questions.readDB(); //Recupera as questões gravadas no banco
            refreshScreen(); //Atualiz a tela
        } catch (Exception ex) {
            new AlertDialog.Builder(this)
                    .setMessage("onCreate() -> " + ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }


        //Insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Exclui no banco o registro atual e atualiza o ambiente.
     *
     * @param view
     */
    public void delReg(View view) {
        try {
            /**
             * INDEX:position
             * SIZE:questions.size()
             * Quando o usuário está exibindo na tela uma nova questão '[INDEX:position]' não corresponde a nenhuma questão existente em 'questions'.
             *
             * Vale lembrar que o INDEX de um array não corresponde ao número de posições SIZE do mesmo... 
             * (Ex. A última posição de um array de SIZE:5 corresponde ao INDEX:4)
             *
             * Portando:
             */
            if (position < Questions.questions.size()) { /** O usuário deseja apgar uma questão que está exibida na tela*/

                Questions.del(position);

                position = (position > 0) ? (position - 1) : 0; //Atualiza a posição do cursor

                Toast.makeText(getApplicationContext(), "EXCLUIDO!", Toast.LENGTH_SHORT).show();

            } else if (position == Questions.questions.size()) { /** Nenhuma questão está sendo exibida na tela*/

                Toast.makeText(getApplicationContext(), "EXCLUIDO!", Toast.LENGTH_SHORT).show();

            }

            refreshScreen();

        } catch (Exception ex) {

            position = Questions.questions.size();
            refreshScreen();

            new AlertDialog.Builder(this)
                    .setMessage("delDB() ->" + ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    /**
     * Atualiza a activity
     */
    private void refreshScreen() {

        //Restaura a formatação original dos TextViews
        TextView tvp = (TextView) findViewById(R.id.tvPerg);
        TextView tvr = (TextView) findViewById(R.id.tvResposta);
        TextView tva = (TextView) findViewById(R.id.tvAlternativa);
        tvp.setBackgroundColor(0x00000000);
        tvr.setBackgroundColor(0x00000000);
        tva.setBackgroundColor(0x00000000);

        //Atualiza os campos da activity em função da posição atual do cursor 'position'
        if (Questions.questions.size() > 0 && position >= 0 && position < Questions.questions.size()) { // Existem questões em 'questions' e cursor não estão posicionado na última questão.
            //Define a posicao
            //Questions q = Questions.questions.get(position);
            try {
                TextView posTextView = (TextView) findViewById(R.id.tvPosicao);

                posTextView.setText((position + 1) + " de " + Questions.questions.size());

                //Seta as pergunta na tela
                TextView qTextView = (TextView) findViewById(R.id.tvQuestoes);
                qTextView.setText(Questions.questions.get(position).title);

                //Seta as opções na Tela
                //Resposta
                RadioButton answer = (RadioButton) findViewById(R.id.rbAnswer);
                answer.setText(Questions.questions.get(position).alternative.get(0).toString());
                //Alternativas
                RadioButton opt1 = (RadioButton) findViewById(R.id.rbOpcao1);
                opt1.setText(Questions.questions.get(position).alternative.get(1).toString());
                RadioButton opt2 = (RadioButton) findViewById(R.id.rbOpcao2);
                opt2.setText(Questions.questions.get(position).alternative.get(2).toString());
                RadioButton opt3 = (RadioButton) findViewById(R.id.rbOpcao3);
                opt3.setText(Questions.questions.get(position).alternative.get(3).toString());
                RadioGroup group = (RadioGroup) findViewById(R.id.rgRespostas);
                group.check(0);
            } catch (Exception ex) {

            }

        } else {

            position = Questions.questions.size();

            TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
            posTextView.setText((position + 1) + "(*) de " + Questions.questions.size());

            //Seta as pergunta na tela
            TextView qTextView = (TextView) findViewById(R.id.tvQuestoes);
            qTextView.setText("Clique para editar a pergunta");

            //Seta as opções na Tela
            RadioButton answer = (RadioButton) findViewById(R.id.rbAnswer);
            answer.setText("Clique para editar a resposta");
            RadioButton opt1 = (RadioButton) findViewById(R.id.rbOpcao1);
            opt1.setText("Clique para editar a alternativa 1");
            RadioButton opt2 = (RadioButton) findViewById(R.id.rbOpcao2);
            opt2.setText("Clique para editar a alternativa 2");
            RadioButton opt3 = (RadioButton) findViewById(R.id.rbOpcao3);
            opt3.setText("Clique para editar a alternativa 3");
            RadioGroup group = (RadioGroup) findViewById(R.id.rgRespostas);
            group.check(0);


        }

        Button btnPrev = (Button) findViewById(R.id.btAnterior);
        if (position <= 0) {
            btnPrev.setText("NOVA");
            btnPrev.setBackgroundResource(R.drawable.btn_1);
        } else {
            btnPrev.setText("ANTERIOR");
            btnPrev.setBackgroundResource(R.drawable.btn_5);
        }

        Button btnNext = (Button) findViewById(R.id.btProximo);
        if ((Questions.questions.size() == 0) || (position >= Questions.questions.size() - 1)) {
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_5);
            btnNext.setText("PRÓXIMO");
        }

    }

    /**
     * Modifica o indica de pocição
     *
     * @param view
     */
    public void anterior(View view) {
        if (position >= 0) {
            position--;
            refreshScreen();
        } else if (position == -1) {
            position = Questions.questions.size();
            refreshScreen();
        } else {
            position = -1;
        }
    }

    /**
     * Modifica o indica de pocição
     *
     * @param view
     */
    public void proximo(View view) {

        if (position < Questions.questions.size() - 1) {
            position++;
            //Carregar o próximo
            refreshScreen();
            //se não houver proximo entrar no modo nova questão
        } else {
            position = Questions.questions.size();
            refreshScreen();
        }

    }


    /**
     * Exibe o popup de modificação dos Textviews
     *
     * @param view
     */
    public void tvQuestClick(View view) {
    }
    //Popups

    /**
     * Popup de modificação de Radiobuttons
     */
    public void editPopup(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Opção");
        final EditText input = new EditText(this);
        if (view instanceof RadioButton) {
            RadioButton opt = (RadioButton) findViewById(view.getId());
            input.setHint(opt.getText().toString());
        } else if (view instanceof TextView) {
            TextView tv = (TextView) findViewById(view.getId());
            input.setHint(tv.getText().toString());
        }
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder
                .setMessage("Entre com o novo texto")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (view instanceof RadioButton) {
                            RadioButton opt = (RadioButton) findViewById(view.getId());
                            String oldText = opt.getText().toString();
                            String newText = input.getText().toString();
                            opt.setText((newText.equals("")) ? oldText : newText);
                            dialog.cancel();
                        } else if (view instanceof TextView) {
                            TextView tv = (TextView) findViewById(view.getId());
                            String oldText = tv.getText().toString();
                            String newText = input.getText().toString();
                            tv.setText((newText.equals("")) ? oldText : newText);
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.show();
    }

    /**
     * Verifica se o usuário informou todos os valores nos campos e passa para os métodos de gravação do banco os valores informados pelo usuário
     *
     * @param view
     */
    public void gravar(View view) {

        //Pergunta
        TextView tv = (TextView) findViewById(R.id.tvQuestoes);
        String pergunta = tv.getText().toString();

        //Resposta
        RadioButton rb = (RadioButton) findViewById(R.id.rbAnswer);
        String answer = rb.getText().toString();

        //Alternativas
        RadioButton rb1 = (RadioButton) findViewById(R.id.rbOpcao1);
        String opt1 = rb1.getText().toString();
        RadioButton rb2 = (RadioButton) findViewById(R.id.rbOpcao2);
        String opt2 = rb2.getText().toString();
        RadioButton rb3 = (RadioButton) findViewById(R.id.rbOpcao3);
        String opt3 = rb3.getText().toString();

        //Verifica se todos os campos foram editados
        boolean ck1 = !(pergunta.equals("Clique para editar a pergunta"));
        boolean ck2 = !(answer.equals("Clique para editar a resposta"));
        boolean ck3 = !(opt1.equals("Clique para editar a alternativa 1"));
        boolean ck4 = !(opt2.equals("Clique para editar a alternativa 2"));
        boolean ck5 = !(opt3.equals("Clique para editar a alternativa 3"));

        if (ck1 && ck2 && ck3 && ck4 && ck5) { //Verifica se todos os campos foram editados

            //Verifica se é nova questão
            TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
            String tvNew = posTextView.getText().toString();
            if (tvNew.equals((position + 1) + "(*) de " + Questions.questions.size())) { //Verifica se o usuário está criando uma nova questão ou alterando uma existente
                Questions.add(pergunta, answer, opt1, opt2, opt3);
            } else {
                Questions.update(position, pergunta, answer, opt1, opt2, opt3);
            }

            refreshScreen();
            Toast.makeText(getApplicationContext(), "GRAVADO!", Toast.LENGTH_SHORT).show();

        } else { //Informa ao usuário que há questões não preenchidas.

            //Muda o background para vermelho onde o usuário deixou de alterar os valors dos campos.
            TextView tvp = (TextView) findViewById(R.id.tvPerg);
            TextView tvr = (TextView) findViewById(R.id.tvResposta);
            TextView tva = (TextView) findViewById(R.id.tvAlternativa);
            if (!ck1) {
                tvp.setBackgroundColor(Color.parseColor("#FF0000"));
            } else {
                tvp.setBackgroundColor(0x00000000);
            }
            if (!ck2) {
                tvr.setBackgroundColor(Color.parseColor("#FF0000"));
            } else {
                tvr.setBackgroundColor(0x00000000);
            }
            if (!ck3 || !ck4 || !ck5) {
                tva.setBackgroundColor(Color.parseColor("#FF0000"));
            } else {
                tva.setBackgroundColor(0x00000000);
            }

            new AlertDialog.Builder(this)
                    .setMessage("Você deve editar todos os campos antes de gravar.")
                    .setPositiveButton("Ok", null)
                    .show();
        }

    }

}
