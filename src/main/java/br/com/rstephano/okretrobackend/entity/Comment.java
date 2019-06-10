package br.com.rstephano.okretrobackend.entity;

import java.time.ZonedDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Comment {

  @Id
  private ObjectId id;
  private ZonedDateTime createdAt;
  private String message;

}
