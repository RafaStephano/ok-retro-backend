package br.com.rstephano.okretrobackend;

import org.bson.types.ObjectId;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.rstephano.okretrobackend.dto.request.StickerCreateRequest;
import br.com.rstephano.okretrobackend.dto.request.StickerUpdateRequest;
import br.com.rstephano.okretrobackend.dto.response.BoardCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.BoardSummaryResponse;
import br.com.rstephano.okretrobackend.dto.response.ColumnCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.ColumnSummaryResponse;
import br.com.rstephano.okretrobackend.dto.response.CommentCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.CommentSummaryResponse;
import br.com.rstephano.okretrobackend.dto.response.StickerCreateResponse;
import br.com.rstephano.okretrobackend.dto.response.StickerSummaryResponse;
import br.com.rstephano.okretrobackend.entity.Board;
import br.com.rstephano.okretrobackend.entity.Column;
import br.com.rstephano.okretrobackend.entity.Comment;
import br.com.rstephano.okretrobackend.entity.Sticker;
import br.com.rstephano.okretrobackend.repository.BoardRepository;
import br.com.rstephano.okretrobackend.service.BoardService;
import br.com.rstephano.okretrobackend.service.StickerService;

@SpringBootApplication
public class OkRetroBackendApplication implements CommandLineRunner {

  @Autowired
  private BoardService boardService;

  @Autowired
  private StickerService stickerService;

  @Autowired
  private BoardRepository boardRepository;

  public static void main(String[] args) {
    SpringApplication.run(OkRetroBackendApplication.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    Converter<String, ObjectId> stringToObjectIdConverter = ctx -> new ObjectId(ctx.getSource());

    ModelMapper modelMapper = new ModelMapper();
    modelMapper //
        .createTypeMap(Board.class, BoardCreateResponse.class) //
        .addMappings(mapper -> mapper.map(Board::getId, BoardCreateResponse::setBoardId));
    modelMapper //
        .createTypeMap(Board.class, BoardSummaryResponse.class) //
        .addMappings(mapper -> mapper.map(Board::getId, BoardSummaryResponse::setBoardId));
    modelMapper //
        .createTypeMap(Column.class, ColumnCreateResponse.class) //
        .addMappings(mapper -> mapper.map(Column::getId, ColumnCreateResponse::setColumnId));
    modelMapper //
        .createTypeMap(Column.class, ColumnSummaryResponse.class) //
        .addMappings(mapper -> mapper.map(Column::getId, ColumnSummaryResponse::setColumnId));
    modelMapper //
        .createTypeMap(StickerCreateRequest.class, Sticker.class) //
        .addMappings(mapper -> {
          mapper.using(stringToObjectIdConverter).map(StickerCreateRequest::getBoardId, Sticker::setBoardId);
          mapper.using(stringToObjectIdConverter).map(StickerCreateRequest::getColumnId, Sticker::setColumnId);
        });
    modelMapper //
        .createTypeMap(StickerUpdateRequest.class, Sticker.class) //
        .addMappings(mapper -> {
          mapper.using(stringToObjectIdConverter).map(StickerUpdateRequest::getBoardId, Sticker::setBoardId);
          mapper.using(stringToObjectIdConverter).map(StickerUpdateRequest::getColumnId, Sticker::setColumnId);
        });
    modelMapper //
        .createTypeMap(Sticker.class, StickerCreateResponse.class) //
        .addMappings(mapper -> mapper.map(Sticker::getId, StickerCreateResponse::setStickerId));
    modelMapper //
        .createTypeMap(Sticker.class, StickerSummaryResponse.class) //
        .addMappings(mapper -> mapper.map(Sticker::getId, StickerSummaryResponse::setStickerId));
    modelMapper //
        .createTypeMap(Comment.class, CommentCreateResponse.class) //
        .addMappings(mapper -> mapper.map(Comment::getId, CommentCreateResponse::setCommentId));
    modelMapper //
        .createTypeMap(Comment.class, CommentSummaryResponse.class) //
        .addMappings(mapper -> mapper.map(Comment::getId, CommentSummaryResponse::setCommentId));
    return modelMapper;

  }

  @Override
  public void run(String... args) throws Exception {
    if (boardRepository.count() == -5) {
      for (int i = 0; i < 10; i++) {
        createBoard(i);
      }
    }
  }

  private void createBoard(int i) {
    Board board0 = new Board();
    board0.setName("Teste Board " + i);
    Board savedBoard = boardService.createBoard(board0);

    Board board = boardService.findBoardById(savedBoard.getId());

    Column column0 = new Column();
    column0.setName("Teste Column 0 - " + i);
    column0.setOrder(0);
    boardService.createColumn(board.getId(), column0);

    Column column1 = new Column();
    column1.setName("Teste Column 1 - " + i);
    column1.setOrder(1);
    boardService.createColumn(board.getId(), column1);

    Column column2 = new Column();
    column2.setName("Teste Column 2 - " + i);
    column2.setOrder(2);
    boardService.createColumn(board.getId(), column2);

    board = boardService.findBoardById(savedBoard.getId());

    Column editedColumn = board.getColumns().get(2);
    editedColumn.setName("Teste Column 2 - " + i + " - Edited");
    editedColumn.setHexColor("#FFFFFF");
    boardService.editColumn(board.getId(), editedColumn);

    Column column3 = new Column();
    column3.setName("Teste Column 3 - " + i);
    column3.setOrder(3);
    Column createdColumn = boardService.createColumn(board.getId(), column3);

    boardService.deleteColumnById(board.getId(), createdColumn.getId());

    try {
      boardService.deleteBoardById(new ObjectId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      boardService.deleteColumnById(board.getId(), new ObjectId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    board = boardService.findBoardById(savedBoard.getId());

    Sticker sticker0 = new Sticker();
    sticker0.setBoardId(board.getId());
    sticker0.setColumnId(board.getColumns().get(0).getId());
    sticker0.setMessage("Sticker message 0 - " + i);
    stickerService.createSticker(sticker0);

    Sticker sticker1 = new Sticker();
    sticker1.setBoardId(board.getId());
    sticker1.setColumnId(board.getColumns().get(0).getId());
    sticker1.setMessage("Sticker message 1 - " + i);
    Sticker createdSticker = stickerService.createSticker(sticker1);

    Sticker sticker2 = new Sticker();
    sticker2.setBoardId(board.getId());
    sticker2.setColumnId(board.getColumns().get(0).getId());
    sticker2.setMessage("Sticker message 2 - " + i);
    stickerService.createSticker(sticker2);

    Sticker sticker = stickerService.findStickerById(createdSticker.getId());

    Comment comment0 = new Comment();
    comment0.setMessage("Comment 0 - " + i);
    stickerService.createComment(sticker.getId(), comment0);

    Comment comment1 = new Comment();
    comment1.setMessage("Comment 1 - " + i);
    stickerService.createComment(sticker.getId(), comment1);

    Comment comment2 = new Comment();
    comment2.setMessage("Comment 2 - " + i);
    stickerService.createComment(sticker.getId(), comment2);

    Comment comment3 = new Comment();
    comment3.setMessage("Comment 3 - " + i);
    stickerService.createComment(sticker.getId(), comment3);
  }

}
