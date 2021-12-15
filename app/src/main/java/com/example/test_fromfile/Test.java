package com.example.test_fromfile;

import java.util.ArrayList;
import java.util.List;

public class Test {
    List<Question> questions;
    Person person;
    List <Answer> givenAnswers;
    String test_name="";
    int currentQuestion=0;
    public Test(Person person,List <Question> questions)
    {
        givenAnswers=new ArrayList<>();
        this.person=person;
        this.questions=questions;
    }
    public Test(Person person)
    {
        givenAnswers=new ArrayList<>();
        questions=new ArrayList<>();
        this.person= person;
    }
    public void Add_question(Question question)
    {
        questions.add(question);
    }
    public void Delete_question()
    {
        //ToDO доделать удаление вопроса из теста
    }
    public void IsRightAnswered()
    {
        //Todo доделать проверку теста. Если верно, то по индексу вопроса поставить "верно"
    }
    public Question LastQuestion()
    {
        return questions.get(questions.size()-1);
    }
}
