package com.springboot.browser.util;

import com.springboot.browser.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebScraper {

    public static Page scrapePage(String url) {
        Document doc = getDocument(url);
        if (doc != null) {
            Page page = new Page(url);
            page.setTitle(doc.title());
            page.setIndexCounter(1);
            String words = Jsoup.clean(doc.body().html(), Whitelist.none())
                    .replaceAll("[^a-zA-Z0-9]", " ");
            page.setPageContent(words.trim().replaceAll("\\s+", " "));
            return page;
        }
        return null;
    }

    public static List<Page> scrapedPages(Page page) {
        List<Page> pages = new ArrayList<>();
        final String pageUrl = page.getUrl();
        page = scrapePage(pageUrl);
        if (page != null) {
            pages.add(page);
            Document doc = getDocument(pageUrl);
            if (doc != null) {
                List<String> links = doc.getElementsByTag("a").stream()
                        .filter(l -> l.hasAttr("href"))
                        .map(url -> url.getElementsByTag("a").attr("href"))
                        //avoid re-scrape the same url or try to scrape invalid urls
                        .filter(url -> url.contains(pageUrl) && !url.equals(pageUrl) || url.startsWith("http"))
                        .limit(2).collect(Collectors.toList());
                if (!links.isEmpty())
                    links.forEach(url -> {
                        Page p = scrapePage(url);
                        if (p != null)
                            pages.add(p);
                    });
            }
        }
        return pages;
    }

    private static Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

}
