package com.bit.boardappbackend.service;

import com.bit.boardappbackend.dto.BoardDto;
import com.bit.boardappbackend.entity.Board;
import com.bit.boardappbackend.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface BoardService {
    Page<BoardDto> post(BoardDto boardDto, MultipartFile[] uploadFiles, Member member, Pageable pageable);

    Page<BoardDto> findAll(String searchCondition, String searchKeyword, Pageable pageable);


    BoardDto findById(Long id);

    void deletById(Long id);

}
