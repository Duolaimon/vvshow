package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.pojo.Category;

import java.util.List;

/**
 * @author Duolaimon
 * 18-2-24 下午2:19
 */
public interface CategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    /**
     * 递归查询本节点的id及其孩子节点的id
     * @param categoryId    本节点
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
