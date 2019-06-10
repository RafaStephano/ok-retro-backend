package br.com.rstephano.okretrobackend.exceptions;

import static java.text.MessageFormat.format;

public class ColumnNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1127474978423538530L;
  private static final String messageTemplate = "Column not found with boardId: {0} and columnId: {1}";

  public ColumnNotFoundException(String boardId, String columnId) {
    super(format(messageTemplate, boardId, columnId));
  }

}
