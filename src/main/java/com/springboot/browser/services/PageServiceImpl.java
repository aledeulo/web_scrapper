package com.springboot.browser.services;

import com.springboot.browser.model.Page;
import com.springboot.browser.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {

    private PageRepository pageRepo;

    @Autowired
    public void setPageRepo(PageRepository pageRepo) {
        this.pageRepo = pageRepo;
    }


    @Override
    public Iterable<Page> getAllPages() {
        return pageRepo.findAll();
    }

    @Override
    public Page getPageById(String url) {
        return pageRepo.findById(url).orElse(null);
    }

    @Override
    public Page savePage(Page page) {
        return pageRepo.save(page);
    }

    @Override
    public void DeleteAllIndexes() {
        pageRepo.deleteAll();
    }
}
