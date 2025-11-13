package com.example.gongjibatni.crawling;

import com.example.gongjibatni.notice.domain.Notice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//크롤링 해서 rows 리턴

@Component   // 스프링이 자동으로 Bean 등록
public class AcademicNoticeCrawler implements NoticeCrawler{

    /**
     * 주어진 URL에서 HTML 문서를 가져옴
     * @param url 크롤링할 웹페이지 URL
     * @return Jsoup Document 객체 (연결 실패 시 null)
     */

    public Document LoadFromURL(String url) {
        try {
            return Jsoup.connect(url).get();

        } catch (IOException e) {
            // 연결 끊기면 에러처리 + null 반환
            System.out.println(" Connection error: " + e.getMessage()); //이것도 바꿔야함
            return null;
        }

    }
    /**
     * HTML Document에서 공지사항 정보를 추출하여 Notice 객체 리스트로 변환
     * <p>파싱 대상:
     * <ul>
     *   <li>번호 (num)</li>
     *   <li>제목 (title)</li>
     *   <li>작성자 (writer)</li>
     *   <li>등록일 (date)</li>
     *   <li>링크 (link)</li>
     * </ul>
     * </p>
     *
     * @param doc Jsoup으로 파싱된 HTML 문서
     * @return 파싱된 Notice 객체 리스트 (번호가 없는 게시글은 제외)
     */
    public List<Notice> parsingElementsToNotices(Document doc) {
        //  <div class="no-more-tables"> 아래 <table class="board_list"> 내부의 <tr> 전부 선택
        Elements rows = doc.select("div.no-more-tables table.board_list tbody tr");


        List<Notice> notices = new ArrayList<>();

        // 각 tr 순회하면서 td 속성값 추출
        for (Element row : rows) {


            Notice notice = new Notice();

            //  data-cell-header 속성값으로 구분해서 데이터 추출
            String num = row.select("td[data-cell-header=번호]").text();
            String title = row.select("td[data-cell-header=제목]").text();
            String writer = row.select("td[data-cell-header=작성자]").text();
            String date = row.select("td[data-cell-header=등록일]").text();


            String link = row.select("td[data-cell-header=제목] a").attr("onclick");
            String nttid = extractNttId(link);
            //nttid추출



            // 빈 문자열 체크 추가
            if (num.isEmpty()) {
                System.out.println("번호 없는 게시글 스킵: " + title);
                continue;
            }

            notice.setWriter(writer);
            notice.setTitle(title);
            notice.setNoticeNo(Integer.valueOf(num));
            notice.setCreatedAt(LocalDate.parse(date));
            notice.setNttid(nttid);
            notices.add(notice);

            // num이 notice같은것들은 없을 수도 있다

        }

        return notices;
    }




    //nttid 추출
    private String extractNttId(String onclick) {
        // fn_search_detail('여기') 패턴
        Pattern pattern = Pattern.compile("fn_search_detail\\('([^']+)'\\)");
        Matcher matcher = pattern.matcher(onclick);

        if (matcher.find()) {
            return matcher.group(1);  //
        }

        return "";
    }







}

