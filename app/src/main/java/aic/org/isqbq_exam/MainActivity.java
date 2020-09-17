package aic.org.isqbq_exam;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rori.question_views.Option;
import com.rori.question_views.Question;
import com.rori.question_views.QuestionView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private QuestionView questionView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

     questionView = findViewById(R.id.question_view);

    questionView.setQuestions(new ArrayList<Question>() {{
      add(new Question(
              "Which of the following is true about White and Black Box Testing Technique",
              new ArrayList<Option>() {{
                add(new Option("Equivalance partitioning, Decision Table and Control flow are White box Testing Techniques"));
                add(new Option("Equivalence partitioning , Boundary Value Analysis , Data Flow are Black Box Testing Techniques"));
                add(new Option("Equivalence partitioning , State Transition , Use Case Testing are black box Testing Techniques"));
                add(new Option("Equivalence Partioning , State Transition , Use Case Testing and Decision Table are White Box Testing Techniques"));
              }},
              0
      ));
      add(new Question(
              "The structure of an incident report is covered in the Standard for Software Test Documentation IEEE 829 and is called as",
              new ArrayList<Option>() {{
                add(new Option("Anomaly Report"));
                add(new Option("Defect Report"));
                add(new Option("Test Defect Report"));
                add(new Option("Test Incident Report"));
              }},
              2
      ));
      add(new Question(
              "Which of the following is a part of Test Closure Activities?\n" +
                      "i. Checking which planned deliverables have been delivered\n" +
                      "ii. Defect report analysis.\n" +
                      "iii. Finalizing and archivi",
              new ArrayList<Option>() {{
                add(new Option("i , ii , iv are true and iii is false"));
                add(new Option("i , ii , iii are true and iv is false"));
                add(new Option("i , iii , iv are true and ii is false"));
                add(new Option("All of above are true"));
              }},
              3
      ));
    }});
  }

  public void submit(View view) {
    questionView.submit();
  }
}