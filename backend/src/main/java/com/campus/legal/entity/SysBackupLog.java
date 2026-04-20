package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysBackupLog {
    private Long id;
    private Long operatorId;
    private String backupType;
    private String filePath;
    private String remark;
    private LocalDateTime createdAt;
}
