package br.com.rstephano.okretrobackend.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rstephano.okretrobackend.entity.Comment;
import br.com.rstephano.okretrobackend.entity.Sticker;
import br.com.rstephano.okretrobackend.exceptions.CommentNotFoundException;
import br.com.rstephano.okretrobackend.exceptions.StickerNotFoundException;
import br.com.rstephano.okretrobackend.repository.StickerRepository;

@Service
public class StickerService {

  @Autowired
  private StickerRepository stickerRepository;

  public List<Sticker> listAllStickers(ObjectId boardId, ObjectId columnId) {
    return stickerRepository.findByBoardIdAndColumnId(boardId, columnId);
  }

  public Sticker findStickerById(ObjectId stickerId) {
    return stickerRepository.findById(stickerId) //
        .orElseThrow(() -> new StickerNotFoundException(stickerId.toHexString()));
  }

  public Comment findCommentById(ObjectId stickerId, ObjectId commentId) {
    return findStickerById(stickerId) //
        .getComments() //
        .stream() //
        .filter(c -> c.getId().equals(commentId)) //
        .findFirst() //
        .orElseThrow(() -> new CommentNotFoundException(stickerId.toHexString(), commentId.toHexString()));
  }

  public Sticker createSticker(Sticker sticker) {
    sticker.setId(new ObjectId());
    sticker.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
    return stickerRepository.save(sticker);
  }

  public Comment createComment(ObjectId stickerId, Comment comment) {
    Sticker sticker = findStickerById(stickerId);
    comment.setId(new ObjectId());
    comment.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
    sticker.getComments().add(comment);
    stickerRepository.save(sticker);
    return comment;
  }

  public void editSticker(Sticker editedSticker) {
    Sticker sticker = findStickerById(editedSticker.getId());
    sticker.setColumnId(editedSticker.getColumnId());
    sticker.setMessage(editedSticker.getMessage());
    stickerRepository.save(sticker);
  }

  public void editComment(ObjectId stickerId, Comment editedComment) {
    Sticker sticker = findStickerById(stickerId);
    sticker.getComments() //
        .stream() //
        .filter(c -> c.getId().equals(editedComment.getId())) //
        .peek(c -> {
          c.setMessage(editedComment.getMessage());
        }) //
        .findFirst() //
        .orElseThrow(() -> new CommentNotFoundException(stickerId.toHexString(), editedComment.getId().toHexString()));
    stickerRepository.save(sticker);
  }

  public void deleteStickerById(ObjectId stickerId) {
    findStickerById(stickerId);
    stickerRepository.deleteById(stickerId);
  }

  public void deleteCommentById(ObjectId stickerId, ObjectId commentId) {
    Sticker sticker = findStickerById(stickerId);
    findCommentById(stickerId, commentId);
    List<Comment> comments = sticker.getComments() //
        .stream() //
        .filter(c -> !c.getId().equals(commentId)) //
        .collect(Collectors.toList());
    sticker.setComments(comments);
    stickerRepository.save(sticker);
  }

}
