package com.duol.controller.portal;

import com.duol.common.ServerResponse;
import com.duol.service.ProductService;
import com.duol.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-14 下午8:40
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{productId}")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(@PathVariable("productId") Integer productId){
        return productService.getProductDetail(productId);
    }

    @GetMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategory(keyword, categoryId,pageNum,pageSize,orderBy);
    }





}
