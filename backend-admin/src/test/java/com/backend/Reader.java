package com.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class Reader {

    private static final String BIQUGE = "https://www.biqumo.com/0_75/8328045.html";
    private static final int TIME_OUT = 5000;


    public static void main(String[] args) {
//        chapters();
        reader();
    }

    private static void chapters() {
        try {
            String urlStr = "https://www.biqumo.com/0_75";
            URL url = new URL(urlStr);
            Document document = Jsoup.parse(url, TIME_OUT);
            System.out.println(document.body());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void reader() {
        try {
            URL url = new URL(BIQUGE);
            Document document = Jsoup.parse(url, TIME_OUT);
//            Element body = document.body();
//            System.out.println(body.html());
            content(document);
            nextChapter(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String content(Document document) {
        Element content = document.getElementById("content");
        String html = content.html();
        html = html.replace("<script>app2();</script>　　", "")
                .replaceAll("<br> \n<br> 　　", "");
        System.out.println(html);
        return html;
    }

    private static String nextChapter(Document document) {
        Element pageChapter = document.getElementsByClass("page_chapter").first();
//        System.out.println(pageChapter.html());
        Element nextChapter = pageChapter.getElementsContainingOwnText("下一章").first();
//        System.out.println(nextChapter);
        String href = nextChapter.attr("href");
        System.out.println(href);
        String nextC = href.substring(href.lastIndexOf("/") + 1, href.indexOf("."));
//        System.out.println(nextC);
        return nextC;
    }

}
