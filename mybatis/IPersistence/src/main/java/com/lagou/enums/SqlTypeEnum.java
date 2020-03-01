/**
 * FileName: SqlTypeEnum.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/1 8:44
 * Description:
 */
package com.lagou.enums;

public enum SqlTypeEnum {

    SELECT("select"),
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    ;

    private String code;

    SqlTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    }
