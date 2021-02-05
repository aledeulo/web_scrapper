package com.springboot.browser.repositories;

import com.springboot.browser.model.Page;
import org.springframework.data.repository.CrudRepository;

public interface PageRepository extends CrudRepository<Page, String> {
}
