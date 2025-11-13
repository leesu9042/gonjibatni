package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.crawling.service.AcademicNoticeCrawlingService;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AcademicNoticeServiceTest {


    @Autowired
    private AcademicNoticeService academicNoticeService;


    @Test
    void getAcademicNotice() {
        List<Notice> firstPage = academicNoticeService.getNoticeFirstPage();

        academicNoticeService.getNoticeNextPage();


    }



}