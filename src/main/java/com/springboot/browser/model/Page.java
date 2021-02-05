package com.springboot.browser.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Page {
    @Id
    private String url;
    private String title;
    private int indexCounter;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String pageContent;

    public Page() {
    }

    public Page(String url) {
        this.url = url;
    }

    public int getIndexCounter() {
        return indexCounter;
    }

    public void setIndexCounter(int indexCounter) {
        this.indexCounter = indexCounter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Page{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", indexCounter=" + indexCounter +
                ", content='" + pageContent + '\'' +
                '}';
    }
}
