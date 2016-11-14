package br.com.fatecpg.pdmquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.com.fatecpg.pdmquiz.db.TasksSQLiteHelp;

public class MaintenanceActivity extends AppCompatActivity {
    private ArrayList<BdQuestion> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int position = 0;
    TasksSQLiteHelp dbHelper = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        try {
            dbHelper = new TasksSQLiteHelp(getApplicationContext());

            inflateDB();
            //clearDB();

            questions = getQuestions();
            refreshQuestion();
        }catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try3->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
        }


        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    public boolean addDB(String question, String answer, String opt1, String opt2, String opt3){
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
            return true;
        } catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try1->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
            return false;
        }
    }

    public boolean changeDB(String id, String question, String answer, String opt1, String opt2, String opt3){
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("UPDATE QUESTIONS SET" +
                    " QUESTION="+question+
                    ", ANSWER="+answer+
                    ", OPT1="+opt1+
                    ", OPT2="+opt2+
                    ", OPT3="+opt3+
                    " WHERE ID='"+id+"'");
            db.close();
            dbHelper.close();
            return true;
        } catch (Exception ex){
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try1->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
            return false;
        }
    }

    public void delDB(View view){
        try{
            if(position<questions.size()) {
                BdQuestion q = questions.get(position);
                db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM QUESTIONS WHERE ID = '" + q._id + "'");
                db.close();
                dbHelper.close();
                position = (position > 0) ? (position - 1) : 0;
                questions = getQuestions();
                refreshQuestion();
            }else if (position == questions.size()){
                refreshQuestion();
            }
        } catch (Exception ex){
            newQuestion();
            new AlertDialog.Builder(this)  //onde eu coloquei this, ele espera um contexto, o this representa este contexto(activity), eu poderia usar o getApplicationContext() para retornar o contexto da activity qye está visível
                    .setMessage("try2->"+ex.getLocalizedMessage()) // ex é a variável declarada no Exception, getLocalizedMessage() é uma mensagem dentro de ex... Isso é o que será exibido para o usuário.
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    protected ArrayList<BdQuestion> getQuestions(){
        ArrayList<BdQuestion> qlist = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        //Cursor para percorrer os dados do select
        Cursor cursor = db.rawQuery("SELECT * FROM QUESTIONS", null);
        cursor.moveToFirst(); //O cursor começa antes do primeiro registro, isso posiciona o cursor no primeiro registro

        while (!cursor.isAfterLast()){
            BdQuestion q = new BdQuestion();
            q.alternative = new ArrayList();
            //q.alternative.clear();

            q._id = cursor.getString(cursor.getColumnIndex("ID"));
            q.title = cursor.getString(cursor.getColumnIndex("QUESTION"));
            q.answer = cursor.getString(cursor.getColumnIndex("ANSWER"));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("ANSWER")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT1")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT2")));
            q.alternative.add(cursor.getString(cursor.getColumnIndex("OPT3")));
            //Collections.shuffle(q.alternative);
            qlist.add(q);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        dbHelper.close();

        return qlist;
    }

    private void clearDB(){
        db = dbHelper.getReadableDatabase();
        //Será criado um cursor para percorrer os dados do select
        db.delete("QUESTIONS", "id is not null", null);
        db.close(); // Objeto banco---
        dbHelper.close();
    }

    private void inflateDB(){

        addDB("O que o operador INTERSECT retorna?",
                "Retorna todos os registros comuns a várias consultas",
                "Retorna todo o conteúdo da tabela",
                "Retorna todas as tabelas existentes",
                "Retorna VAI CURINTIA");


        addDB("Com qual comando abaixo eu deleto a View VU_CONTORLE?",
                "DROP VIEW VU_CONTROLE",
                "DELETE VIEW VU_CONTROLE",
                "DROP VU_CONTROLE",
                "DELETE VU_CONTROLE");

        addDB("Com qual comando eu consigo visualizar todas as sequências existentes?",
                "SELECT * FROM USER_SEQUENCES",
                "SELECT * FROM USER_SEQUENCES",
                "SELECT * SEQUENCE",
                "SELECT * FROM SEQUENCES");

        // Terminar de converter

/*            Question q4 = new Question();
            q4.title = "Com qual comando eu deleto o sinônimo DOCENTE?";
            q4.answer = "DROP SYNONYM DOCENTE";
            q4.alternative = new ArrayList();
            q4.alternative.add("DROP SYNONYM DOCENTE");
            q4.alternative.add("DELETE DOCENTE");
            q4.alternative.add("DEL SYNONYM DOCENTE");
            q4.alternative.add("DROP DOCENTE");
        Collections.shuffle(q4.alternative);
        questions.add(q4);

            Question q5 = new Question();
            q5.title = "Qual o nome do comando para criar tabelas?";
            q5.answer = "CREATE TABLE";
            q5.alternative = new ArrayList();
            q5.alternative.add("CREATE TAB");
            q5.alternative.add("CREATE TABLE");
            q5.alternative.add("CREATE");
            q5.alternative.add("ONCREATE");
        Collections.shuffle(q5.alternative);
        questions.add(q5);

            Question q6 = new Question();
            q6.title = "Como criar um usuário em banco de dados chamado FATEC com a senha de 123456 e solicitar que a senha seja alterada no primeiro acesso?";
            q6.answer = "CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE";
            q6.alternative = new ArrayList();
            q6.alternative.add("CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE");
            q6.alternative.add("CREATE USER FATEC BY 123456 EXPIRE");
            q6.alternative.add("CREATE USER FATEC IDENTIFIED 123456");
            q6.alternative.add("CREATE USER FATEC PASSWORD EXPIRE");
        Collections.shuffle(q6.alternative);
        questions.add(q6);

            Question q7 = new Question();
            q7.title = "Como criar um grupo chamado 5º ciclo em banco de dados?";
            q7.answer = "CREATE ROLE 5º CICLO";
            q7.alternative = new ArrayList();
            q7.alternative.add("CREATE ROLE 5º CICLO");
            q7.alternative.add("CREATE GROUP 5º CILCLO");
            q7.alternative.add("CREATE 5º CICLO");
            q7.alternative.add("CREATE CICLO");
        Collections.shuffle(q7.alternative);
        questions.add(q7);

            Question q8 = new Question();
            q8.title = "Como criar um sinônimo para a tabela professor?";
            q8.answer = "CREATE SYNONYM DOCENTE FOR PROFESSOR";
            q8.alternative = new ArrayList();
            q8.alternative.add("CREATE SYNONYM DOCENTE FOR PROFESSOR");
            q8.alternative.add("CREATE SYNONYM DOCENTE IN PROFESSOR");
            q8.alternative.add("CREATE SYNONIM DOCENTE FOR PROFESSOR");
            q8.alternative.add("CREATE SYNONIM DOCENTE IN PROFESSOR");
        Collections.shuffle(q8.alternative);
        questions.add(q8);

            Question q9 = new Question();
            q9.title = "Qual o comando para visualizar todas as sequências criadas em um usuário em banco de dados?";
            q9.answer = "SELECT * FROM USER_SEQUENCES";
            q9.alternative = new ArrayList();
            q9.alternative.add("SELECT * FROM USER_SEQUENCES");
            q9.alternative.add("SELECT FROM USER_SEQUENCES");
            q9.alternative.add("SELECT USER_SEQUENCES");
            q9.alternative.add("SELECT * FROM SEQUENCES");
        Collections.shuffle(q9.alternative);
        questions.add(q9);

            Question q10 = new Question();
            q10.title = "Qual o comando em banco de dados utilizado para dar privilégios?";
            q10.answer = "GRANT";
            q10.alternative = new ArrayList();
            q10.alternative.add("GRANT");
            q10.alternative.add("GRANT");
            q10.alternative.add("PRIVILEGIO");
            q10.alternative.add("SELECT");
        Collections.shuffle(q10.alternative);
        questions.add(q10);

            Question q11 = new Question();
            q11.title = "Qual o comando em banco de dados utilizado para buscar informações?";
            q11.answer = "SELECT";
            q11.alternative = new ArrayList();
            q11.alternative.add("GRANT");
            q11.alternative.add("CREATE");
            q11.alternative.add("PRIVILEGIO");
            q11.alternative.add("SELECT");
        Collections.shuffle(q11.alternative);
        questions.add(q11);

            Question q12 = new Question();
            q12.title = "Qual o comando em banco de dados utilizado para criar itens no banco?";
            q12.answer = "CREATE";
            q12.alternative = new ArrayList();
            q12.alternative.add("GRANT");
            q12.alternative.add("CREATE");
            q12.alternative.add("PRIVILEGIO");
            q12.alternative.add("SELECT");
        Collections.shuffle(q12.alternative);
        questions.add(q12);

            Question q13 = new Question();
            q13.title = "Atribuir o usuário AUXILIXAR para o grupo de usuarios SECRETARIA";
            q13.answer = "GRANT SECRETARIA TO AUXILIAR";
            q13.alternative = new ArrayList();
            q13.alternative.add("GRANT SECRETARIA TO AUXILIAR");
            q13.alternative.add("GRANT SECRETARIA IN AUXILIAR");
            q13.alternative.add("GRANT SECRETARIA FOR AUXILIAR");
            q13.alternative.add("GRANT SECRETARIA A AUXILIAR");
        Collections.shuffle(q13.alternative);
        questions.add(q13);

            Question q14 = new Question();
            q14.title = "Conceder privilégios de criar sessão, criar tabela e criar sequencia o grupo SECRETARIA";
            q14.answer = "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA";
            q14.alternative = new ArrayList();
            q14.alternative.add("GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA");
            q14.alternative.add("GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE IN SECRETARIA");
            q14.alternative.add("GRANT SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA");
            q14.alternative.add("GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE FOR SECRETARIA");
        Collections.shuffle(q14.alternative);
        questions.add(q14);

            Question q15 = new Question();
            q15.title = "Conceder privilégio de criar view e criar sinonimo para o usuario AUXILIAR";
            q15.answer = "GRANT CREATE VIEW, CREATE SYNONYM TO AUXILIAR";
            q15.alternative = new ArrayList();
            q15.alternative.add("GRANT CREATE VIEW, CREATE SYNONYM FOR AUXILIAR");
            q15.alternative.add("GRANT VIEW, CREATE SYNONYM TO AUXILIAR");
            q15.alternative.add("GRANT CREATE VIEW, CREATE SYNONYM IN AUXILIAR");
            q15.alternative.add("GRANT CREATE VIEW TO AUXILIAR");
        Collections.shuffle(q15.alternative);
        questions.add(q15);

            Question q16 = new Question();
            q16.title = "Criar um indice chamado IDX_ALUNO para a coluna NMALUNO da tabela ALUNO";
            q16.answer = "CREATE INDEX IDX_ALUNO ON ALUNO (NMALUNO)";
            q16.alternative = new ArrayList();
            q16.alternative.add("CREATE INDEX IDX_ALUNO IN ALUNO (NMALUNO)");
            q16.alternative.add("CREATE IDX_ALUNO ON ALUNO (NMALUNO)");
            q16.alternative.add("CREATE INDEX IDX_ALUNO FOR ALUNO (NMALUNO)");
            q16.alternative.add("INDEX IDX_ALUNO ON ALUNO (NMALUNO)");
        Collections.shuffle(q16.alternative);
        questions.add(q16);

            Question q17 = new Question();
            q17.title = "O que faz o comando EXISTS?";
            q17.answer = "Testa a existência de linhas no conjunto de resultados da sub-consulta";
            q17.alternative = new ArrayList();
            q17.alternative.add("Testa a existência de linhas no conjunto de resultados da sub-consulta");
            q17.alternative.add("Testa se há tabelas no banco");
            q17.alternative.add("Testa a sub-consulta para verificar se a mesma funciona corretamente");
            q17.alternative.add("N.D.A ");
        Collections.shuffle(q17.alternative);
        questions.add(q17);

            Question q18 = new Question();
            q18.title = "Para que é utilizado o TABLESPACE";
            q18.answer = "É utilizado para agrupar estruturas lógicas e relacionadas";
            q18.alternative = new ArrayList();
            q18.alternative.add("É utilizado para agrupar estruturas lógicas e relacionadas");
            q18.alternative.add("É o nome dado as espaços não preenchidos da tabela");
            q18.alternative.add("É uma tabela grande que contém muito espaço para armazenar dados");
            q18.alternative.add("N.D.A");
        Collections.shuffle(q18.alternative);
        questions.add(q18);

            Question q19 = new Question();
            q19.title = "O que tipo de dado passo armazenar numa váriavel tipo RAW em PL/SQL";
            q19.answer = "Dados Binários";
            q19.alternative = new ArrayList();
            q19.alternative.add("Dados String");
            q19.alternative.add("Dados Binários");
            q19.alternative.add("Dados int");
            q19.alternative.add("Dados Boolean");
        Collections.shuffle(q19.alternative);
        questions.add(q19);

            Question q20 = new Question();
            q20.title = "Qual o comando para apagar a trigger EXPLOSÃO";
            q20.answer = "DROP TRIGGER EXPLOSAO";
            q20.alternative = new ArrayList();
            q20.alternative.add("DROP TRIGGER EXPLOSAO");
            q20.alternative.add("DROP EXPLOSAO");
            q20.alternative.add("DROP TRIGGER IN EXPLOSAO");
            q20.alternative.add("DROP TRIGGER TO EXPLOSAO");
        Collections.shuffle(q20.alternative);
        questions.add(q20);

            Question q21 = new Question();
            q21.title = "Qual o comando para recompilar a trigger EXPLOSÃO";
            q21.answer = "ALTER TRIGGER EXPLOSAO COMPILE";
            q21.alternative = new ArrayList();
            q21.alternative.add("ALTER TRIGGER EXPLOSAO COMPILE");
            q21.alternative.add("TRIGGER EXPLOSAO COMPILE");
            q21.alternative.add("COMPILE TRIGGER EXPLOSAO");
            q21.alternative.add("ALTER TRIGGER EXPLOSAO");
        Collections.shuffle(q21.alternative);
        questions.add(q21);


            Question q22 = new Question();
            q22.title = "O que significa SQL?";
            q22.answer = " Linguagem de Consulta em Banco de Dados";
            q22.alternative = new ArrayList();
            q22.alternative.add(" Linguagem de Consulta em Banco de Dados");
            q22.alternative.add("São Paulo");
            q22.alternative.add("Sinônimo Query Linguagem");
            q22.alternative.add("Significa questões de linguagem");
            q22.alternative.add("Não sei");
        Collections.shuffle(q22.alternative);
        questions.add(q22);


            Question q23 = new Question();
            q23.title = "A linguagem SQL é a mesma em todos os bancos de dados?";
            q23.answer = "Não";
            q23.alternative = new ArrayList();
            q23.alternative.add("Sim");
            q23.alternative.add("Não");
            q23.alternative.add("Talvez");
            q23.alternative.add("Não sei");
        Collections.shuffle(q23.alternative);
        questions.add(q23);


            Question q24 = new Question();
            q24.title = "É possível usar SQL em todos os bancos?";
            q24.answer = "Sim, se forem relacionais.";
            q24.alternative = new ArrayList();
            q24.alternative.add("Sim, se forem relacionais.");
            q24.alternative.add("Não! Cada um utiliza uma linguagem própria");
            q24.alternative.add("Não");
            q24.alternative.add("Não sei essa tá muito difícil");
        Collections.shuffle(q24.alternative);
        questions.add(q24);


            Question q25 = new Question();
            q25.title = "O que é DML?";
            q25.answer = "Linguagem de Manipulação de Dados";
            q25.alternative = new ArrayList();
            q25.alternative.add("Linguagem de Manipulação de Dados");
            q25.alternative.add("Linguagem de Modalidade de Dados");
            q25.alternative.add("Linguagem de Modularização de Dados");
            q25.alternative.add("Não sei");
        Collections.shuffle(q25.alternative);
        questions.add(q25);


            Question q26 = new Question();
            q26.title = "O que é DDL?";
            q26.answer = "Linguagem de Definição de Dados";
            q26.alternative = new ArrayList();
            q26.alternative.add("Linguagem de Definição de Dados");
            q26.alternative.add("Linguagem de Detenção de Dados");
            q26.alternative.add("Linguagem de Descrição de Dados");
            q26.alternative.add("Linguagem de Destruição de Dados");
        Collections.shuffle(q26.alternative);
        questions.add(q26);


            Question q27 = new Question();
            q27.title = "Os comandos SQL precisam ser escritos em letras maiúsculas?";
            q27.answer = "Não";
            q27.alternative = new ArrayList();
            q27.alternative.add("Não");
            q27.alternative.add("Sim");
            q27.alternative.add("Depende");
            q27.alternative.add("Sempre");
        Collections.shuffle(q27.alternative);
        questions.add(q27);


            Question q28 = new Question();
            q28.title = "Comandos SQL podem ser escritos em uma única linha ou precisa ser separado em várias linhas?";
            q28.answer = "Pode ser colocado em várias linhas.";
            q28.alternative = new ArrayList();
            q28.alternative.add("Pode ser colocado em várias linhas.");
            q28.alternative.add("Somente poderá ser escrito em uma única linha");
            q28.alternative.add("Pode ser colocado em várias linhas desde que no final de cada linha seja colocado o ponto e vírgula");
            q28.alternative.add("N.D.A");
            q28.alternative.add("Curitiba");
        Collections.shuffle(q28.alternative);
        questions.add(q28);

            Question q29 = new Question();
            q29.title = "Para que serve o ponto-e-vírgula no final das linhas?";
            q29.answer = "Serve para terminar um comando ou instrução SQL";
            q29.alternative = new ArrayList();
            q29.alternative.add("Serve para terminar um comando ou instrução SQL");
            q29.alternative.add("Serve para dividir um comando ou instrução em várias linhas SQL");
            q29.alternative.add("Não sei");
            q29.alternative.add("N.D.A");
            q29.alternative.add("Curitiba");
        Collections.shuffle(q29.alternative);
        questions.add(q29);


            Question q30 = new Question();
            q30.title = "Qual dos operadores abaixo não é um operador lógico?";
            q30.answer = "TRUE";
            q30.alternative = new ArrayList();
            q30.alternative.add("AND");
            q30.alternative.add("OR");
            q30.alternative.add("NOT");
            q30.alternative.add("TRUE");
        Collections.shuffle(q30.alternative);
        questions.add(q30);
*/
    }

    //Seta as perguntas na tela
    private void refreshQuestion(){
        if((questions.size() == 0) || (position == questions.size()-1)) {
            Button btnNext = (Button) findViewById(R.id.btProximo);
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
        }else{
            Button btnNext = (Button)findViewById(R.id.btProximo);
            btnNext.setBackgroundResource(R.drawable.btn_5);
            btnNext.setText("PRÓXIMO");
        }

        if(questions.size()>0 && position<questions.size()){
            //Define a posicao
            BdQuestion q = questions.get(position);
            TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
            posTextView.setText((position+1)+" de "+questions.size());

            //Seta as pergunta na tela
            TextView qTextView = (TextView)findViewById(R.id.tvQuestoes);
            qTextView.setText(q.title);

            //Seta as opções na Tela
            RadioButton opt1 = (RadioButton)findViewById(R.id.rbOpcao1);
            opt1.setText(q.alternative.get(0).toString());
            RadioButton opt2 = (RadioButton)findViewById(R.id.rbOpcao2);
            opt2.setText(q.alternative.get(1).toString());
            RadioButton opt3 = (RadioButton)findViewById(R.id.rbOpcao3);
            opt3.setText(q.alternative.get(2).toString());
            RadioButton opt4 = (RadioButton)findViewById(R.id.rbOpcao4);
            opt4.setText(q.alternative.get(3).toString());
            RadioGroup group = (RadioGroup)findViewById(R.id.rgRespostas);
            group.check(0);

            //Compara se a pergunta
            if(q.answer.equals(opt1.getText()))
                group.check(R.id.rbOpcao1);
            else if(q.answer.equals(opt2.getText()))
                group.check(R.id.rbOpcao2);
            else if(q.answer.equals(opt3.getText()))
                group.check(R.id.rbOpcao3);
            else if(q.answer.equals(opt4.getText()))
                group.check(R.id.rbOpcao4);
        }else{
            Button btnNext = (Button) findViewById(R.id.btProximo);
            btnNext.setText("NOVA");
            btnNext.setBackgroundResource(R.drawable.btn_1);
            newQuestion();
        }

    }

    public void anterior(View view){
        if(position>0) {
            position--;
            refreshQuestion();
        }
    }
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

    public void newQuestion(){
        position = questions.size();

        TextView posTextView = (TextView) findViewById(R.id.tvPosicao);
        posTextView.setText("Nova questão");

        //Seta as pergunta na tela
        TextView qTextView = (TextView) findViewById(R.id.tvQuestoes);
        qTextView.setText("Clique para editar a pergunta");

        //Seta as opções na Tela
        RadioButton opt1 = (RadioButton) findViewById(R.id.rbOpcao1);
        opt1.setText("Clique para editar a resposta");
        RadioButton opt2 = (RadioButton) findViewById(R.id.rbOpcao2);
        opt2.setText("Clique para editar a alternativa");
        RadioButton opt3 = (RadioButton) findViewById(R.id.rbOpcao3);
        opt3.setText("Clique para editar a alternativa");
        RadioButton opt4 = (RadioButton) findViewById(R.id.rbOpcao4);
        opt4.setText("Clique para editar a alternativa");
        RadioGroup group = (RadioGroup) findViewById(R.id.rgRespostas);
        group.check(0);
    }

    public void abrePopup(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set dialog message
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
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // show it
        alertDialogBuilder.show();
    }

    public void abrePopupTv(String textoAtual, String title, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        final EditText input = new EditText(this);
        input.setHint(textoAtual);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set dialog message
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
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // show it
        alertDialogBuilder.show();
    }

    public void refreshTv(View view, EditText ed){
        TextView tv = (TextView)findViewById(view.getId());
        String oldText = tv.getText().toString();
        String newText = ed.getText().toString();
        tv.setText((newText.equals("")) ? oldText : newText);
    }

    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        abrePopup(opt.getText().toString(), "Opção", view);

        //userAnswers.set(position, opt.getText().toString());
    }

    public void tvQuest(View view){
        TextView tv = (TextView)findViewById(view.getId());
        abrePopupTv(tv.getText().toString(), "Pergunta", view);
    }

    public void finalizar (View view){
        int sum = 0;
        for(int i=0; i<questions.size(); i++){
            if(questions.get(i).answer.equals(userAnswers.get(i))){
                sum++;
            }
        }
        Intent m = getIntent();
        int qtdQuestions = m.getIntExtra("qtdQuestions", 0);
        String teste = "Teste "+(Armazenador.historico.size()+1)+" ("+qtdQuestions+" questões) "+sum+" pontos "+getDateTime();
        Armazenador.historico.add(teste);
        Armazenador.nAcertos += sum;
        Armazenador.nQuestions +=qtdQuestions;

        teste = "";
        double result = 100.0 * (double)sum / (double)questions.size();
        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        i.putExtra("result", result);
        startActivity(i);
        finish();
    }

    public void cancelTest(View View) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
