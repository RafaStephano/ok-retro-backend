package br.com.rstephano.okretrobackend.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "boards")
@Data
public class Board {

  @Id
  private ObjectId id;
  private String name;
  private String description;
  private ZonedDateTime createdAt;
  private List<Column> columns;

  public List<Column> getColumns() {
    if (this.columns == null) {
      this.columns = new ArrayList<Column>();
    }
    return this.columns;
  }

}
