package com.springboot.browser.controllers;

import com.springboot.browser.model.Page;
import com.springboot.browser.services.PageService;
import com.springboot.browser.util.WebScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private PageService pageService;

    @Autowired
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @RequestMapping("/browser")
    String index(Model model) {
        model.addAttribute("page", new Page());
        return "index";
    }

    @RequestMapping(value = "browser/clear", method = RequestMethod.POST)
    public String deleteIndexes(Model model) {
        pageService.DeleteAllIndexes();
        model.addAttribute("erased", "All index has been cleared");
        model.addAttribute("page", new Page());
        return "index";
    }

    @RequestMapping(value = "browser/page", method = RequestMethod.POST)
    public String scrapePage(@ModelAttribute("page") Page page, Model model) {
        List<Page> pages = WebScraper.scrappedPages(page, new ArrayList<>());
        final AtomicLong inc = new AtomicLong(0);
        if (!pages.isEmpty()) {
            for (Page p : pages) {
                Page previous = pageService.getPageById(page.getUrl());
                int index = (previous != null) ? previous.getIndexCounter() + 1 : 1;
                p.setIndexCounter(index);
                if (pageService.savePage(p) != null) {
                    long wordsCount = Arrays.stream(p.getPageContent().split(" "))
                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                            .entrySet().stream()
                            .filter(e -> e.getValue() == 1)
                            .count() + inc.get();
                    inc.set(wordsCount);
                }
            }
        }
        model.addAttribute("indexed", pages.size());
        model.addAttribute("count_words", inc.get());
        return "index";
    }
}
