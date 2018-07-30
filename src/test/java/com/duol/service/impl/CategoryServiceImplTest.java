package com.duol.service.impl;

import com.duol.cache.ListCache;
import com.duol.common.ServerResponse;
import com.duol.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Duolaimon
 * 18-7-23 下午4:37
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ListCache listCache;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void selectCategoryAndChildrenById() {
        Integer categoryId = 100002;
        ServerResponse<List<Integer>> response = categoryService.selectCategoryAndChildrenById(categoryId);
        System.out.println(response.getData());
    }

    @Test
    public void delete() {
        listCache.delete("categoryId");
    }
}