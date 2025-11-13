package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AcademicNoticeServiceTest {


    @Autowired
    private AcademicNoticeService academicNoticeService;

    @DisplayName("공지를 가져와서 print하는 매우 안좋지만 일단이걸로 확인완료")
    @Test
    void getAcademicNotice() {
        Window<Notice> firstNoticesWindow= academicNoticeService.getNoticeFirstwindow();

        Window<Notice> NextNoticesWindow = academicNoticeService.getNoticeNextwindow(firstNoticesWindow);

//          //list에 넣기
//        ArrayList<Notice> firstNotices = new ArrayList<>();
//        firstNoticesWindow.forEach(firstNotices::add);


        List<Notice> firstNotices = academicNoticeService.windowToList(firstNoticesWindow);


        List<Notice> nextNotices = academicNoticeService.windowToList(NextNoticesWindow);
//        ArrayList<Notice> nextNotices = new ArrayList<>();
//        NextNoticesWindow.forEach(nextNotices::add);


        Assertions.assertNotNull(firstNotices);
        Assertions.assertNotNull(nextNotices);

        System.out.println("===== NOTICE 출력 =====");

        for (Notice n : firstNotices) {
            System.out.println("---- Notice ----");
            System.out.println("id: " + n.getNoticeId());
            System.out.println("title: " + n.getTitle());
            System.out.println("writer: " + n.getWriter());
            System.out.println("createdAt: " + n.getCreatedAt());
            System.out.println("category: " + n.getCategoryKeyword());
            System.out.println("noticeNo: " + n.getNoticeNo());
            System.out.println("nttid: " + n.getNttid());
        }








    }



}