package br.com.rstephano.okretrobackend.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "stickers")
@Data
public class Sticker {

  @Id
  private ObjectId id;
  private ObjectId boardId;
  private ObjectId columnId;
  private ZonedDateTime createdAt;
  private String message;
  private List<Comment> comments;

  public List<Comment> getComments() {
    if (this.comments == null) {
      this.comments = new ArrayList<Comment>();
    }
    return this.comments;
  }

}
