package br.com.rstephano.okretrobackend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ColumnResponse {

  private String id;
  private String name;
  private int order;
  private String hexColor;

}
