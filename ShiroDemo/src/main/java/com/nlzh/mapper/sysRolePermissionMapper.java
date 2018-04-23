package com.nlzh.mapper;

import com.nlzh.pojo.sysRolePermission;
import com.nlzh.pojo.sysRolePermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface sysRolePermissionMapper {
    int countByExample(sysRolePermissionExample example);

    int deleteByExample(sysRolePermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(sysRolePermission record);

    int insertSelective(sysRolePermission record);

    List<sysRolePermission> selectByExample(sysRolePermissionExample example);

    sysRolePermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") sysRolePermission record, @Param("example") sysRolePermissionExample example);

    int updateByExample(@Param("record") sysRolePermission record, @Param("example") sysRolePermissionExample example);

    int updateByPrimaryKeySelective(sysRolePermission record);

    int updateByPrimaryKey(sysRolePermission record);
}