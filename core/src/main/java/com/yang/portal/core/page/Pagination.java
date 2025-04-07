package com.yang.portal.core.page;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class Pagination {

    @Min(1)
    private Integer pageNum = 1;

    @Max(1000)
    private Integer pageSize = 20;

    @Valid
    private List<Sort> sorts;

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }


    @Data
    public static class Sort{
        @NotBlank(message = "排序字段不能为空")
        private String field;
        private String direction = "desc";
    }

}
