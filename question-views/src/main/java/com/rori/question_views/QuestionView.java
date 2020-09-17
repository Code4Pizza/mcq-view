package com.rori.question_views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionView extends LinearLayout {

  private static final String FORMAT_NUMBER_IN_TOTAL = "%d / %d";
  private static final int DEFAULT_OPTION_NUMBERS = 4;

  private TextView txtQuestion;
  private TextView txtNumberInTotal;
  private TextView txtTranslated;
  private LinearLayout layoutOptions;
  private MaterialButton btnNext;
  private MaterialButton btnBack;
  private Space space;

  private boolean requireAllSelectedBeforeSubmit;
  private String requireAllSelectedMessage;
  private int currentQuestionIndex;
  private List<Question> questions = new ArrayList<>();
  private boolean showResults;
  private int grade;

  private List<OptionView> optionViews = new ArrayList<>();

  private OnSubmitListener onSubmitListener;

  public QuestionView(@NonNull Context context) {
    this(context, null);
  }

  public QuestionView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public QuestionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public QuestionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    inflate(context, R.layout.question_view, this);

    txtQuestion = findViewById(R.id.txt_question);
    txtNumberInTotal = findViewById(R.id.txt_number_in_total);
    txtTranslated = findViewById(R.id.txt_translate);
    ImageView imgTranslate = findViewById(R.id.img_translate);
    layoutOptions = findViewById(R.id.layout_options);
    btnNext = findViewById(R.id.btn_next);
    btnBack = findViewById(R.id.btn_back);
    space = findViewById(R.id.space);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuestionView);
    boolean showNumberInTotal = typedArray.getBoolean(R.styleable.QuestionView_qvShowNumberInTotal, false);
    txtNumberInTotal.setVisibility(showNumberInTotal ? VISIBLE : GONE);
    String question = typedArray.getString(R.styleable.QuestionView_qvQuestion);
    if (question != null) txtQuestion.setText(question);
    int questionTextColor = typedArray.getColor(R.styleable.QuestionView_qvQuestionTextColor, Color.BLACK);
    txtQuestion.setTextColor(questionTextColor);
    int questionTextSize = typedArray.getDimensionPixelSize(R.styleable.QuestionView_qvQuestionTextSize, getResources().getDimensionPixelSize(R.dimen.default_text_size));
    txtQuestion.setTextSize(TypedValue.COMPLEX_UNIT_PX, questionTextSize);
    boolean questionTranslatable = typedArray.getBoolean(R.styleable.QuestionView_qvQuestionTranslatable, false);
    imgTranslate.setVisibility(questionTranslatable ? VISIBLE : GONE);
    Drawable iconTranslate = typedArray.getDrawable(R.styleable.QuestionView_qvQuestionTranslatableIcon);
    if (iconTranslate != null) imgTranslate.setImageDrawable(iconTranslate);

    OptionView.NumberingStyle numberingStyle = OptionView.NumberingStyle.values()[typedArray.getInt(R.styleable.QuestionView_qvOptionNumberingStyle, 0)];
    OptionView.NumberingSeparateCharacter numberingSeparateCharacter = OptionView.NumberingSeparateCharacter.values()[typedArray.getInt(R.styleable.QuestionView_qvOptionNumberingSeparateCharacter, 0)];
    int optionTextColor = typedArray.getColor(R.styleable.QuestionView_qvOptionTextColor, Color.BLACK);
    int optionTextSize = typedArray.getDimensionPixelSize(R.styleable.QuestionView_qvQuestionTextSize, getResources().getDimensionPixelSize(R.dimen.default_text_size));
    boolean optionTranslatable = typedArray.getBoolean(R.styleable.QuestionView_qvOptionTranslatable, false);

    int wrongAnswerBackgroundColor = typedArray.getColor(R.styleable.QuestionView_qvWrongAnswerBackgroundColor, ContextCompat.getColor(context, R.color.default_wrong_background_color));
    int rightAnswerBackgroundColor = typedArray.getColor(R.styleable.QuestionView_qvRightAnswerBackgroundColor, ContextCompat.getColor(context, R.color.default_right_background_color));
    int checkedStateColor = typedArray.getColor(R.styleable.QuestionView_qvCheckedStateColor, R.attr.colorPrimary);

    requireAllSelectedBeforeSubmit = typedArray.getBoolean(R.styleable.QuestionView_qvRequireAllSelectedBeforeSubmit, true);
    if (typedArray.hasValue(R.styleable.QuestionView_qvRequireAllSelectedMessage)) {
      requireAllSelectedMessage = typedArray.getString(R.styleable.QuestionView_qvRequireAllSelectedMessage);
    } else {
      requireAllSelectedMessage = getContext().getString(R.string.require_all_selected_message);
    }

    typedArray.recycle();

    for (int i = 0; i < DEFAULT_OPTION_NUMBERS; i++) {
      OptionView optionView = new OptionView.Builder(context)
              .setNumbering(numberingStyle)
              .setNumberingSeparateCharacter(numberingSeparateCharacter)
              .setOptionTextColor(optionTextColor)
              .setOptionTextSize(optionTextSize)
              .setOptionTranslatable(optionTranslatable)
              .setRightAnswerBackgroundColor(rightAnswerBackgroundColor)
              .setWrongAnswerBackgroundColor(wrongAnswerBackgroundColor)
              .setCheckedStateColor(checkedStateColor)
              .setOnClickListener(new OptionView.OptionSelectListener() {
                @Override
                public void onOptionSelected(OptionView optionView) {
                  if (showResults) {
                    return;
                  }
                  optionView.setChecked(!optionView.isChecked());
                  for (OptionView ov : optionViews) {
                    if (!ov.equals(optionView)) {
                      ov.setChecked(false);
                    }
                  }
                  Question currentQuestion = questions.get(currentQuestionIndex);
                  if (optionView.isChecked()) {
                    currentQuestion.setSelectedIndex(optionView.getOptionIndex());
                  } else {
                    currentQuestion.setSelectedIndex(-1);
                  }
                }
              }).create();
      optionViews.add(optionView);
    }

    for (int index = 0; index < optionViews.size(); index++) {
      OptionView optionView = optionViews.get(index);
      optionView.setOption(index, null);
      layoutOptions.addView(optionView);
      Space space = new Space(context);
      space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.default_option_space)));
      layoutOptions.addView(space);
    }

    btnNext.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        currentQuestionIndex++;
        updateCurrentQuestionView();
      }
    });

    btnBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        currentQuestionIndex--;
        updateCurrentQuestionView();
      }
    });

    imgTranslate.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        txtTranslated.setVisibility(txtTranslated.getVisibility() == VISIBLE ? GONE : VISIBLE);
      }
    });
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
    this.currentQuestionIndex = 0;
    updateCurrentQuestionView();
  }

  private void updateCurrentQuestionView() {
    if (questions.size() <= currentQuestionIndex) return;
    updateCurrentNumberInTotal();
    Question question = questions.get(currentQuestionIndex);
    txtQuestion.setText(question.getContent());

    layoutOptions.removeAllViews();
    for (int index = 0; index < question.getOptions().size(); index++) {
      if (index > optionViews.size()) {
        break;
      }
      OptionView optionView = optionViews.get(index);
      optionView.setOption(index, question.getOptions().get(index).getContent());
      if (question.getSelectedIndex() == index) {
        if (showResults && index != question.getAnswerIndex()) {
          optionView.setChecked(false);
          optionView.setWrongAnswerBackground();
        } else {
          optionView.setChecked(true);
        }
      } else {
        optionView.setChecked(false);
      }
      if (showResults && question.getAnswerIndex() == index) {
        optionView.setRightAnswerBackground();
      }
      layoutOptions.addView(optionView);
      Space space = new Space(getContext());
      space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.default_option_space)));
      layoutOptions.addView(space);
    }

    if (questions.size() <= 1) {
      btnNext.setVisibility(GONE);
      btnBack.setVisibility(GONE);
      space.setVisibility(GONE);
    } else if (currentQuestionIndex == 0) {
      btnNext.setVisibility(VISIBLE);
      btnBack.setVisibility(GONE);
      space.setVisibility(GONE);
    } else if (currentQuestionIndex == (questions.size() - 1)) {
      btnNext.setVisibility(GONE);
      btnBack.setVisibility(VISIBLE);
      space.setVisibility(GONE);
    } else {
      btnNext.setVisibility(VISIBLE);
      btnBack.setVisibility(VISIBLE);
      space.setVisibility(VISIBLE);
    }
  }

  private void updateCurrentNumberInTotal() {
    String numberInTotal = String.format(Locale.getDefault(), FORMAT_NUMBER_IN_TOTAL, currentQuestionIndex + 1, questions.size());
    int splashIndex = numberInTotal.indexOf("/");
    SpannableString spannableString = new SpannableString(numberInTotal);
    spannableString.setSpan(new RelativeSizeSpan(1.3f), 0, splashIndex, 0);
    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.default_text_color)), splashIndex, numberInTotal.length(), 0);
    txtNumberInTotal.setText(spannableString);
  }

  public void showResultView() {
    if (showResults) {
      return;
    }

    grade = 0;
    for (int index = 0; index < questions.size(); index++) {
      Question question = questions.get(index);
      int selectedIndex = question.getSelectedIndex();
      if (selectedIndex == -1 && requireAllSelectedBeforeSubmit) {
        currentQuestionIndex = index;
        updateCurrentQuestionView();
        Toast.makeText(getContext(), requireAllSelectedMessage, Toast.LENGTH_SHORT).show();
        return;
      }
      if (selectedIndex == question.getAnswerIndex()) {
        grade++;
      }
    }

    new AlertDialog.Builder(getContext())
            .setMessage("Result " + grade + "/" + questions.size())
            .setCancelable(false)
            .setPositiveButton(R.string.action_finish_test, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (onSubmitListener != null) onSubmitListener.onClickFinishTest(grade);
              }
            })
            .setNeutralButton(R.string.action_show_results, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (onSubmitListener != null) onSubmitListener.onClickShowResult(grade);
                showResults = true;
                updateCurrentQuestionView();
              }
            }).show();
  }

  public void submit() {
    showResultView();
  }

  public void submit(boolean requireAllSelectedBeforeSubmit, String requireAllSelectedMessage) {
    this.requireAllSelectedBeforeSubmit = requireAllSelectedBeforeSubmit;
    this.requireAllSelectedMessage = requireAllSelectedMessage;
    showResultView();
  }

  public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
    this.onSubmitListener = onSubmitListener;
  }

  public boolean isFinished() {
    return showResults;
  }

  public interface OnSubmitListener {
    void onClickFinishTest(int grade);

    void onClickShowResult(int grade);
  }
}
