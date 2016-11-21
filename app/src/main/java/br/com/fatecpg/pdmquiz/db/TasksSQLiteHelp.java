package br.com.fatecpg.pdmquiz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksSQLiteHelp extends SQLiteOpenHelper {

    public TasksSQLiteHelp(Context context) {
        super(context, "quiz.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CRIAR A TABELAS
        db.execSQL("" +
                "CREATE TABLE QUESTIONS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "QUESTION TEXT, " +
                "ANSWER TEXT, " +
                "OPT1 TEXT, " +
                "OPT2 TEXT, " +
                "OPT3 TEXT" +
                ");");
        inflateDB(db);

        db.execSQL("" +
                "CREATE TABLE TESTS_HISTORY(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "T_SIZE INTEGER, " +
                "T_POINTS INTEGER, " +
                "T_DATE TEXT" +
                ");");

        db.execSQL("" +
                "CREATE TABLE ANSWERS_HISTORY(" +
                "ID_TEST TEXT, " +
                "QUESTION TEXT, " +
                "ANSWER TEXT, " +
                "USER_ANSWER TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS QUESTIONS");
        onCreate(db);
    }

    /**
     * Adiciona registros ao banco
     * @param db
     * @param question
     * @param answer
     * @param opt1
     * @param opt2
     * @param opt3
     */
    private void addDB(SQLiteDatabase db, String question, String answer, String opt1, String opt2, String opt3) {
        db.execSQL("INSERT INTO QUESTIONS(QUESTION, ANSWER, OPT1, OPT2, OPT3) VALUES(" +
                "'" + question + "', " +
                "'" + answer + "', " +
                "'" + opt1 + "', " +
                "'" + opt2 + "', " +
                "'" + opt3 + "')");
    }

    /**
     * Pré cadastro de questões do QUIZ
     * @param db
     */
    private void inflateDB(SQLiteDatabase db) {
        addDB(db,
                "O que o operador INTERSECT retorna?",
                "Retorna todos os registros comuns a várias consultas",
                "Retorna todo o conteúdo da tabela",
                "Retorna todas as tabelas existentes",
                "Retorna VAI CURINTIA");
        addDB(db,
                "Com qual comando abaixo eu deleto a View VU_CONTORLE?",
                "DROP VIEW VU_CONTROLE",
                "DELETE VIEW VU_CONTROLE",
                "DROP VU_CONTROLE",
                "DELETE VU_CONTROLE");
        addDB(db,
                "Com qual comando eu consigo visualizar todas as sequências existentes?",
                "SELECT * FROM USER_SEQUENCES",
                "SELECT * FROM USER_SEQUENCES",
                "SELECT * SEQUENCE",
                "SELECT * FROM SEQUENCES");
        addDB(db,
                "Com qual comando eu deleto o sinônimo DOCENTE?",
                "DROP SYNONYM DOCENTE",
                "DELETE DOCENTE",
                "DEL SYNONYM DOCENTE",
                "DROP DOCENTE");
        addDB(db,
                "Qual o nome do comando para criar tabelas?",
                "CREATE TABLE",
                "CREATE TAB",
                "CREATE",
                "ONCREATE");
        addDB(db,
                "Como criar um usuário em banco de dados chamado FATEC com a senha de 123456 e solicitar que a senha seja alterada no primeiro acesso?",
                "CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE",
                "CREATE USER FATEC IDENTIFIED BY 123456 PASSWORD EXPIRE",
                "CREATE USER FATEC IDENTIFIED 123456",
                "CREATE USER FATEC PASSWORD EXPIRE");
        addDB(db,
                "Como criar um grupo chamado 5º ciclo em banco de dados?",
                "CREATE ROLE 5º CICLO",
                "CREATE GROUP 5º CILCLO",
                "CREATE 5º CICLO",
                "CREATE CICLO");
        addDB(db,
                "Como criar um sinônimo para a tabela professor?",
                "CREATE SYNONYM DOCENTE FOR PROFESSOR",
                "CREATE SYNONYM DOCENTE IN PROFESSOR",
                "CREATE SYNONIM DOCENTE FOR PROFESSOR",
                "CREATE SYNONIM DOCENTE IN PROFESSOR");
        addDB(db,
                "Qual o comando para visualizar todas as sequências criadas em um usuário em banco de dados?",
                "SELECT * FROM USER_SEQUENCES",
                "SELECT FROM USER_SEQUENCES",
                "SELECT USER_SEQUENCES",
                "SELECT * FROM SEQUENCES");
        addDB(db,
                "Qual o comando em banco de dados utilizado para dar privilégios?",
                "GRANT",
                "ROLE",
                "PRIVILEGIO",
                "SELECT");
        addDB(db,
                "Qual o comando em banco de dados utilizado para buscar informações?",
                "SELECT",
                "GRANT",
                "CREATE",
                "PRIVILEGIO");
        addDB(db,
                "Qual o comando em banco de dados utilizado para criar itens no banco?",
                "CREATE",
                "GRANT",
                "PRIVILEGIO",
                "SELECT");
        addDB(db,
                "Atribuir o usuário AUXILIXAR para o grupo de usuarios SECRETARIA",
                "GRANT SECRETARIA TO AUXILIAR",
                "GRANT SECRETARIA IN AUXILIAR",
                "GRANT SECRETARIA FOR AUXILIAR",
                "GRANT SECRETARIA A AUXILIAR");
        addDB(db,
                "Conceder privilégios de criar sessão, criar tabela e criar sequencia o grupo SECRETARIA",
                "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA",
                "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE IN SECRETARIA",
                "GRANT SESSION, CREATE TABLE, CREATE SEQUENCE TO SECRETARIA",
                "GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE FOR SECRETARIA");
        addDB(db,
                "Conceder privilégio de criar view e criar sinonimo para o usuario AUXILIAR",
                "GRANT CREATE VIEW, CREATE SYNONYM TO AUXILIAR",
                "GRANT CREATE VIEW, CREATE SYNONYM FOR AUXILIAR",
                "GRANT CREATE VIEW, CREATE SYNONYM IN AUXILIAR",
                "GRANT CREATE VIEW TO AUXILIAR");
        addDB(db,
                "Criar um indice chamado IDX_ALUNO para a coluna NMALUNO da tabela ALUNO",
                "CREATE INDEX IDX_ALUNO ON ALUNO (NMALUNO)",
                "CREATE INDEX IDX_ALUNO IN ALUNO (NMALUNO)",
                "CREATE IDX_ALUNO ON ALUNO (NMALUNO)",
                "CREATE INDEX IDX_ALUNO FOR ALUNO (NMALUNO)");
        addDB(db,
                "O que faz o comando EXISTS?",
                "Testa a existência de linhas no conjunto de resultados da sub-consulta",
                "Testa se há tabelas no banco",
                "Testa a sub-consulta para verificar se a mesma funciona corretamente",
                "N.D.A ");
        addDB(db,
                "Para que é utilizado o TABLESPACE",
                "É utilizado para agrupar estruturas lógicas e relacionadas",
                "É o nome dado as espaços não preenchidos da tabela",
                "É uma tabela grande que contém muito espaço para armazenar dados",
                "N.D.A");
        addDB(db,
                "O que tipo de dado passo armazenar numa váriavel tipo RAW em PL/SQL",
                "Dados Binários",
                "Dados String",
                "Dados int",
                "Dados Boolean");
        addDB(db,
                "Qual o comando para apagar a trigger EXPLOSÃO",
                "DROP TRIGGER EXPLOSAO",
                "DROP EXPLOSAO",
                "DROP TRIGGER IN EXPLOSAO",
                "DROP TRIGGER TO EXPLOSAO");
        addDB(db,
                "Qual o comando para recompilar a trigger EXPLOSÃO",
                "ALTER TRIGGER EXPLOSAO COMPILE",
                "TRIGGER EXPLOSAO COMPILE",
                "COMPILE TRIGGER EXPLOSAO",
                "ALTER TRIGGER EXPLOSAO");
        addDB(db,
                "O que significa SQL?",
                " Linguagem de Consulta em Banco de Dados",
                "São Paulo",
                "Sinônimo Query Linguagem",
                "Significa questões de linguagem");
        addDB(db,
                "A linguagem SQL é a mesma em todos os bancos de dados?",
                "Não",
                "Sim",
                "Talvez",
                "Não sei");
        addDB(db,
                "É possível usar SQL em todos os bancos?",
                "Sim, se forem relacionais.",
                "Não! Cada um utiliza uma linguagem própria",
                "Não",
                "Não sei essa tá muito difícil");
        addDB(db,
                "O que é DML?",
                "Linguagem de Manipulação de Dados",
                "Linguagem de Modalidade de Dados",
                "Linguagem de Modularização de Dados",
                "Não sei");
        addDB(db,
                "O que é DDL?",
                "Linguagem de Definição de Dados",
                "Linguagem de Detenção de Dados",
                "Linguagem de Descrição de Dados",
                "Linguagem de Destruição de Dados");
        addDB(db,
                "Os comandos SQL precisam ser escritos em letras maiúsculas?",
                "Não",
                "Sim",
                "Depende",
                "Sempre");
        addDB(db,
                "Comandos SQL podem ser escritos em uma única linha ou precisa ser separado em várias linhas?",
                "Pode ser colocado em várias linhas.",
                "Somente poderá ser escrito em uma única linha",
                "Pode ser colocado em várias linhas desde que no final de cada linha seja colocado o ponto e vírgula",
                "N.D.A");
        addDB(db,
                "Para que serve o ponto-e-vírgula no final das linhas?",
                "Serve para terminar um comando ou instrução SQL",
                "Serve para dividir um comando ou instrução em várias linhas SQL",
                "Não sei",
                "N.D.A");
        addDB(db,
                "Qual dos operadores abaixo não é um operador lógico?",
                "TRUE",
                "AND",
                "OR",
                "NOT");
    }
}
