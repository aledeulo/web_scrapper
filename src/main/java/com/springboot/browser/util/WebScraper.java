package com.springboot.browser.util;

import com.springboot.browser.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class WebScraper {

    private static final List<String> visited = new ArrayList<>();

    public static Page scrapePage(String url) {
        Document doc = getDocument(url);
        if (doc != null) {
            Page page = new Page(url);
            page.setTitle(doc.title());
            page.setIndexCounter(1);
            String words = Jsoup.clean(doc.body().html(), Whitelist.none()).replaceAll("[^a-zA-Z0-9]", " ");
            page.setPageContent(words.trim().replaceAll("\\s+", " "));
            return page;
        }
        return null;
    }

    public static List<Page> scrappedPages(Page page, List<Page> pages) {
        final String pageUrl = page.getUrl();
        page = scrapePage(pageUrl);
        if (page != null) {
            pages.add(page);
            Document doc = getDocument(pageUrl);
            if (doc != null) {
                int countLinks = doc.getElementsByTag("a").size();
                if (countLinks == 0 || pages.size() == 3)
                    return pages;
                String link = getFirstInternalLink(doc, pageUrl);
                if (pages.size() < 3 && Objects.nonNull(link))
                    scrappedPages(new Page(link), pages);
            }
        }
        return pages;
    }

    public static String getFirstInternalLink(Document doc, String pageUrl) {
        return doc.getElementsByTag("a").stream()
                .filter(l -> l.hasAttr("href"))
                .map(url -> url.getElementsByTag("a").attr("href"))
                //avoid re-scrape the same url or try to scrape invalid urls
                .filter(url -> url.startsWith("http") || (url.contains(pageUrl) && !url.equals(pageUrl)))
                .findFirst().orElse(null);
    }

    private static Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            String message = e.getMessage().toLowerCase();
            //avoid print an issue because of could not do handshake for ssl connections
            if (!message.contains("handshake"))
                e.printStackTrace();
        }
        return doc;
    }

}
