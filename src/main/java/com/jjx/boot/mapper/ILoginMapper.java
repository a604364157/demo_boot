package com.jjx.boot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author jjx
 */
@Mapper
@SuppressWarnings("unused")
public interface ILoginMapper {

    @Select("SELECT PASSWORD FROM LOGIN_DICT WHERE LOGIN_NO = #{loginNo} ")
    String qryPasswordByLoginNo(String loginNo);

}
