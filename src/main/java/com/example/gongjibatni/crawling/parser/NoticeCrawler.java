package com.example.gongjibatni.crawling.parser;

import com.example.gongjibatni.notice.domain.Notice;
import org.jsoup.nodes.Document;

import java.util.List;

public interface NoticeCrawler {

    Document LoadFromURL(String url);
    // URL의 html load하기
    List<Notice> parsingElementsToNotices(Document doc);
    // Doc의 요소를 Notice list로 뽑아내기


}
