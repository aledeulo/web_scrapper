package com.springboot.browser;

import com.springboot.browser.model.Page;
import com.springboot.browser.util.WebScraper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class BrowserApplicationTests {
    final Page page = new Page("https://stackoverflow.com/questions/51527683/java-net-unknownhostexception-dockerized-mysql-from-spring-boot-application");

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    void contextLoads() {
        List<Page> pages = WebScraper.scrappedPages(page, new ArrayList<>());
        Assertions.assertEquals(pages.size(), 3);
        Assertions.assertEquals(pages.get(0).getTitle(), "java.net.UnknownHostException dockerized mysql from spring boot application - Stack Overflow");
        String[] array = pages.get(0).getPageContent().toLowerCase().split(" ");
        long count = Arrays.stream(array).filter(a -> a.contains("docker")).count();
        Assertions.assertEquals(count, 20);
    }
}
