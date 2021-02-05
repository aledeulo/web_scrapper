package com.springboot.browser.controllers;

import com.springboot.browser.model.Page;
import com.springboot.browser.services.PageService;
import com.springboot.browser.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;


@Controller
public class SearchController extends Tools<Page, Long> {

    private PageService pageService;

    @Autowired
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @RequestMapping(value = "/search")
    String search(Model model) {
        return "search";
    }

    @RequestMapping(value = "/search/word", method = RequestMethod.GET)
    public String searchWord(@ModelAttribute("search") String word, Model model) {
        model.addAttribute("links", getPonderedLinks(word));
        return "search";
    }

    public Map<Page, Long> getPonderedLinks(String word) {
        Map<Page, Long> links = new HashMap<>();
        if (Objects.nonNull(word)) {
            List<Page> pages = (List<Page>) pageService.getAllPages();
            if (!pages.isEmpty()) {
                for (Page p : pages) {
                    String[] array = p.getPageContent().toLowerCase().split(" ");
                    long count = Arrays.stream(array).filter(a -> a.contains(word)).count();
                    if (count > 0) links.put(p, count);
                }
            }
            if (links.size() > 1)
                return sortByValue(links);
        }
        return links;
    }

    @Override
    public Map<Page, Long> sortByValue(Map<Page, Long> map) {
        List<Map.Entry<Page, Long>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<Page, Long> sortedMap = new HashMap<>();
        for (Map.Entry<Page, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
