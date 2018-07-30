package com.duol.dao;

import com.duol.pojo.Category;
import com.duol.pojo.CategoryId2Pid;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectCategoryChildrenByParentId(Integer parentId);

    List<Integer> selectCategoryChildrenIdByParentId(Integer parentId);

    List<CategoryId2Pid> selectCategoryIdLists();
}