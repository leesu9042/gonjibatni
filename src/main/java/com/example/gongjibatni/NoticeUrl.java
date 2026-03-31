package com.example.gongjibatni;

public enum NoticeUrl {
    ACADEMIC(
            "https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do?mno=sub07_01"
    ),
    ACADEMIC_NOTICE(
            "https://www.hanbat.ac.kr/bbs/bbsmstr_000000000050/list.do"
    );


    private final String url;

    NoticeUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
