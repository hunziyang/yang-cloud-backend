package com.yang.portal.dataScope.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataScopeColumnDto {

    private String tableAlias;
    private String column;
    private String dataScopeType;
}
