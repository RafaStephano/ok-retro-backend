package br.com.rstephano.okretrobackend.exceptions;

import static java.text.MessageFormat.format;

public class BoardNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1386356873326693743L;
  private static final String messageTemplate = "Board not found with boardId: {0}";

  public BoardNotFoundException(String boardId) {
    super(format(messageTemplate, boardId));
  }

}
