package com.rori.question_views;

public class Option {

  private String content;
  private String contentTranslated;
  private String explanation;

  public Option() {
  }

  public Option(String content) {
    this.content = content;
  }

  public Option(String content, String contentTranslated) {
    this.content = content;
    this.contentTranslated = contentTranslated;
  }

  public Option(String content, String contentTranslated, String explanation) {
    this.content = content;
    this.contentTranslated = contentTranslated;
    this.explanation = explanation;
  }

  public String getContent() {
    return content;
  }

  public String getContentTranslated() {
    return contentTranslated;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setContentTranslated(String contentTranslated) {
    this.contentTranslated = contentTranslated;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }
}
