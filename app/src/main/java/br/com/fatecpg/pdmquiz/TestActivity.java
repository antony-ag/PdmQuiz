package br.com.fatecpg.pdmquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        createTest();
        clearAnswers();
        refreshQuestion();

        // insere icone no actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }
    //Lista de perguntas
    public class Test {
        public static ArrayList<Question> getTest() {
            ArrayList<Question> test;
            test = new ArrayList<>();

            Question q = new Question();
            q.title = "O que o operador INTERSECT retorna?";
            q.answer = "Retorna todos os registros comuns a várias consultas";
            q.alternative = new String[]{"Retorna todos os registros comuns a várias consultas", "Retorna todo o conteúdo da tabela", "Retorna todas as tabelas existentes", "Retorna VAI CURINTIA"};
            test.add(q);


            Question q1 = new Question();
            q1.title = "Com qual comando abaixo eu deleto a View VU_CONTORLE?";
            q1.answer = "DROP VIEW VU_CONTROLE";
            q1.alternative = new String[]{"DROP VIEW VU_CONTROLE", "DELETE VIEW VU_CONTROLE", "DROP VU_CONTROLE", "DELETE VU_CONTROLE"};
            test.add(q1);

            Question q2 = new Question();
            q2.title = "Com qual comando eu consigo visualizar todas as sequências existentes?";
            q2.answer = "SELECT * FROM USER_SEQUENCES";
            q2.alternative = new String[]{"SELECT * FROM SEQUENCE", "SELECT * FROM USER_SEQUENCES", "SELECT * SEQUENCE", "SELECT * FROM SEQUENCES"};
            test.add(q2);

            Question q3 = new Question();
            q3.title = "Com qual comando eu deleto o sinônimo DOCENTE?";
            q3.answer = "DROP SYNONYM DOCENTE";
            q3.alternative = new String[]{"DROP SYNONYM DOCENTE", "DELETE DOCENTE", "DEL SYNONYM DOCENTE", "DROP DOCENTE"};
            test.add(q3);

            Question q4 = new Question();
            q4.title = "Qual o nome do comando para criar tabelas?";
            q4.answer = "CREATE TABLE";
            q4.alternative = new String[]{"CREATE TAB", "CREATE TABLE", "CREATE", "ONCREATE"};
            test.add(q4);

            Question q5 = new Question();
            q5.title = "Como criar um usuário em banco de dados chamado FATEC com a senha de 123456 e solicitar que a senha seja alterada no primeiro acesso?";
            q5.answer = "CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE";
            q5.alternative = new String[]{"CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE", "CREATE USER FATEC BY 123456 EXPIRE", "CREATE USER FATEC IDENTIFIED 123456", "CREATE USER FATEC PASSWORD EXPIRE"};
            test.add(q5);

            Question q6 = new Question();
            q6.title = "Como criar um grupo chamado 5º ciclo em banco de dados?";
            q6.answer = "CREATE ROLE 5º CICLO";
            q6.alternative = new String[]{"CREATE ROLE 5º CICLO", "CREATE GROUP 5º CILCLO", "CREATE 5º CICLO", "CREATE CICLO"};
            test.add(q6);

            Question q7 = new Question();
            q7.title = "Como criar um sinônimo para a tabela professor?";
            q7.answer = "CREATE SYNONYM DOCENTE FOR PROFESSOR";
            q7.alternative = new String[]{"CREATE SYNONYM DOCENTE FOR PROFESSOR", "CREATE SYNONYM DOCENTE IN PROFESSOR", "CREATE SYNONIM DOCENTE FOR PROFESSOR", "CREATE SYNONIM DOCENTE IN PROFESSOR"};
            test.add(q7);

            Question q8 = new Question();
            q8.title = "Qual o comando para visualizar todas as sequências criadas em um usuário em banco de dados?";
            q8.answer = "SELECT * FROM USER_SEQUENCES";
            q8.alternative = new String[]{"SELECT * FROM USER_SEQUENCES", "SELECT FROM USER_SEQUENCES", "SELECT USER_SEQUENCES", "SELECT * FROM SEQUENCES"};
            test.add(q8);

            Question q9 = new Question();
            q9.title = "Qual o comando em banco de dados utilizado para dar privilégios?";
            q9.answer = "GRANT";
            q9.alternative = new String[]{"GRANT", "CREATE", "PRIVILEGIO", "SELECT"};
            test.add(q9);

            Question q10 = new Question();
            q10.title = "Qual o comando em banco de dados utilizado para buscar informações?";
            q10.answer = "SELECT";
            q10.alternative = new String[]{"GRANT", "CREATE", "PRIVILEGIO", "SELECT"};
            test.add(q10);

            Question q11 = new Question();
            q11.title = "Qual o comando em banco de dados utilizado para criar itens no banco?";
            q11.answer = "CREATE";
            q11.alternative = new String[]{"GRANT", "CREATE", "PRIVILEGIO", "SELECT"};
            test.add(q11);

            Question q12 = new Question();
            q12.title = "Atribuir o usuário AUXILIXAR para o grupo de usuarios SECRETARIA";
            q12.answer = "GRANT SECRETARIA TO AUXILIAR";
            q12.alternative = new String[]{"GRANT SECRETARIA TO AUXILIAR", "GRANT SECRETARIA IN AUXILIAR", "GRANT SECRETARIA FOR AUXILIAR", "GRANT SECRETARIA A AUXILIAR"};
            test.add(q12);

            Question q13 = new Question();
            q13.title = "Conceder privilégios de criar sessão, criar tabela e criar sequencia o grupo SECRETARIA";
            q13.answer = "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA";
            q13.alternative = new String[]{"GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA", "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE IN SECRETARIA", "GRANT SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA", "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE FOR SECRETARIA"};
            test.add(q13);

            Question q14 = new Question();
            q14.title = "Conceder privilégio de criar view e criar sinonimo para o usuario AUXILIAR";
            q14.answer = "GRANT CREATE VIEW, CREATE SYNONYM TO AUXILIAR";
            q14.alternative = new String[]{"GRANT CREATE VIEW, CREATE SYNONYM FOR AUXILIAR", "GRANT VIEW, CREATE SYNONYM TO AUXILIAR", "GRANT CREATE VIEW, CREATE SYNONYM IN AUXILIAR", "GRANT CREATE VIEW TO AUXILIAR"};
            test.add(q14);

            Question q15 = new Question();
            q15.title = "Criar um indice chamado IDX_ALUNO para a coluna NMALUNO da tabela ALUNO";
            q15.answer = "CREATE INDEX IDX_ALUNO ON ALUNO (NMALUNO)";
            q15.alternative = new String[]{"CREATE INDEX IDX_ALUNO IN ALUNO (NMALUNO)", "CREATE IDX_ALUNO ON ALUNO (NMALUNO)", "CREATE INDEX IDX_ALUNO FOR ALUNO (NMALUNO)", "INDEX IDX_ALUNO ON ALUNO (NMALUNO)"};
            test.add(q15);

            Question q16 = new Question();
            q16.title = "O que faz o comando EXISTS?";
            q16.answer = "Testa a existência de linhas no conjunto de resultados da sub-consulta";
            q16.alternative = new String[]{"Testa a existência de linhas no conjunto de resultados da sub-consulta", "Testa se há tabelas no banco", "Testa a sub-consulta para verificar se a mesma funciona corretamente","N.D.A "};
            test.add(q16);

            Question q17 = new Question();
            q17.title = "Para que é utilizado o TABLESPACE";
            q17.answer = "É utilizado para agrupar estruturas lógicas e relacionadas";
            q17.alternative = new String[]{"É utilizado para agrupar estruturas lógicas e relacionadas", "É o nome dado as espaços não preenchidos da tabela", "É uma tabela grande que contém muito espaço para armazenar dados", "N.D.A"};
            test.add(q17);

            Question q = new Question();
            q.title = "O que tipo de dado passo armazenar numa váriavel tipo RAW em PL/SQL";
            q.answer = "Dados Binários";
            q.alternative = new String[]{"Dados String", "Dados Binários", "Dados int", "Dados Boolean"};
            test.add(q);

            Question q18 = new Question();
            q18.title = "Qual o comando para apagar a trigger EXPLOSÃO";
            q18.answer = "DROP TRIGGER EXPLOSAO";
            q18.alternative = new String[]{"DROP TRIGGER EXPLOSAO", "DROP EXPLOSAO", "DROP TRIGGER IN EXPLOSAO", "DROP TRIGGER TO EXPLOSAO"};
            test.add(q18);

            Question q19 = new Question();
            q19.title = "Qual o comando para recompilar a trigger EXPLOSÃO";
            q19.answer = "ALTER TRIGGER EXPLOSAO COMPILE";
            q19.alternative = new String[]{"ALTER TRIGGER EXPLOSAO COMPILE", "TRIGGER EXPLOSAO COMPILE", "COMPILE TRIGGER EXPLOSAO", "ALTER TRIGGER EXPLOSAO"};
            test.add(q19);


            Question q20 = new Question();
            q20.title = "O que significa SQL?";
            q20.answer = " Linguagem de Consulta em Banco de Dados";
            q20.alternative = new String[]{" Linguagem de Consulta em Banco de Dados", "SÃ£o Paulo", "Sinônimo Query Linguagem", "Significa questões de linguagem", "Não sei"};
            test.add(q20);


            Question q21 = new Question();
            q21.title = "A linguagem SQL é a mesma em todos os bancos de dados?";
            q21.answer = "Não";
            q21.alternative = new String[]{"Sim", "Não", "Talvez", "Não sei"};
            test.add(q21);


            Question q22 = new Question();
            q22.title = "É possível usar SQL em todos os bancos?";
            q22.answer = "Sim, se forem relacionais.";
            q22.alternative = new String[]{"Sim, se forem relacionais.", "Não! Cada um utiliza uma linguagem própria", "Não", "Não sei essa tá muito difícil"};
            test.add(q22);


            Question q23 = new Question();
            q23.title = "O que é DML?";
            q23.answer = "Linguagem de Manipulação de Dados";
            q23.alternative = new String[]{"Linguagem de Manipulação de Dados", "Linguagem de Modalidade de Dados", "Linguagem de Modularização de Dados", "Não sei"};
            test.add(q23);


            Question q24 = new Question();
            q24.title = "O que é DDL?";
            q24.answer = "Linguagem de Definição de Dados";
            q24.alternative = new String[]{"Linguagem de Definição de Dados", "Linguagem de Detenção de Dados", "Linguagem de Descrição de Dados", "Linguagem de Destruição de Dados"};
            test.add(q24);


            Question q25 = new Question();
            q25.title = "Os comandos SQL precisam ser escritos em letras maiúsculas?";
            q25.answer = "Não";
            q25.alternative = new String[]{"Não", "Sim", "Depende", "Sempre"};
            test.add(q25);


            Question q26 = new Question();
            q26.title = "Comandos SQL podem ser escritos em uma única linha ou precisa ser separado em várias linhas?";
            q26.answer = "Pode ser colocado em várias linhas.";
            q26.alternative = new String[]{"Pode ser colocado em várias linhas.", "Somente poderá ser escrito em uma única linha", "Pode ser colocado em várias linhas desde que no final de cada linha seja colocado o ponto e vírgula", "N.D.A", "Curitiba"};
            test.add(q26);


            Question q27 = new Question();
            q27.title = "Para que serve o ponto-e-vírgula no final das linhas?";
            q27.answer = "Serve para terminar um comando ou instrução SQL";
            q27.alternative = new String[]{"Serve para terminar um comando ou instrução SQL", "Serve para dividir um comando ou instrução em várias linhas SQL", "Não sei", "N.D.A", "Curitiba"};
            test.add(q27);


            Question q28 = new Question();
            q28.title = "Qual dos operadores abaixo não é um operador lógico?";
            q28.answer = "TRUE";
            q28.alternative = new String[]{"AND", "OR", "NOT", "TRUE"};
            test.add(q28);

            return test;



        }

    }
    //Limpa os campos de resposta
    private void clearAnswers(){
        for(Question question: questions){
            userAnswers.add("");
        }
    }
    //Seta as perguntas na tela
    private void refreshQuestion(){
        //Define a posicao
        Question q = questions.get(position);
        TextView posTextView = (TextView)findViewById(R.id.tvPosicao);
        posTextView.setText((position+1)+" de "+questions.size());

        //Seta as perguntas na tela
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
        if(userAnswers.get(position).equals(opt1.getText()))
            group.check(R.id.rbOpcao1);
        else if(userAnswers.get(position).equals(opt2.getText()))
            group.check(R.id.rbOpcao2);
        else if(userAnswers.get(position).equals(opt3.getText()))
            group.check(R.id.rbOpcao3);
        else if(userAnswers.get(position).equals(opt4.getText()))
            group.check(R.id.rbOpcao4);

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
            refreshQuestion();
        }
    }
    public void opcaoSelecionada(View view){
        RadioButton opt = (RadioButton)findViewById(view.getId());
        userAnswers.set(position, opt.getText().toString());
    }

    public void finalizar (View view){
        int sum = 0;
        for(int i=0; i<questions.size(); i++){
            if(questions.get(i).answer.equals(userAnswers.get(i))){
                sum++;
            }
        }
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
}
