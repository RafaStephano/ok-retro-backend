package br.com.rstephano.okretrobackend.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rstephano.okretrobackend.entity.Board;
import br.com.rstephano.okretrobackend.entity.Column;
import br.com.rstephano.okretrobackend.exceptions.BoardNotFoundException;
import br.com.rstephano.okretrobackend.exceptions.ColumnNotFoundException;
import br.com.rstephano.okretrobackend.repository.BoardRepository;

@Service
public class BoardService {

  @Autowired
  private BoardRepository boardRepository;

  public List<Board> listAllBoards() {
    return boardRepository.findAll();
  }

  public Board findBoardById(ObjectId boardId) {
    return boardRepository.findById(boardId) //
        .orElseThrow(() -> new BoardNotFoundException(boardId.toHexString()));
  }

  public Column findColumnById(ObjectId boardId, ObjectId columnId) {
    return findBoardById(boardId) //
        .getColumns() //
        .stream() //
        .filter(c -> c.getId().equals(columnId)) //
        .findFirst() //
        .orElseThrow(() -> new ColumnNotFoundException(boardId.toHexString(), columnId.toHexString()));
  }

  public Board createBoard(Board board) {
    board.setId(new ObjectId());
    board.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
    return boardRepository.save(board);
  }

  public Column createColumn(ObjectId boardId, Column column) {
    Board board = findBoardById(boardId);
    column.setId(new ObjectId());
    board.getColumns().add(column);
    boardRepository.save(board);
    return column;
  }

  public void editBoard(Board editedBoard) {
    Board board = findBoardById(editedBoard.getId());
    board.setName(editedBoard.getName());
    board.setDescription(editedBoard.getDescription());
    boardRepository.save(board);
  }

  public void editColumn(ObjectId boardId, Column editedColumn) {
    Board board = findBoardById(boardId);
    board.getColumns() //
        .stream() //
        .filter(c -> c.getId().equals(editedColumn.getId())) //
        .peek(c -> {
          c.setHexColor(editedColumn.getHexColor());
          c.setName(editedColumn.getName());
          c.setOrder(editedColumn.getOrder());
        }) //
        .findFirst() //
        .orElseThrow(() -> new ColumnNotFoundException(boardId.toHexString(), editedColumn.getId().toHexString()));
    boardRepository.save(board);
  }

  public void deleteBoardById(ObjectId boardId) {
    findBoardById(boardId);
    boardRepository.deleteById(boardId);
  }

  public void deleteColumnById(ObjectId boardId, ObjectId columnId) {
    Board board = findBoardById(boardId);
    findColumnById(boardId, columnId);
    List<Column> columns = board.getColumns() //
        .stream() //
        .filter(c -> !c.getId().equals(columnId)) //
        .collect(Collectors.toList());
    board.setColumns(columns);
    boardRepository.save(board);
  }

}
