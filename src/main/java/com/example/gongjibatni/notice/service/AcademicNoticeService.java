package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.support.WindowIterator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
    public WindowIterator<Notice> getNoticeFirstwindow() {

        WindowIterator<Notice> Notices = WindowIterator.of(
                position -> noticeRepository.findFirst10ByOrderByNoticeNoDesc(position))
                .startingAt(ScrollPosition.keyset());

        return Notices;
    }





    //Window를 list로 변환 함수
    public List<Notice> iteratorToList(WindowIterator<Notice> iterator) {

        List<Notice> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }




}
