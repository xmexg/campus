package com.campus.legal.mapper;

import com.campus.legal.dto.ContentViewStatRow;
import com.campus.legal.entity.EduContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduContentMapper {
    EduContent findById(@Param("id") Long id);

    int insert(EduContent c);

    int update(EduContent c);

    int delete(@Param("id") Long id);

    // +1阅读量
    int incrementView(@Param("id") Long id);

    List<EduContent> search(@Param("keyword") String keyword, @Param("contentType") String contentType,
                            @Param("onlyPublished") Boolean onlyPublished);

    List<EduContent> listPendingSubmit();

    List<ContentViewStatRow> statsViewsByType();
}
