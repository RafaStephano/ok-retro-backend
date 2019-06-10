package br.com.rstephano.okretrobackend.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.rstephano.okretrobackend.entity.Sticker;

@Repository
public interface StickerRepository extends MongoRepository<Sticker, ObjectId> {

  public List<Sticker> findByBoardIdAndColumnId(ObjectId boardId, ObjectId columnId);

}
