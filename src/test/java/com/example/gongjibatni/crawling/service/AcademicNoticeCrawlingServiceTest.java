//package com.example.gongjibatni.crawling.service;
//
//import com.example.gongjibatni.classifier.Tag.NoticeTag;
//import com.example.gongjibatni.classifier.Tag.NoticeTagger;
//import com.example.gongjibatni.crawling.crawler.AcademicNoticeCrawler;
//import com.example.gongjibatni.notice.domain.Notice;
//import com.example.gongjibatni.notice.repository.NoticeRepository;
//import com.example.gongjibatni.notice.service.AcademicNoticeService;
//import org.jsoup.nodes.Document;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InOrder;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//
//import java.util.Collections; // Collections.emptyList() 사용
//import java.util.Set;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//
////@SpringBootTest
////@Transactional  // 테스트 끝나면 자동 롤백
//@ExtendWith(MockitoExtension.class)
//class AcademicNoticeCrawlingServiceTest {
//    // 1. Mock 객체: 의존성 객체 (NoticeRepository)를 가짜로 만듭니다.
//    @Mock
//    private NoticeRepository noticeRepository;
//
//    // 2. 새로운 공지 확인 을 가짜로 만든다.
//    @Mock
//    private NoticeSyncService noticeSyncService;
//
//    // 3. Mock 객체: 크롤러 (웹 크롤링 동작을 가짜로 만듭니다.)
//    @Mock
//    private AcademicNoticeCrawler academicNoticeCrawler;
//
//
//    @Mock
//    NoticeTagger noticeTagger;
//
//    @Mock
//    AcademicNoticeService academicNoticeService;
//
//    @InjectMocks AcademicNoticeCrawlingService sut; // 위 메서드들이 있는 클래스
//
//
//
//
//
//    @Test
//    void syncNotices_newNotices없으면_저장_캐시무효화_태깅_안한다() {
//
//        // given 최신  Notice 없을떄
//        List<Notice> input = List.of(new Notice(), new Notice());
//
//        //when
//        when(noticeSyncService.filterNewAcademicNotices(input)).thenReturn(List.of());
//
//        sut.syncNotices(input);
//
//        //then
//        verify(noticeTagger, never()).extractTags(anyString());
//        verify(noticeRepository, never()).saveAll(any());
//        verify(academicNoticeService, never()).evictFirstWindowCache();
//    }
//
//
////이해안가서 나중에 차근차근보기
////    @Test
////    void syncNotices_newNotices있으면_태그추출후_저장_캐시무효화한다() {
////        Notice n1 = new Notice("장학금 안내");
////        Notice n2 = new Notice("수강신청 변경");
////        List<Notice> input = List.of(n1, n2);
////        //notice만들고 title을 넣어야하는거아닌가
////        // 그리고 input 그대로 튀어나오고
////        // 그걸가지고 extractTags를 진행
////
////        when(noticeSyncService.filterNewAcademicNotices(input)).thenReturn(List.of(n1, n2));
////        when(noticeTagger.extractTags("장학금 안내")).thenReturn(Set.of(new NoticeTag("장학금")));
////        when(noticeTagger.extractTags("수강신청 변경")).thenReturn(Set.of(new NoticeTag("수강신청")));
////
////        sut.syncNotices(input);
////
////        // 상태 검증: tags가 실제로 추가됐는지
////        assertTrue(n1.getTags().stream().anyMatch(t -> t.getName().equals("장학금")));
////        assertTrue(n2.getTags().stream().anyMatch(t -> t.getName().equals("수강신청")));
////
////        // 행위 검증
////        verify(noticeRepository).saveAll(List.of(n1, n2));
////        verify(academicNoticeService).evictFirstWindowCache();
////
////        // 호출 순서까지 보고 싶으면
////        InOrder inOrder = inOrder(noticeRepository, academicNoticeService);
////        inOrder.verify(noticeRepository).saveAll(List.of(n1, n2));
////        inOrder.verify(academicNoticeService).evictFirstWindowCache();
////    }
////
//
//    // 4. 테스트 대상 객체: InjectMocks를 사용하여 모든 Mock 의존성을 주입받음
//    @InjectMocks
//    private AcademicNoticeCrawlingService academicNoticeCrawlingService; // (혹은 crawlingService)
//
//    // 테스트용 Notice 객체를 간결하게 생성하는 헬퍼 메서드
//    private Notice createNotice(int no) {
//        // 1. Notice 인스턴스 생성
//        Notice notice = new Notice();
//        // 2. 테스트의 핵심 값 설정: noticeNo
//        notice.setNoticeNo(no);
//        // 3. 나머지 필드에는 테스트에 영향을 주지 않는 일관된 기본값 설정
//        // 제목: 테스트 목적으로 번호를 포함하여 명확하게 설정
//        notice.setTitle("Test Title " + no);
//        // 작성자: 기본값
//        notice.setWriter("테스트 작성자");
//
//        // 생성일: 필터링 로직에 영향이 없도록 고정된 날짜 사용 (예: 2024-01-01)
//        notice.setCreatedAt(LocalDate.of(2024, 1, 1));
//        // Nttid: 테스트 목적으로 ID를 문자열로 설정 (실제 로직에 맞게)
//        notice.setNttid(String.valueOf(no));
//
//
//        return notice;
//    }
//
//
//
//    @Test
//    @DisplayName("크롤링 결과가 존재할 경우, 필터링 후 saveAll이 호출되어야 한다")
//    void testCrawlAndSave_Success() {
//        // 1. Given (준비)
//        String testUrl = "http://test.com";
//        List<Notice> fullList = Arrays.asList(createNotice(10), createNotice(15));
//        List<Notice> newNoticesToSave = Arrays.asList(createNotice(15)); // 필터링된 최종 결과
//
//        // Mocking 1: Crawler 설정 (가짜 입력 데이터를 반환)
//        when(academicNoticeCrawler.LoadFromURL(testUrl)).thenReturn(mock(Document.class));
//        when(academicNoticeCrawler.parsingElementsToNotices(any(Document.class))).thenReturn(fullList);
//
//
//        // Mocking 2: SyncService 설정 (필터링된 최종 결과를 반환)
//        when(noticeSyncService.filterNewAcademicNotices(fullList)).thenReturn(newNoticesToSave);
//
//        when(keywordClassifier.classifyKeyword(anyString()))
//                .thenReturn("학사");
//
//
//        // 2. When (실행)
//        academicNoticeCrawlingService.crawlLatestNotices(testUrl);
//
//        // 3. Then (검증)
//        // 검증 1: saveAll이 필터링된 newNoticesToSave를 인자로 받아 한 번 호출되었는지 확인
//        verify(noticeRepository, times(1)).saveAll(newNoticesToSave);
//    }
//
//
//
//    @Test
//    @DisplayName("필터링 결과가 비어있을 경우, saveAll이 호출되지 않아야 한다")
//    void testCrawlAndSave_NoNewNotices() {
//        // 1. Given (준비)
//        String testUrl = "http://test.com";
//        List<Notice> fullList = Arrays.asList(createNotice(10));
//
//        // Mocking: SyncService가 빈 리스트를 반환하도록 설정
//        when(noticeSyncService.filterNewAcademicNotices(fullList)).thenReturn(Collections.emptyList());
//        when(academicNoticeCrawler.LoadFromURL(testUrl)).thenReturn(mock(Document.class));
//        when(academicNoticeCrawler.parsingElementsToNotices(any(Document.class))).thenReturn(fullList);
//
//        // 2. When (실행)
//        academicNoticeCrawlingService.crawlLatestNotices(testUrl);
//
//        // 3. Then (검증)
//        // 검증 1: saveAll 메서드가 단 한 번도 호출되지 않았는지 확인
//        verify(noticeRepository, never()).saveAll(anyList());
//    }
//
//
//    @Test
//    void crawlAndSaveTenPage() {
//
//
//    }
//}
