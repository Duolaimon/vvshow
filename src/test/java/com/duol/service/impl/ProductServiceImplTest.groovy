package com.duol.service.impl

import com.duol.pojo.Product
import com.duol.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.support.FileSystemXmlApplicationContext
import spock.lang.Specification

class ProductServiceImplTest extends Specification {
    private ProductService productService

    void setup() {
        System.out.println(" ============= test start =============")


        ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");

        productService = ac.getBean(ProductService.class)
    }

    void cleanup() {
    }

    def "SaveOrUpdateProduct"() {
        setup:
        Product product = new Product()
        product.setName("49寸超流行液晶显示器")
        product.setPrice(BigDecimal.valueOf(9998L))
        product.setCategoryId(1000025)
        product.setStock(800)
        product.setStatus(1)
        product.setSubtitle("49寸显示器")
        when:
        productService.saveOrUpdateProduct(product)

    }

    def "SetSaleStatus"() {
    }

    def "ManageProductDetail"() {
    }

    def "GetProductList"() {
    }

    def "SearchProduct"() {
    }

    def "GetProductDetail"() {
    }

    def "GetProductByKeywordCategory"() {
    }
}
