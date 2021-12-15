package com.example.test_fromfile;

public class Answer {
    String answer="";
    boolean isRight=false;
    public Answer() {}
    public Answer(String answer)
    {
        this.answer=answer.trim();//Todo проверить лидирующие пробелы
        if (this.answer.contains("+++"))//Todo проверить, находит ли ответы
        {
            isRight=true;
        }
        this.answer=this.answer.substring(0,this.answer.length()-3);//ToDo проверить, обрезает ли ответы.
    }
}