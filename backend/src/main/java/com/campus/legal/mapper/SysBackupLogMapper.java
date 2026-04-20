package com.campus.legal.mapper;

import com.campus.legal.entity.SysBackupLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysBackupLogMapper {
    int insert(SysBackupLog log);

    List<SysBackupLog> listRecent(@Param("limit") int limit);
}
