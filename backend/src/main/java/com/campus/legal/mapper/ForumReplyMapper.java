package com.campus.legal.mapper;

import com.campus.legal.dto.ForumReplyVO;
import com.campus.legal.entity.ForumReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumReplyMapper {
    int insert(ForumReply r);

    List<ForumReplyVO> listByPost(@Param("postId") Long postId);

    int delete(@Param("id") Long id);

    int deleteByPost(@Param("postId") Long postId);
}
