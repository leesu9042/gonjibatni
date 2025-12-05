package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.support.WindowIterator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


//공지가져오기 + 가져온공지 List로 변환하기
//결국 가져오고 ,
@Service
public class AcademicNoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public AcademicNoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
    //repository 주입


    // 처음스크롤 위치 가진 공지 window들가져오기
    //이걸가지고 hasNext() , next()메소드를 쓰면 된다.

    // 첫 번째 페이지
    public Window<Notice> getFirstWindow() {
        return noticeRepository.findFirst10ByOrderByNoticeNoDesc(
                ScrollPosition.keyset()
        );
    }

    // 커서(noticeNo)로 다음 페이지 가져오기
    public Window<Notice> getNextWindow(Long lastNoticeId,Integer lastNoticeNo) {
        // 다음 페이지: noticeNo 90, 89, 88, ..., 81 (forward로 계속 진행)
        KeysetScrollPosition position = ScrollPosition.forward(Map.of(
                "noticeNo", lastNoticeNo,
                "noticeId", lastNoticeId

        ));

        return noticeRepository.findFirst10ByOrderByNoticeNoDesc(position);
    }
}
