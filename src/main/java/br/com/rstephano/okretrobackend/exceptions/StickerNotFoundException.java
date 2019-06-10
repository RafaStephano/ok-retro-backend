package br.com.rstephano.okretrobackend.exceptions;

import static java.text.MessageFormat.format;

public class StickerNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4993736148369333837L;
  private static final String messageTemplate = "Sticker not found with stickerId: {0}";

  public StickerNotFoundException(String stickerId) {
    super(format(messageTemplate, stickerId));
  }

}
