package org.springboot.demo.dao.ext;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtPermissionMapper {

    List<String> selectPermissionByUserId(@Param("userId") String userId);
}
