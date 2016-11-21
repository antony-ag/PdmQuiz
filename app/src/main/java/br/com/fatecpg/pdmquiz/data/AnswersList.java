package br.com.fatecpg.pdmquiz.data;

import java.util.ArrayList;

/**
 * Created by tony on 19/11/16.
 */

public class AnswersList {
    protected ArrayList<String> id = new ArrayList<>();
    protected ArrayList<String> question = new ArrayList<>();
    protected ArrayList<String> answer = new ArrayList<>();
    protected ArrayList<String> userAnswer = new ArrayList<>();

    public int getPoints(){
        int points = 0;
        if(question.size()>0)
            for(int i = 0; i < question.size()-1; i++)
                if(userAnswer.get(i).equals(answer.get(i)))
                    points++;
        return points;
    }

    public void add(String question, String answer, String userAnswer){
        this.id.add("");
        this.question.add(question);
        this.answer.add(answer);
        this.userAnswer.add(userAnswer);
    }

    public int size(){ return question.size(); }

}
