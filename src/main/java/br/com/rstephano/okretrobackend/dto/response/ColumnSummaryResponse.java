package br.com.rstephano.okretrobackend.dto.response;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ColumnSummaryResponse extends ResourceSupport {

  @JsonProperty("id")
  private String columnId;
  private String name;
  private int order;
  private String hexColor;

}
