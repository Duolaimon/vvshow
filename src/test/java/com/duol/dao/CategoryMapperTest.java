package com.duol.dao;

import com.duol.pojo.CategoryId2Pid;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Duolaimon
 * 18-7-23 上午11:06
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;
    List<CategoryId2Pid> categoryIdList;

    @Before
    public void setup() {
        categoryIdList = categoryMapper.selectCategoryIdLists();
    }

    @Test
    public void selectCategoryIdLists() {
        List<CategoryId2Pid> list = categoryMapper.selectCategoryIdLists();
        System.out.println(list);
    }

    @Test
    public void findChildCategoryId() {
        List<Integer> list = Lists.newArrayList();
        Integer parentId = 0;
        doFindChildCategoryId(list,parentId);
        System.out.println(list);
    }

    private void doFindChildCategoryId(List<Integer> result, Integer parentId) {
        List<CategoryId2Pid> list = categoryIdList.stream().filter(item -> parentId.equals(item.getParentId())).collect(Collectors.toList());
        for (CategoryId2Pid item:
             list) {
            result.add(item.getId());
            doFindChildCategoryId(result,item.getId());
        }
    }
}