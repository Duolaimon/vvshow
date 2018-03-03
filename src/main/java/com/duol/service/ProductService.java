package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.pojo.Product;
import com.duol.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * @author Duolaimon
 * 18-2-26 下午10:42
 */
public interface ProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

}
