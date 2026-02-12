package com.example.gongjibatni.notice.repository;

import com.example.gongjibatni.notice.domain.Notice;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {

    List<Notice> findByCategoryKeyword(String categoryKeyword);

    Slice<Notice> findAllBy(Pageable pageable);
    // 그냥 slice로 오프셋써서 Notice 가져오기

    Window<Notice> findFirst10ByOrderByNoticeNoDesc(ScrollPosition position);
    //key-set-based 윈도우 방식

    Optional<Notice> findTopByOrderByNoticeNoDesc();
    // NoticeNo 가장높은거 가져오기 optional로

    boolean existsByNoticeNo(Integer noticeNo);
    // NoticeNo가 존재하는지

    @Query("select n.title from Notice n")
    List<String> findAllTitles();
    //공지사항 제목만 가져오는 쿼리메소드





}
