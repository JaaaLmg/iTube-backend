package com.ja.itubeadmin.controller;

import com.ja.itubecommon.entity.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController extends BaseController{
    @RequestMapping("loadDataList")
    public ResponseVO loadDataList() {
        return getSuccessResponseVO(null);
    }

    @RequestMapping("file")
    public ResponseVO file() {
        return getSuccessResponseVO(null);
    }
}
