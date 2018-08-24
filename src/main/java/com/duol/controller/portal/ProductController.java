package com.duol.controller.portal;

import com.duol.common.ServerResponse;
import com.duol.service.ProductService;
import com.duol.vo.ProductDetailVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-14 下午8:40
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @ApiOperation("获取指定产品")
    @GetMapping("/{productId}")
    public ServerResponse<ProductDetailVO> detail(@PathVariable("productId") Integer productId){
        return productService.getProductDetail(productId);
    }

    @ApiOperation("关键字查找产品")
    @GetMapping("/")
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",required = false) String orderBy){
        return productService.getProductByKeywordCategory(keyword, categoryId,pageNum,pageSize,orderBy);
    }





}
