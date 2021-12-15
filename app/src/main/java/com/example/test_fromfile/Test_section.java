package com.example.test_fromfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Test_section extends AppCompatActivity {

    public static final String mPath = "угол.txt";
    private QuoteBank mQuoteBank;
    private List<String> mLines;
    TextView txtTestName;
    TextView txtFullName;
    TextView textQuestionField;
    RadioGroup rbgAnswerGroup;
    Button btnNext;
    Button btnBack;
    TextView txtTestCounter;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_section);

        //связываю поля с переменными в этом файле
        txtTestName= (TextView) findViewById(R.id.txtTestName);
        //todo пересмотреть как будет меняться название теста
        txtTestName.setText(mPath.substring(0,this.mPath.length()-4));
        //radiobuttons
        rbgAnswerGroup= (RadioGroup)findViewById(R.id.rbgAnswerGroup);
        rbgAnswerGroup.removeAllViews();
        btnNext=(Button) findViewById(R.id.btnNext);
        btnBack=(Button) findViewById(R.id.btnBack);

        txtFullName= (TextView)findViewById(R.id.txtFullName);
        //подхватываю имя и фамилию тестируемого.
        Person person = new Person(getIntent().getStringExtra("first_name"),getIntent().getStringExtra("last_name"));
        txtFullName.setText(person.last_name+" "+person.first_name);
        textQuestionField=(TextView)findViewById(R.id.textQuestionField);

        //начинаю генерацию теста из файла:
        //1) Читаю тест из файла и размечаю его в классовом файле Test
        mQuoteBank = new QuoteBank(this);//получаю в обладание всю activity
        mLines = mQuoteBank.readLine(mPath);
        //Todo тут кусок кода кривой. Вместо того, чтобы по очереди считывать строку из файла, я должен вычитать весь файл и держать всё одной строкой. Это опасно.
        Test test = new Test(person);//создаю инстанс теста с человеком.
        //Начинаю генерацию теста из файла
        for (String string:mLines)
        {
            if (!string.startsWith("   "))
            {
                //создаю вопрос
                Question question = new Question(string);
                test.Add_question(question);
            }
            else
            {
                //создаю ответ
                Answer answer= new Answer(string);
                //подцепляю ответ к уже существующему последнему вопросу
                // todo ОБЯЗАТЕЛЬНО НУЖНО ПРОСЛЕДИТЬ, ЧТОБЫ Я ТУТ ОТСЛЕДИЛ, ПОПАЛСЯ ЛИ ПУСТОЙ ФАЙЛ ИЛИ НЕВЕРНО ОФОРМЛЕННЫЙ ФАЙЛ. КРИТИЧЕСКАЯ ОШИБКА
                test.LastQuestion().Add_answer(answer);
            }
        }
        test.test_name=mPath.substring(0,this.mPath.length()-4);
        test.currentQuestion=0;
        txtTestCounter=(TextView) findViewById(R.id.txtTestCounter);

        //если ничего не сломалось:
        //2) начинаю изменять activity с учётом выбранного теста.

        updateQuestion(test,0);

        //вставляю данные из сборного "теста"
        // здесь заканчивается генерация ответов
        // создаю ивент на кнопку (след.вопрос)
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //проверка вопроса.
                //проверка на выбранность варианта ответа
                if (rbgAnswerGroup.getCheckedRadioButtonId()==-1)
                {
                    //no radio buttons are checked
                    Toast.makeText(context,"Вы не указали ответ!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Если он/она ответил(-а) - записать в файл
                    //int selectedId=rbgAnswerGroup.getCheckedRadioButtonId();//нахождение верного ответа
                    //RadioButton selectedRadioButton= (RadioButton) findViewById(selectedId);
                    //if (test.questions.get(number_of_question).answers.get(rbgAnswerGroup.f))

                    // Запись в файл
                    //"перелистываем" вопрос
                    //эта заглушка даёт понять, выбран ли был вообще хоть что-то.
                    String str="";
                    RadioButton radioButton= (RadioButton) findViewById(rbgAnswerGroup.getCheckedRadioButtonId());

                    Toast.makeText(context,radioButton.getText() ,Toast.LENGTH_SHORT).show();
                    test.currentQuestion++;
                    updateQuestion(test,test.currentQuestion);
                }

            }
        });
        // создаю ивент на кнопку (предыд.вопрос)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test.currentQuestion !=0)
                {
                    if (rbgAnswerGroup.getCheckedRadioButtonId()==-1)
                    {
                        //no radio buttons are checked
                        Toast.makeText(context,"Вы не указали ответ!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //"отлистываем" назад тест (если, конечно, вопрос не самый первый.
                        //затем выбираем вариант ответа(ов), которые были выбраны.
                        test.currentQuestion--;
                        updateQuestion(test,test.currentQuestion);
                    }
                }
            }
        });
    }

    public void updateQuestion(Test test, int currentQuestionNumber)
    {
        //эта функция обновляет тест при изменении номера вопроса (либо +, либо -).
        //ему скармливают номер вопроса
        //он
        //1) Выбирает вопрос и связанные с ним ответы
        //2) изменяет Activity
        //3) уведомляет, был ли это вопрос

        txtTestCounter.setText((Object)(currentQuestionNumber+1)+"/"+test.questions.size());
        //косметика для кнопочки вперёд и назад и программирование перехода на новый активити.
        if (currentQuestionNumber==0)
        {
            btnBack.setVisibility(View.INVISIBLE);
            btnNext.setText("Вперёд");
        }
        else if (currentQuestionNumber==test.questions.size()-1)
        {
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setText("Завершить");
        }
        else if(currentQuestionNumber==test.questions.size())
        {
            //Todo сделать подсчёт теста и вывести результаты
            Intent intent = new Intent(Test_section.this, test_result.class);
            startActivity(intent);
            return;
        }
        else
        {
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setText("Вперёд");
        }

        //вставляю данные из сборного "теста"
        //todo добавить отслеживание на форме номера вопроса
        textQuestionField.setText((Object)(currentQuestionNumber+1)+". "+test.questions.get(currentQuestionNumber).question_name);
        rbgAnswerGroup.removeAllViews();
        rbgAnswerGroup.clearCheck();
        //заранее добавим счётчик для теста
        int counter=0;
        for (Answer answer:test.questions.get(currentQuestionNumber).answers)
        {
            //добавляю поля выбора ответов
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(answer.answer);
            radioButton.setId(counter);//set radiobutton id and store it somewhere
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            rbgAnswerGroup.addView(radioButton, params);
            counter++;
        }
    }
    public void create_testFile(Test test)
    {
        try {
            File root = new File(Environment.getExternalStorageDirectory(),"Test_folder");// create folder for file on external storage
            if (!root.exists())
            {
                root.mkdir();
            }
            File filepath= new File(root,test.person.first_name+"_"+test.person.last_name+".res");
            FileWriter writer = new FileWriter(filepath);//open for writing
            writer.append(test.test_name+"\n");
            writer.write(test.person.last_name+" "+test.person.first_name+"\n");
            writer.flush();
            writer.close(); // this will create the file, but file name will be the same

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void update_testFile(File file, Test test)
    {
        if (!file.exists())
            create_testFile(test);
        else
        try {
            File root = new File(Environment.getExternalStorageDirectory(),"Test_folder");// create folder for file on external storage
            if (!root.exists())
            {
                root.mkdir();
            }
            String filename=test.person.first_name+"_"+test.person.last_name+".res";

            File filepath= new File(root,filename);

                FileWriter writer = new FileWriter(filepath);//open for writing
            //доработать запись выбранного ответа в файл.
            //for (Answer answer:
            //     ) {
//
//            }
//                writer.write();//Название вопроса, на который даётся ответ
  //              writer.write();//полученный ответ(-ы)
  //              writer.flush();
  //              writer.close(); // this will create the file, but file name will be the same


        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

