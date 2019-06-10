package br.com.rstephano.okretrobackend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Column {

  @Id
  private ObjectId id;
  private String name;
  private int order;
  private String hexColor;

}
