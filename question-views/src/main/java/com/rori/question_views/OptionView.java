package com.rori.question_views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class OptionView extends CardView {

  private MaterialCardView cardView;
  private TextView txtNumbering;
  private TextView txtOption;
  private ImageView imgTranslate;

  private NumberingStyle numberingStyle = NumberingStyle.NONE;
  private NumberingSeparateCharacter numberingSeparateCharacter = NumberingSeparateCharacter.NONE;

  private int optionTextColor = Color.BLACK;
  private int wrongAnswerBackgroundColor = getResources().getColor(R.color.default_wrong_background_color);
  private int rightAnswerBackgroundColor = getResources().getColor(R.color.default_right_background_color);
  private int checkedStateColor = R.attr.colorPrimary;

  private int optionIndex;

  public OptionView(@NonNull Context context) {
    this(context, null);
  }

  public OptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflate(getContext(), R.layout.option_view, this);

    cardView = findViewById(R.id.card_view);
    txtNumbering = findViewById(R.id.txt_numbering);
    txtOption = findViewById(R.id.txt_option);
    imgTranslate = findViewById(R.id.img_translate);
  }

  public void setOption(int index, String option) {
    this.optionIndex = index;
    txtNumbering.setVisibility(numberingStyle == NumberingStyle.NONE ? GONE : VISIBLE);
    if (numberingStyle == NumberingStyle.BULLETS) {
      txtNumbering.setText(numberingStyle.label);
    } else {
      txtNumbering.setText(String.format("%c%s", numberingStyle.label + index, numberingSeparateCharacter.character));
    }
    if (option != null) txtOption.setText(option);
    cardView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
    txtNumbering.setTextColor(optionTextColor);
    txtOption.setTextColor(optionTextColor);
  }

  public void setRightAnswerBackground() {
    cardView.setBackgroundColor(rightAnswerBackgroundColor);
    txtNumbering.setTextColor(Color.WHITE);
    txtOption.setTextColor(Color.WHITE);
  }

  public void setWrongAnswerBackground() {
    cardView.setBackgroundColor(wrongAnswerBackgroundColor);
    txtNumbering.setTextColor(Color.WHITE);
    txtOption.setTextColor(Color.WHITE);
  }

  public void setChecked(boolean checked) {
    cardView.setChecked(checked);
  }

  public int getOptionIndex() {
    return optionIndex;
  }

  public boolean isChecked() {
    return cardView.isChecked();
  }

  public static class Builder {

    private OptionView optionView;

    public Builder(@NonNull Context context) {
      optionView = new OptionView(context);
    }

    public Builder setNumbering(NumberingStyle numberingStyle) {
      optionView.numberingStyle = numberingStyle;
      return this;
    }

    public Builder setNumberingSeparateCharacter(NumberingSeparateCharacter numberingSeparateCharacter) {
      optionView.numberingSeparateCharacter = numberingSeparateCharacter;
      return this;
    }

    public Builder setOption(String option) {
      optionView.txtOption.setText(option);
      return this;
    }

    public Builder setOptionTextSize(int textSize) {
      optionView.txtOption.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      return this;
    }

    public Builder setOptionTextColor(int textColor) {
      optionView.optionTextColor = textColor;
      return this;
    }

    public Builder setOptionTranslatable(boolean enabled) {
      optionView.imgTranslate.setVisibility(enabled ? VISIBLE : GONE);
      return this;
    }

    public Builder setCheckedStateColor(int color) {
      optionView.checkedStateColor = color;
      return this;
    }

    public Builder setWrongAnswerBackgroundColor(int color) {
      optionView.wrongAnswerBackgroundColor = color;
      return this;
    }

    public Builder setRightAnswerBackgroundColor(int color) {
      optionView.rightAnswerBackgroundColor = color;
      return this;
    }

    public Builder setOnClickListener(final OptionSelectListener listener) {
      optionView.cardView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onOptionSelected(optionView);
        }
      });
      return this;
    }

    public OptionView create() {
      return optionView;
    }
  }

  public enum NumberingStyle {
    NONE('\0'), BULLETS('\u2022'), DIGIT_NUMBER('1'), ROMAN_NUMBER('I'), CHARACTER('a');

    public final Character label;

    NumberingStyle(Character label) {
      this.label = label;
    }
  }

  public enum NumberingSeparateCharacter {
    NONE(""), DOT("."), PARENTHESIS(")");

    public final String character;

    NumberingSeparateCharacter(String character) {
      this.character = character;
    }
  }

  public interface OptionSelectListener {
    void onOptionSelected(OptionView optionView);
  }
}
