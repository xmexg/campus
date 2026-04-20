package com.campus.legal.mapper;

import com.campus.legal.dto.ForumPostVO;
import com.campus.legal.entity.ForumPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumPostMapper {
    ForumPost findById(@Param("id") Long id);

    int insert(ForumPost p);

    int update(ForumPost p);

    int delete(@Param("id") Long id);

    int incrementView(@Param("id") Long id);

    List<ForumPostVO> listPublic(@Param("keyword") String keyword);

    List<ForumPostVO> listByUser(@Param("userId") Long userId);

    List<ForumPostVO> listPendingAudit();

    ForumPostVO findPublicVoById(@Param("id") Long id);

    ForumPostVO findVoByIdAndUser(@Param("id") Long id, @Param("userId") Long userId);

    /** 管理端：全部帖子，可筛选状态与关键词 */
    List<ForumPostVO> listAllForMod(@Param("keyword") String keyword, @Param("auditStatus") String auditStatus);
}
