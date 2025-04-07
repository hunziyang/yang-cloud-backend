package com.yang.portal.user.service.impl.postService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

@Data
public class PostSelectVo {

    private String name;
    private String description;
    private Pagination pagination = new Pagination();
}
