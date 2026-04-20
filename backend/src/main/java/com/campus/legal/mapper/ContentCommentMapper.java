package com.campus.legal.mapper;

import com.campus.legal.entity.ContentComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentCommentMapper {
    List<ContentComment> listByContent(@Param("contentId") Long contentId);

    int insert(ContentComment c);

    int setHidden(@Param("id") Long id, @Param("hidden") int hidden);

    int delete(@Param("id") Long id);
}
