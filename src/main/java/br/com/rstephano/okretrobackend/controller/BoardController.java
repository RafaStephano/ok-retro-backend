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

import br.com.rstephano.okretrobackend.dto.request.BoardCreateRequest;
import br.com.rstephano.okretrobackend.dto.request.BoardUpdateRequest;
import br.com.rstephano.okretrobackend.dto.response.BoardCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.BoardResponse;
import br.com.rstephano.okretrobackend.dto.response.BoardSummaryResponse;
import br.com.rstephano.okretrobackend.entity.Board;
import br.com.rstephano.okretrobackend.service.BoardService;

@RestController
@RequestMapping(path = "/board", produces = APPLICATION_JSON_VALUE)
public class BoardController {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BoardService service;

  @PostMapping
  public ResponseEntity<BoardCreateResponse> create(@Valid @RequestBody BoardCreateRequest boardCreateRequest)
      throws URISyntaxException {
    Board board = modelMapper.map(boardCreateRequest, Board.class);
    board = service.createBoard(board);
    BoardCreateResponse response = modelMapper.map(board, BoardCreateResponse.class);
    response.add(linkTo(methodOn(BoardController.class).findById(response.getBoardId())).withSelfRel());
    return ResponseEntity.created(new URI(response.getId().expand().getHref())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody BoardUpdateRequest boardUpdateRequest)
      throws URISyntaxException {
    Board board = modelMapper.map(boardUpdateRequest, Board.class);
    board.setId(new ObjectId(id));
    service.editBoard(board);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardResponse> findById(@PathVariable String id) {
    Board board = service.findBoardById(new ObjectId(id));
    BoardResponse boardResponse = modelMapper.map(board, BoardResponse.class);
    return ResponseEntity.ok().body(boardResponse);
  }

  @GetMapping
  public ResponseEntity<List<BoardSummaryResponse>> listAll() {
    List<BoardSummaryResponse> boards = service.listAllBoards() //
        .stream() //
        .map(b -> {
          BoardSummaryResponse board = modelMapper.map(b, BoardSummaryResponse.class);
          board.add(linkTo(methodOn(BoardController.class).findById(board.getBoardId())).withSelfRel());
          return board;
        }) //
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(boards);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {
    service.deleteBoardById(new ObjectId(id));
    return ResponseEntity.ok().body(null);
  }

}
