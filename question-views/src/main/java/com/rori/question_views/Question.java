package com.rori.question_views;

import java.util.List;

public class Question {

  private String content;

  private String contentTranslated;

  private List<Option> options;

  private int answerIndex;

  private int selectedIndex = -1;

  public Question() {
  }

  public Question(String content, List<Option> options, int answerIndex) {
    this(content, null, options, answerIndex);
  }

  public Question(String content, String contentTranslated, List<Option> options, int answerIndex) {
    this.content = content;
    this.contentTranslated = contentTranslated;
    this.options = options;
    if (answerIndex < options.size()) {
      this.answerIndex = answerIndex;
    }
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContentTranslated() {
    return contentTranslated;
  }

  public void setContentTranslated(String contentTranslated) {
    this.contentTranslated = contentTranslated;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  public int getAnswerIndex() {
    return answerIndex;
  }

  public void setAnswerIndex(int answerIndex) {
    this.answerIndex = answerIndex;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
  }
}
