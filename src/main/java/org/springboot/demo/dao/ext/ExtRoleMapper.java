package org.springboot.demo.dao.ext;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRoleMapper {
    List<String> selectRoleByUserId(@Param("id") String id);
}
