package br.com.rstephano.okretrobackend.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rstephano.okretrobackend.dto.request.CommentCreateRequest;
import br.com.rstephano.okretrobackend.dto.request.CommentUpdateRequest;
import br.com.rstephano.okretrobackend.dto.response.CommentCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.CommentResponse;
import br.com.rstephano.okretrobackend.dto.response.CommentSummaryResponse;
import br.com.rstephano.okretrobackend.entity.Comment;
import br.com.rstephano.okretrobackend.service.StickerService;

@RestController
@RequestMapping(path = "/sticker/{idSticker}/comment", produces = APPLICATION_JSON_VALUE)
public class CommentController {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private StickerService service;

  @PostMapping
  public ResponseEntity<CommentCreateResponse> create(@PathVariable("idSticker") String idSticker,
      @Valid @RequestBody CommentCreateRequest commentCreateRequest) throws URISyntaxException {
    Comment comment = modelMapper.map(commentCreateRequest, Comment.class);
    comment = service.createComment(new ObjectId(idSticker), comment);
    CommentCreateResponse response = modelMapper.map(comment, CommentCreateResponse.class);
    response.add(linkTo(methodOn(CommentController.class).findById(idSticker, response.getCommentId())).withSelfRel());
    return ResponseEntity.created(new URI(response.getId().expand().getHref())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("idSticker") String idSticker, @PathVariable String id,
      @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) throws URISyntaxException {
    Comment comment = modelMapper.map(commentUpdateRequest, Comment.class);
    comment.setId(new ObjectId(id));
    service.editComment(new ObjectId(idSticker), comment);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> findById(@PathVariable("idSticker") String idSticker,
      @PathVariable String id) {
    Comment comment = service.findCommentById(new ObjectId(idSticker), new ObjectId(id));
    CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
    return ResponseEntity.ok().body(commentResponse);
  }

  @GetMapping
  public ResponseEntity<List<CommentSummaryResponse>> listAll(@PathVariable("idSticker") String idSticker) {
    List<CommentSummaryResponse> comments = service.findStickerById(new ObjectId(idSticker)).getComments() //
        .stream() //
        .map(s -> {
          CommentSummaryResponse comment = modelMapper.map(s, CommentSummaryResponse.class);
          comment.add(
              linkTo(methodOn(CommentController.class).findById(idSticker, s.getId().toHexString())).withSelfRel());
          return comment;
        }) //
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(comments);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable("idSticker") String idSticker, @PathVariable String id) {
    service.deleteCommentById(new ObjectId(idSticker), new ObjectId(id));
    return ResponseEntity.ok().body(null);
  }

}
