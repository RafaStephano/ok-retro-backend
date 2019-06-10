package br.com.rstephano.okretrobackend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.rstephano.okretrobackend.entity.Board;

@Repository
public interface BoardRepository extends MongoRepository<Board, ObjectId> {

}
