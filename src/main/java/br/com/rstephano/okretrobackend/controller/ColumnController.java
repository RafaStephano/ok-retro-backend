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

import br.com.rstephano.okretrobackend.dto.request.ColumnCreateRequest;
import br.com.rstephano.okretrobackend.dto.request.ColumnUpdateRequest;
import br.com.rstephano.okretrobackend.dto.response.ColumnCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.ColumnResponse;
import br.com.rstephano.okretrobackend.dto.response.ColumnSummaryResponse;
import br.com.rstephano.okretrobackend.entity.Column;
import br.com.rstephano.okretrobackend.service.BoardService;

@RestController
@RequestMapping(path = "/board/{idBoard}/column", produces = APPLICATION_JSON_VALUE)
public class ColumnController {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BoardService service;

  @PostMapping
  public ResponseEntity<ColumnCreateResponse> create(@PathVariable("idBoard") String idBoard,
      @Valid @RequestBody ColumnCreateRequest columnCreateRequest) throws URISyntaxException {
    Column column = modelMapper.map(columnCreateRequest, Column.class);
    column = service.createColumn(new ObjectId(idBoard), column);
    ColumnCreateResponse response = modelMapper.map(column, ColumnCreateResponse.class);
    response.add(linkTo(methodOn(ColumnController.class).findById(idBoard, response.getColumnId())).withSelfRel());
    return ResponseEntity.created(new URI(response.getId().expand().getHref())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("idBoard") String idBoard, @PathVariable String id,
      @Valid @RequestBody ColumnUpdateRequest columnUpdateRequest) throws URISyntaxException {
    Column column = modelMapper.map(columnUpdateRequest, Column.class);
    column.setId(new ObjectId(id));
    service.editColumn(new ObjectId(idBoard), column);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ColumnResponse> findById(@PathVariable("idBoard") String idBoard, @PathVariable String id) {
    Column column = service.findColumnById(new ObjectId(idBoard), new ObjectId(id));
    ColumnResponse columnResponse = modelMapper.map(column, ColumnResponse.class);
    return ResponseEntity.ok().body(columnResponse);
  }

  @GetMapping
  public ResponseEntity<List<ColumnSummaryResponse>> listAll(@PathVariable("idBoard") String idBoard) {
    List<ColumnSummaryResponse> columns = service.findBoardById(new ObjectId(idBoard)).getColumns() //
        .stream() //
        .map(c -> {
          ColumnSummaryResponse column = modelMapper.map(c, ColumnSummaryResponse.class);
          column.add(linkTo(methodOn(ColumnController.class).findById(idBoard, c.getId().toHexString())).withSelfRel());
          return column;
        }) //
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(columns);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable("idBoard") String idBoard, @PathVariable String id) {
    service.deleteColumnById(new ObjectId(idBoard), new ObjectId(id));
    return ResponseEntity.ok().body(null);
  }

}
