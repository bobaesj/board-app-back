package com.bit.boardappbackend.controller;

import com.bit.boardappbackend.dto.BoardDto;
import com.bit.boardappbackend.dto.ResponseDto;
import com.bit.boardappbackend.entity.Board;
import com.bit.boardappbackend.entity.CustomUserDetails;
import com.bit.boardappbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    /*
    * application/json 형태로 넘어온 데이터에
    * multipartFile이 추가된 데이터는 @RequestPart로 받아준다.
    * */
    public ResponseEntity<?> post(@RequestPart("boardDto") BoardDto boardDto,
            @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @PageableDefault(page = 0, size = 10) Pageable pageable) {
        ResponseDto<BoardDto> responseDto = new ResponseDto<>();

        try {
            log.info("post boardDto: {}", boardDto);
            Page<BoardDto> boardDtoList = boardService.post(boardDto, uploadFiles, customUserDetails.getMember(), pageable);

            log.info("post boardDtoList: {}", boardDtoList);
            responseDto.setPageItems(boardDtoList);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

            return ResponseEntity.created(new URI("/boards")).body(responseDto);
        } catch (Exception e) {
            log.error("post error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping
    public ResponseEntity<?> getBoards(@RequestParam("searchCondition") String searchCondtion,
                                       @RequestParam("searchKeyword") String searchKeyword,
                                       @PageableDefault(page = 0, size = 10) Pageable pageable) {
        ResponseDto<BoardDto> responseDto = new ResponseDto<>();
        

        try {
            Page<BoardDto> boardDtoList = boardService.findAll(searchCondtion, searchKeyword, pageable);

            responseDto.setPageItems(boardDtoList);
            responseDto.setItem(BoardDto.builder()
                         .searchCondition(searchCondtion)
                         .searchKeyword(searchKeyword)
                         .build());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");

            return ResponseEntity.ok(responseDto);
        } catch(Exception e) {
            log.error("getBoards error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable("id") Long id) {
        ResponseDto<BoardDto> responseDto = new ResponseDto<>();

        try{
            log.info("findById id: {}", id);
            BoardDto boardDto = boardService.findById(id);

            responseDto.setItem(boardDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("getBoards error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        ResponseDto<BoardDto> responseDto = new ResponseDto<>();

        try{
            log.info("deleteById id: {}", id);
            boardService.deletById(id);

            responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
            responseDto.setStatusMessage("no content");

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            log.error("delete error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<?> modify(@PathVariable("id") int id,
//                                    BoardDto boardDto,
//                                    MultipartFile[] uploadFiles,
//                                    MultipartFile[] changeFiles,
//                                    @RequestParam(name = "originFiles", required = false) String originFiles) {
//        ResponseDto<BoardDto> responseDto = new ResponseDto<>();
//
//        try {
//            BoardDto modifyBoardDto = boardService.modify(boardDto, uploadFiles, changeFiles, originFiles);
//
//            responseDto.setItem(modifyBoardDto);
//            responseDto.setStatusCode(HttpStatus.OK.value());
//            responseDto.setStatusMessage("ok");
//            return ResponseEntity.ok(responseDto);
//        } catch(Exception e) {
//            log.error("modify error: {}", e.getMessage());
//            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            responseDto.setStatusMessage(e.getMessage());
//            return ResponseEntity.internalServerError().body(responseDto);
//        }
//
//    }









}
