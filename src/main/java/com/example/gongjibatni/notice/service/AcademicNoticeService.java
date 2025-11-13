package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AcademicNoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public AcademicNoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
    //repository 주입


    //공지 처음 window가져오기
    public Window<Notice> getNoticeFirstwindow() {

        //오프셋 포지션 0으로 설정
        OffsetScrollPosition offset = ScrollPosition.offset();
        Window<Notice> noticeWindow  = noticeRepository.findFirst10ByOrderByNoticeNoDesc(offset);
        //리팩토링 가능점 이 기능만 달라지고 나머지는 그대로 쓰일 것 같음
        //그래서 이부분을 함수를 외부에서 가져와서 실행하는 느낌으로 하는것 고려



        return noticeWindow;
    }



    // 공지 window로 다음 page 가져오기
    public Window<Notice> getNoticeNextwindow(Window<Notice> noticeWindow) {
        if (noticeWindow.isEmpty() && !noticeWindow.hasNext()) {
            return Window.from(Collections.emptyList(), ScrollPosition::offset);
        }

        // 다음 window 가져오기

        Window<Notice> nextNoticeWinodw = noticeRepository.findFirst10ByOrderByNoticeNoDesc(
                (OffsetScrollPosition) noticeWindow.positionAt(noticeWindow.size() - 1)
        );
        // window.positionAt(9) Window 안의 9번째 요소를 기준으로 다음 위치를 만든다.


        return nextNoticeWinodw;
    }

    
    //Window를 list로 변환 함수
    public List<Notice> windowToList(Window<Notice> noticeWindow) {
        
        ArrayList<Notice> resultList = new ArrayList<>();
        noticeWindow.forEach(resultList::add);


        return resultList;
    }



}
