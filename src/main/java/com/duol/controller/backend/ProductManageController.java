package com.duol.controller.backend;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.pojo.Product;
import com.duol.pojo.User;
import com.duol.service.FileService;
import com.duol.service.ProductService;
import com.duol.service.UserService;
import com.duol.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Duolaimon
 * 18-7-14 下午9:06
 */
@RestController
@RequestMapping("/manage")
public class ProductManageController {

    private final UserService userService;
    private final ProductService productService;
    private final FileService fileService;

    @Autowired
    public ProductManageController(UserService userService, ProductService productService, FileService fileService) {
        this.userService = userService;
        this.productService = productService;
        this.fileService = fileService;
    }

    @PostMapping("/product")
    public ServerResponse productSave(Product product) {
        return productService.saveOrUpdateProduct(product);
    }

    @PutMapping("/product/{productId}/status")
    public ServerResponse setSaleStatus(@PathVariable("productId") Integer productId, Integer status) {
        return productService.setSaleStatus(productId, status);
    }

    @GetMapping("/product/{productId}")
    public ServerResponse getDetail(@PathVariable("productId") Integer productId) {
        return productService.manageProductDetail(productId);
    }

    @GetMapping("/products")
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return productService.getProductList(pageNum, pageSize);
    }

    @GetMapping("/product/search")
    public ServerResponse productSearch(String productName,
                                        Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return productService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @PostMapping("/product/img")
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {
        //todo
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }


    @RequestMapping("richtext_img_upload.do")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}


