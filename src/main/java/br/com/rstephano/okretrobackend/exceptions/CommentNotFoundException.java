package br.com.rstephano.okretrobackend.exceptions;

import static java.text.MessageFormat.format;

public class CommentNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1383610629679735111L;
  private static final String messageTemplate = "Comment not found with stickerId: {0} and commentId: {1}";

  public CommentNotFoundException(String stickerId, String commentId) {
    super(format(messageTemplate, stickerId, commentId));
  }

}
