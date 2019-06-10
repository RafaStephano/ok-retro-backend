package br.com.rstephano.okretrobackend.dto.request;

import javax.validation.constraints.Min;
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
public class ColumnCreateRequest {

  @NotBlank
  @NotNull
  private String name;
  @Min(1)
  private int order;
  @NotBlank
  @NotNull
  private String hexColor;

}
