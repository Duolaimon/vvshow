package com.duol.service.impl;

import com.duol.cache.ListCache;
import com.duol.common.ServerResponse;
import com.duol.dao.CategoryMapper;
import com.duol.pojo.Category;
import com.duol.pojo.CategoryId2Pid;
import com.duol.service.CategoryService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Duolaimon
 * 18-2-24 下午2:24
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private static final String CATEGORY_PREFIX = "category:";
    private static final String CATEGORY_ID_LIST_PREFIX = "categoryList";

    private final CategoryMapper categoryMapper;
    private final ListCache<CategoryId2Pid> categoryId2PidListCache;
    private final ListCache<Integer> integerListCache;

    private List<CategoryId2Pid> categoryIdList;


    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper, ListCache<CategoryId2Pid> categoryId2PidListCache, ListCache<Integer> integerListCache) {
        this.categoryMapper = categoryMapper;
        this.categoryId2PidListCache = categoryId2PidListCache;
        this.integerListCache = integerListCache;
    }

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        cacheCategoryIdList(categoryId);
        categoryIdList = categoryId2PidListCache.range(CATEGORY_ID_LIST_PREFIX);
        return cacheCategoryIdByParentId(categoryId);

    }

    /**
     * 缓存类别id和其父类id对应关系
     */
    private void cacheCategoryIdList(Integer categoryId) {
        if (!categoryId2PidListCache.hasKey(CATEGORY_ID_LIST_PREFIX)) {
            categoryIdList = categoryMapper.selectCategoryIdLists();
            List<Integer> resultList = Lists.newArrayList();
            findChildCategoryId(resultList, categoryId);
            categoryId2PidListCache.cacheNewList(CATEGORY_ID_LIST_PREFIX, categoryIdList);
        }
    }

    /**
     * 缓存指定类别和其子类id
     */
    private ServerResponse<List<Integer>> cacheCategoryIdByParentId(Integer categoryId) {
        if(integerListCache.hasKey(categoryId.toString())){
            return ServerResponse.createBySuccess(integerListCache.range(categoryId.toString()));
        }
        List<Integer> result = Lists.newArrayList();
        result.add(categoryId);
        findChildCategoryId(result,categoryId);
        integerListCache.cacheNewList(CATEGORY_PREFIX + categoryId,result);
        return ServerResponse.createBySuccess(result);
    }

    private void findChildCategoryId(List<Integer> result, Integer parentId) {
        List<CategoryId2Pid> list = categoryIdList.stream().filter(item -> parentId.equals(item.getParentId())).collect(Collectors.toList());
        for (CategoryId2Pid item :
                list) {
            result.add(item.getId());
            findChildCategoryId(result, item.getId());
        }
    }
}
