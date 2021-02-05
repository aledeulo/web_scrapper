package com.springboot.browser.services;

import com.springboot.browser.model.Page;


public interface PageService {

    Iterable<Page> getAllPages();

    Page getPageById(String url);

    Page savePage(Page page);

    void DeleteAllIndexes();


}
