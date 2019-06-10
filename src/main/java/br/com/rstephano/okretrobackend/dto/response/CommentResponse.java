package br.com.rstephano.okretrobackend.dto.response;

import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CommentResponse {

  private String id;
  private ZonedDateTime createdAt;
  private String message;

}
