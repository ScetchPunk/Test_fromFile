package com.example.test_fromfile;

import java.util.ArrayList;
import java.util.List;

public class Question {
    List<Answer> answers;
    String question_name;
    public Question(){}
    public Question(String question_name, List<Answer> answers)
    {
        this.answers=answers;
        this.question_name=question_name;
    }
    public Question(String question_name)
    {
        this.question_name=question_name;
        answers=new ArrayList<>();
    }
    public void Add_answer (Answer answer)
    {
        answers.add(answer);
    }
    public void Delete_answer (Answer answer)// Todo проверить алгоритм удаления вопроса по самому вопросу
    {
        Integer counter=0;
        for (Answer ans_check:answers)
        {
            if(ans_check==answer)
                answers.remove(counter);
            counter++;
        }
    }
    public void Delete_answer (Integer number)// Todo проверить алгоритм удаления вопроса по номеру вопроса
    {
        answers.remove(number);
    }
}
