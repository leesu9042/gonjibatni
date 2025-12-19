package com.example.gongjibatni.crawling.service;

import com.example.gongjibatni.crawling.parser.AcademicNoticeCrawler;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


import java.util.Collections; // Collections.emptyList() 사용

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//@SpringBootTest
//@Transactional  // 테스트 끝나면 자동 롤백
@ExtendWith(MockitoExtension.class)
class AcademicNoticeCrawlingServiceTest {
    // 1. Mock 객체: 의존성 객체 (NoticeRepository)를 가짜로 만듭니다.
    @Mock
    private NoticeRepository noticeRepository;

    // 2. 새로운 공지 확인 을 가짜로 만든다.
    @Mock
    private NoticeSyncService noticeSyncService;

    // 3. Mock 객체: 크롤러 (웹 크롤링 동작을 가짜로 만듭니다.)
    @Mock
    private AcademicNoticeCrawler academicNoticeCrawler;


    // 4. 테스트 대상 객체: InjectMocks를 사용하여 모든 Mock 의존성을 주입받음
    @InjectMocks
    private AcademicNoticeCrawlingService academicNoticeCrawlingService; // (혹은 crawlingService)

    // 테스트용 Notice 객체를 간결하게 생성하는 헬퍼 메서드
    private Notice createNotice(int no) {
        // 1. Notice 인스턴스 생성
        Notice notice = new Notice();
        // 2. 테스트의 핵심 값 설정: noticeNo
        notice.setNoticeNo(no);
        // 3. 나머지 필드에는 테스트에 영향을 주지 않는 일관된 기본값 설정
        // 제목: 테스트 목적으로 번호를 포함하여 명확하게 설정
        notice.setTitle("Test Title " + no);
        // 작성자: 기본값
        notice.setWriter("테스트 작성자");
        // 생성일: 필터링 로직에 영향이 없도록 고정된 날짜 사용 (예: 2024-01-01)
        notice.setCreatedAt(LocalDate.of(2024, 1, 1));
        // Nttid: 테스트 목적으로 ID를 문자열로 설정 (실제 로직에 맞게)
        notice.setNttid(String.valueOf(no));


        return notice;
    }


//    @DisplayName("크롤러 Service의 크롤링결과 저장기능 확인 ")
//    @Test
//    void crawlAndSaveTest() {
//        // 테스트할 URL 지정 (학교 공지 페이지 등)
//        String url = "https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do?mno=sub07_01";
//
//        crawlingService.crawlAndSave(url);
//
//        // DB에 실제로 저장되었는지 확인
//        List<Notice> notices = noticeRepository.findAll();
//        System.out.println("저장된 공지 개수: " + notices.size());
//
//        for (Notice n : notices) {
//            System.out.println(n.getTitle() + " | " + n.getWriter());
//        }
//    }


    @Test
    @DisplayName("크롤링 결과가 존재할 경우, 필터링 후 saveAll이 호출되어야 한다")
    void testCrawlAndSave_Success() {
        // 1. Given (준비)
        String testUrl = "http://test.com";
        List<Notice> fullList = Arrays.asList(createNotice(10), createNotice(15));
        List<Notice> newNoticesToSave = Arrays.asList(createNotice(15)); // 필터링된 최종 결과

        // Mocking 1: Crawler 설정 (가짜 입력 데이터를 반환)
        when(academicNoticeCrawler.LoadFromURL(testUrl)).thenReturn(mock(Document.class));
        when(academicNoticeCrawler.parsingElementsToNotices(any(Document.class))).thenReturn(fullList);

        // Mocking 2: SyncService 설정 (필터링된 최종 결과를 반환)
        when(noticeSyncService.filterNewAcademicNotices(fullList)).thenReturn(newNoticesToSave);

        // 2. When (실행)
        academicNoticeCrawlingService.crawlAndSave(testUrl);

        // 3. Then (검증)
        // 검증 1: saveAll이 필터링된 newNoticesToSave를 인자로 받아 한 번 호출되었는지 확인
        verify(noticeRepository, times(1)).saveAll(newNoticesToSave);
    }



    @Test
    @DisplayName("필터링 결과가 비어있을 경우, saveAll이 호출되지 않아야 한다")
    void testCrawlAndSave_NoNewNotices() {
        // 1. Given (준비)
        String testUrl = "http://test.com";
        List<Notice> fullList = Arrays.asList(createNotice(10));

        // Mocking: SyncService가 빈 리스트를 반환하도록 설정
        when(noticeSyncService.filterNewAcademicNotices(fullList)).thenReturn(Collections.emptyList());
        when(academicNoticeCrawler.LoadFromURL(testUrl)).thenReturn(mock(Document.class));
        when(academicNoticeCrawler.parsingElementsToNotices(any(Document.class))).thenReturn(fullList);

        // 2. When (실행)
        academicNoticeCrawlingService.crawlAndSave(testUrl);

        // 3. Then (검증)
        // 검증 1: saveAll 메서드가 단 한 번도 호출되지 않았는지 확인
        verify(noticeRepository, never()).saveAll(anyList());
    }


}
