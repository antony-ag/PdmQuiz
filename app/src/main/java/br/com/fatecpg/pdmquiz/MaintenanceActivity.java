package br.com.fatecpg.pdmquiz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class MaintenanceActivity extends AppCompatActivity {
    private ArrayList<BdQuestion> questions = new ArrayList<>();
    private int position = 0; //cursor para identificar a questão à ser exibida

    TasksSQLiteHelp dbHelper = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        try {
            dbHelper = new TasksSQLiteHelp(getApplicationContext());

            questions = getQuestions(); //Recupera as questões no banco
            refreshQuestion(); //Atualiz a tela
        }catch (Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage("onCreate() -> "+ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }


        //Insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    /**
     * Adiciona um novo registro ao banco
     * @param question -> Pergunta
     * @param answer -> Resposta certa
     * @param opt1 -> Alternativa 1
     * @param opt2 -> Alternativa 2
     * @param opt3 -> Alternativa 3
     */
    private void addDB(String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO QUESTIONS(QUESTION, ANSWER, OPT1, OPT2, OPT3) VALUES(" +
                    "'"+question+"', " +
                    "'"+answer+"', " +
                    "'"+opt1+"', " +
                    "'"+opt2+"', " +
                    "'"+opt3+"'" +
                    ")");
            db.close();
            dbHelper.close();
        } catch (Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage("addDB() erro ->"+ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    /**
     * Altera um registro existente no banco de dados
     * @param id -> Id do registro no banco
     * @param question -> Pergunta
     * @param answer -> Resposta certa
     * @param opt1 -> Alternativa 1
     * @param opt2 -> Alternativa 2
     * @param opt3 -> Alternativa 3
     */
    private void changeDB(String id, String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            
            ContentValues cv = new ContentValues();//Preparando a Query
            cv.put("QUESTION",question);
            cv.put("ANSWER",answer);
            cv.put("OPT1",opt1);
            cv.put("OPT2",opt2);
            cv.put("OPT3",opt3);
            
            db.update("QUESTIONS", cv, "ID="+id, null);

            db.close();
            dbHelper.close();
        } catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("changeDB erro ->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    /**
     * Exclui no banco o registro atual e atualiza o ambiente.
     * @param view
     */
    public void delDB(View view){
        try{
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
            if(position < questions.size()) { /** O usuário deseja apgar uma questão que está exibida na tela*/ 
                BdQuestion q = questions.get(position); //Instancia uma variável com os dados da questão exibida atualmente na tela.
                
                db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM QUESTIONS WHERE ID = '" + q._id + "'"); //Exclui no banco o _id da questão atual
                db.close();
                dbHelper.close();
                
                position = (position > 0) ? (position - 1) : 0; //Atualiza a posição do cursor
                
                questions = getQuestions();
                refreshQuestion();
                
            }else if (position == questions.size()){ /** Nenhuma questão está sendo exibida na tela*/
                refreshQuestion();
            }
            
            Toast.makeText(getApplicationContext(), "EXCLUIDO!", Toast.LENGTH_SHORT).show();

        } catch (Exception ex){
            newQuestion();
            new AlertDialog.Builder(this)
                    .setMessage("delDB() ->"+ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    /**
     * Recupera os dados gravados no banco.
     * @return ArrayList[ID, QUESTION, ANSWER, OPT1, OPT2, OP3]
     */
    private ArrayList<BdQuestion> getQuestions(){
        
        ArrayList<BdQuestion> qlist = new ArrayList<>(); //Instancia a variável de retorno

        db = dbHelper.getReadableDatabase();

        //Cursor para percorrer os dados do select
        Cursor cursor = db.rawQuery("SELECT * FROM QUESTIONS", null);
        cursor.moveToFirst(); //O cursor começa antes do primeiro registro, isso posiciona o cursor no primeiro registro

        while (!cursor.isAfterLast()){ // Verifica se todos os registros recuperados do banco já foram percorridos pelo cursor
            
            BdQuestion q = new BdQuestion(); //A cada iteração instancia uma nova 'BdQuestion' que será incluida na variável de retorno 'qlist'

            //Recupera coluna por coluna do banco na posição atual do cursor
            q._id = cursor.getString(cursor.getColumnIndex("ID"));
            q.title = cursor.getString(cursor.getColumnIndex("QUESTION"));
            q.answer = cursor.getString(cursor.getColumnIndex("ANSWER"));

            q.alternative = new ArrayList();
            q.alternative.add(cursor.getString(cursor.getColumnIndex("ANSWER")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT1")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT2")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT3")));

            qlist.add(q); //Adiciona os dados na variável de retorno

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();

        return qlist;
    }


    /**
     * Atualiza a activity
     */
    private void refreshQuestion(){
        
        //Restaura a formatação original dos TextViews
        TextView tvp = (TextView)findViewById(R.id.tvPerg);
        TextView tvr = (TextView)findViewById(R.id.tvResposta);
        TextView tva = (TextView)findViewById(R.id.tvAlternativa);
        tvp.setBackgroundColor(0x00000000);
        tvr.setBackgroundColor(0x00000000);
        tva.setBackgroundColor(0x00000000);
        
        //Atualiza os campos da activity em função da posição atual do cursor 'position'
        if(questions.size()>0 && position >= 0 && position<questions.size()){ // Existem questões em 'questions' e cursor não estão posicionado na última questão.
            //Define a posicao
            BdQuestion q = questions.get(position);
            TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
            posTextView.setText((position+1)+" de "+questions.size());

            //Seta as pergunta na tela
            TextView qTextView = (TextView)findViewById(R.id.tvQuestoes);
            qTextView.setText(q.title);

            //Seta as opções na Tela
            //Resposta
            RadioButton answer = (RadioButton)findViewById(R.id.rbAnswer);
            answer.setText(q.alternative.get(0).toString());
            //Alternativas
            RadioButton opt1 = (RadioButton)findViewById(R.id.rbOpcao1);
            opt1.setText(q.alternative.get(1).toString());
            RadioButton opt2 = (RadioButton)findViewById(R.id.rbOpcao2);
            opt2.setText(q.alternative.get(2).toString());
            RadioButton opt3 = (RadioButton)findViewById(R.id.rbOpcao3);
            opt3.setText(q.alternative.get(3).toString());
            RadioGroup group = (RadioGroup)findViewById(R.id.rgRespostas);
            group.check(0);

            //Compara se a pergunta
            group.check(R.id.rbAnswer);

        }else{
            newQuestion();
        }

        changeButtons();

    }

    /**
     * Modifica os botões de próximo e anterior
     */
    private void changeButtons() {
        //Modifica o texto e o tipo do botão 'próximo' caso esteja sendo exibida a última questão registrada ou uma questão nova.
        Button btnPrev = (Button) findViewById(R.id.btAnterior);
        if (position <= 0){
            btnPrev.setText("NOVA");
            btnPrev.setBackgroundResource(R.drawable.btn_1);
        }else{
            btnPrev.setText("ANTERIOR");
            btnPrev.setBackgroundResource(R.drawable.btn_5);
        }

        Button btnNext = (Button) findViewById(R.id.btProximo);
        if((questions.size() == 0) || (position >= questions.size()-1)) {
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
        }else{
            btnNext.setBackgroundResource(R.drawable.btn_5);
            btnNext.setText("PRÓXIMO");
        }
    }

    /**
     * Modifica o indica de pocição
     * @param view
     */
    public void anterior(View view){
        if(position>=0) {
            position--;
            refreshQuestion();
        }else if(position == -1){
            newQuestion();
        }else{
            position = -1;
        }
    }

    /**
     * Modifica o indica de pocição
     * @param view
     */
    public void proximo(View view){

        if(position<questions.size()-1) {
            position++;
            //Carregar o próximo
            refreshQuestion();
            //se não houver proximo entrar no modo nova questão
        }else {
            newQuestion();
        }

    }

    /**
     * Prepara a activity para receber os dados de uma nova questão.
     */
    private void newQuestion(){
        changeButtons();

        position = questions.size();

        TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
        posTextView.setText((position+1)+"(*) de "+ questions.size());

        //Seta as pergunta na tela
        TextView qTextView = (TextView) findViewById(R.id.tvQuestoes);
        qTextView.setText("Clique para editar a pergunta");

        //Seta as opções na Tela
        RadioButton opt1 = (RadioButton) findViewById(R.id.rbAnswer);
        opt1.setText("Clique para editar a resposta");
        RadioButton opt2 = (RadioButton) findViewById(R.id.rbOpcao1);
        opt2.setText("Clique para editar a alternativa 1");
        RadioButton opt3 = (RadioButton) findViewById(R.id.rbOpcao2);
        opt3.setText("Clique para editar a alternativa 2");
        RadioButton opt4 = (RadioButton) findViewById(R.id.rbOpcao3);
        opt4.setText("Clique para editar a alternativa 3");
        RadioGroup group = (RadioGroup) findViewById(R.id.rgRespostas);
        group.check(0);
    }

    /**
     * Exibe o popup de modificação dos RadioButtons
     * @param view
     */
    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        abrePopup(opt.getText().toString(), "Opção", view);
    }

    /**
     * Exibe o popup de modificação dos Textviews
     * @param view
     */
    public void tvQuest(View view){
        TextView tv = (TextView)findViewById(view.getId());
        abrePopupTv(tv.getText().toString(), "Pergunta", view);
    }
    //Popups

    /**
     * Popup de modificação de Radiobuttons
     * @param textoAtual -> Texto atual para exibir no campo hint do EditText do popup
     * @param title -> Título da janela do popup
     * @param view
     */
    public void abrePopup(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder
                .setMessage("Entre com o novo texto")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Alterar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioButton opt = (RadioButton)findViewById(view.getId());
                        String oldText = opt.getText().toString();
                        String newText = input.getText().toString();
                        opt.setText((newText.equals("")) ? oldText : newText);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.show();
    }

    /**
     * Popup de modificação de TextView
     * @param textoAtual -> Texto atual para exibir no campo hint do EditText do popup
     * @param title -> Título da janela do popup
     * @param view
     */
    public void abrePopupTv(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder
                .setMessage("Entre com o novo texto")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Alterar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextView tv = (TextView)findViewById(view.getId());
                        String oldText = tv.getText().toString();
                        String newText = input.getText().toString();
                        tv.setText((newText.equals("")) ? oldText : newText);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }


    /**
     * Verifica se o usuário informou todos os valores nos campos e passa para os métodos de gravação do banco os valores informados pelo usuário
     * @param view
     */
    public void gravar(View view){

        //Nova Questão
        TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
        String tvNew = posTextView.getText().toString();
        boolean newQuestion = tvNew.equals((position+1)+"(*) de "+ questions.size());

        //Pergunta
        TextView tv = (TextView)findViewById(R.id.tvQuestoes);
        String pergunta = tv.getText().toString();

        //Resposta
        RadioButton rb = (RadioButton)findViewById(R.id.rbAnswer);
        String answer = rb.getText().toString();

        //Alternativas
        RadioButton rb1 = (RadioButton)findViewById(R.id.rbOpcao1);
        String opt1 = rb1.getText().toString();
        RadioButton rb2 = (RadioButton)findViewById(R.id.rbOpcao2);
        String opt2 = rb2.getText().toString();
        RadioButton rb3 = (RadioButton)findViewById(R.id.rbOpcao3);
        String opt3 = rb3.getText().toString();

        //Verifica se todos os campos foram editados
        boolean ck1 = !(pergunta.equals("Clique para editar a pergunta"));
        boolean ck2 = !(answer.equals("Clique para editar a resposta"));
        boolean ck3 = !(opt1.equals("Clique para editar a alternativa 1"));
        boolean ck4 = !(opt2.equals("Clique para editar a alternativa 2"));
        boolean ck5 = !(opt3.equals("Clique para editar a alternativa 3"));

        TextView tvp = (TextView)findViewById(R.id.tvPerg);
        TextView tvr = (TextView)findViewById(R.id.tvResposta);
        TextView tva = (TextView)findViewById(R.id.tvAlternativa);

        if(ck1&&ck2&&ck3&&ck4&&ck5){ //Verifica se todos os campos foram editados
            //Restaura a formatação dos campos
            tvp.setBackgroundColor(0x00000000);
            tvr.setBackgroundColor(0x00000000);
            tva.setBackgroundColor(0x00000000);

            if(newQuestion){ //Verifica se o usuário está criando uma nova questão ou alterando uma existente
                addDB(pergunta, answer, opt1, opt2, opt3);
            }else{
                BdQuestion q = questions.get(position);
                changeDB(q._id, pergunta, answer, opt1, opt2, opt3);
            }

            questions = getQuestions();
            refreshQuestion();
            Toast.makeText(getApplicationContext(), "GRAVADO!", Toast.LENGTH_SHORT).show();

        }else{ //Informa ao usuário que há questões não preenchidas.

            //Muda o background para vermelho onde o usuário deixou de alterar os valors dos campos.
            if(!ck1){tvp.setBackgroundColor(Color.parseColor("#FF0000"));}else{tvp.setBackgroundColor(0x00000000);}
            if(!ck2){tvr.setBackgroundColor(Color.parseColor("#FF0000"));}else{tvr.setBackgroundColor(0x00000000);}
            if(!ck3||!ck4||!ck5){tva.setBackgroundColor(Color.parseColor("#FF0000"));}else{tva.setBackgroundColor(0x00000000);}

            new AlertDialog.Builder(this)
                    .setMessage("Você deve editar todos os campos antes de gravar.")
                    .setPositiveButton("Ok", null)
                    .show();
        }

    }

//metodos teste .... Excluir ao final do projeto
    private void clearDB(){
        db = dbHelper.getReadableDatabase();
        //Será criado um cursor para percorrer os dados do select
        db.delete("QUESTIONS", "id is not null", null);
        db.close(); // Objeto banco---
        dbHelper.close();
    }
}
