package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.CLI;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ICLIMappper {

    boolean addCLI(@Param("cli") CLI cli);


}
