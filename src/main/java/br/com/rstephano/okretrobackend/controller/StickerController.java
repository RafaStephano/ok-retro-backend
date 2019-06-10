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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rstephano.okretrobackend.dto.request.StickerCreateRequest;
import br.com.rstephano.okretrobackend.dto.request.StickerUpdateRequest;
import br.com.rstephano.okretrobackend.dto.response.StickerCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.StickerResponse;
import br.com.rstephano.okretrobackend.dto.response.StickerSummaryResponse;
import br.com.rstephano.okretrobackend.entity.Sticker;
import br.com.rstephano.okretrobackend.service.StickerService;

@RestController
@RequestMapping(path = "/sticker", produces = APPLICATION_JSON_VALUE)
public class StickerController {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private StickerService service;

  @PostMapping
  public ResponseEntity<StickerCreateResponse> create(@Valid @RequestBody StickerCreateRequest stickerCreateRequest)
      throws URISyntaxException {
    Sticker sticker = modelMapper.map(stickerCreateRequest, Sticker.class);
    sticker = service.createSticker(sticker);
    StickerCreateResponse response = modelMapper.map(sticker, StickerCreateResponse.class);
    response.add(linkTo(methodOn(StickerController.class).findById(response.getStickerId())).withSelfRel());
    return ResponseEntity.created(new URI(response.getId().expand().getHref())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable String id,
      @Valid @RequestBody StickerUpdateRequest stickerUpdateRequest) throws URISyntaxException {
    Sticker sticker = modelMapper.map(stickerUpdateRequest, Sticker.class);
    sticker.setId(new ObjectId(id));
    service.editSticker(sticker);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StickerResponse> findById(@PathVariable String id) {
    Sticker sticker = service.findStickerById(new ObjectId(id));
    StickerResponse stickerResponse = modelMapper.map(sticker, StickerResponse.class);
    return ResponseEntity.ok().body(stickerResponse);
  }

  @GetMapping
  public ResponseEntity<List<StickerSummaryResponse>> listAll(@RequestParam("idBoard") String idBoard,
      @RequestParam("idColumn") String idColumn) {
    List<StickerSummaryResponse> stickers = service.listAllStickers(new ObjectId(idBoard), new ObjectId(idColumn)) //
        .stream() //
        .map(s -> {
          StickerSummaryResponse sticker = modelMapper.map(s, StickerSummaryResponse.class);
          sticker.add(linkTo(methodOn(StickerController.class).findById(s.getId().toHexString())).withSelfRel());
          return sticker;
        }) //
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(stickers);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {
    service.deleteStickerById(new ObjectId(id));
    return ResponseEntity.ok().body(null);
  }

}
