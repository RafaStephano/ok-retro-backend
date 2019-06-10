package br.com.rstephano.okretrobackend.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StickerCreateRequest {

  @NotBlank
  @NotNull
  private String boardId;
  @NotBlank
  @NotNull
  private String columnId;
  @NotBlank
  @NotNull
  private String message;

}
