package com.duol.service.impl;

import com.duol.common.Const;
import com.duol.common.ResponseCode;
import com.duol.common.ServerResponse;
import com.duol.dao.CategoryMapper;
import com.duol.dao.ProductMapper;
import com.duol.pojo.Category;
import com.duol.pojo.Product;
import com.duol.service.CategoryService;
import com.duol.service.ProductService;
import com.duol.vo.ProductDetailVO;
import com.duol.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Duolaimon
 * 18-2-26 下午10:49
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper, CategoryService categoryService) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
    }

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        ProductDetailVO productDetailVo = ProductDetailVO.assembleProductDetailVO(product, category == null ? 0 : category.getId());
        return ServerResponse.createBySuccess(productDetailVo);
    }


    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        return getPageInfoServerResponse(productList);
    }


    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        String name = "";
        if (StringUtils.isNotBlank(productName)) {
            name = "%" + productName + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByNameAndProductId(name, productId);
        return getPageInfoServerResponse(productList);
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        ProductDetailVO productDetailVo = ProductDetailVO.assembleProductDetailVO(product, category == null ? 0 : category.getId());
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                return getPageInfoServerResponse(pageNum, pageSize);
            }
            if (category != null)
                categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        orderBy(orderBy);
        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        return getPageInfoServerResponse(productList);
    }

    private ServerResponse<PageInfo> getPageInfoServerResponse(int pageNum, int pageSize) {
        List<ProductListVO> productListVoList = Lists.newArrayList();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ServerResponse<PageInfo> getPageInfoServerResponse(List<Product> productList) {
        List<ProductListVO> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVo = ProductListVO.assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private void orderBy(String orderBy) {
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
    }
}
