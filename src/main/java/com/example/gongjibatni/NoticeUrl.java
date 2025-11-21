package com.example.gongjibatni;

public enum NoticeUrl {
    ACADEMIC("https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do?mno=sub07_01");


    private final String url;

    NoticeUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
