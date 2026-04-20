package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.entity.ContentComment;
import com.campus.legal.entity.EduContent;
import com.campus.legal.entity.SysUser;
import com.campus.legal.mapper.ContentCommentMapper;
import com.campus.legal.mapper.EduContentMapper;
import com.campus.legal.mapper.SysUserMapper;
import com.campus.legal.security.LoginUser;
import com.campus.legal.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final EduContentMapper eduContentMapper;
    private final ContentCommentMapper contentCommentMapper;

    @Resource
    private final UserService userService;
    // 修复评论名字
    @Resource
    private final SysUserMapper sysUserMapper;

    public List<EduContent> searchPublic(String keyword, String contentType) {
        return eduContentMapper.search(keyword, contentType, true);
    }

    public EduContent getPublic(Long id) {
        EduContent c = eduContentMapper.findById(id);
        if (c == null || c.getPublished() == null || c.getPublished() != 1) {
            throw new BusinessException("内容不存在");
        }
        if (!"PUBLISHED".equals(c.getSubmitStatus())) {
            throw new BusinessException("内容不存在");
        }
        return c;
    }

    @Transactional
    public void addView(Long id) {
        getPublic(id);  // 获取评论
        eduContentMapper.incrementView(id); // +1阅读量
    }

    public List<ContentComment> listComments(Long contentId) {
        getPublic(contentId);
        return contentCommentMapper.listByContent(contentId);
    }

    @Transactional
    public void addComment(Long contentId, String body) {
        LoginUser lu = SecurityUtils.currentUser();
        if (lu == null) {
            throw new BusinessException(401, "请先登录");
        }
        userService.requireIdentityApproved(lu);
        getPublic(contentId);
        ContentComment c = new ContentComment();
        c.setContentId(contentId);
        c.setUserId(lu.getUserId());
        SysUser u = sysUserMapper.findById(lu.getUserId());
        c.setBody(body);
        c.setHidden(0);
        contentCommentMapper.insert(c);
    }

    @Transactional
    public Long teacherSubmit(EduContent c) {
        Long uid = SecurityUtils.requireUserId();
        c.setCreatorId(uid);
        c.setViewCount(0);
        c.setPublished(0);
        c.setSubmitStatus("PENDING");
        eduContentMapper.insert(c);
        return c.getId();
    }

    @Transactional
    public void adminSave(EduContent c) {
        SecurityUtils.requireUserId();
        if (c.getId() == null) {
            c.setViewCount(0);
            if (c.getPublished() == null) {
                c.setPublished(1);
            }
            if (!StringUtils.hasText(c.getSubmitStatus())) {
                c.setSubmitStatus("PUBLISHED");
            }
            eduContentMapper.insert(c);
        } else {
            eduContentMapper.update(c);
        }
    }

    @Transactional
    public void adminDelete(Long id) {
        eduContentMapper.delete(id);
    }

    public List<EduContent> adminList(String keyword, String contentType) {
        return eduContentMapper.search(keyword, contentType, false);
    }

    public EduContent adminGet(Long id) {
        EduContent c = eduContentMapper.findById(id);
        if (c == null) {
            throw new BusinessException("内容不存在");
        }
        return c;
    }

    @Transactional
    public void auditSubmit(Long id, boolean approve, String remark) {
        EduContent c = eduContentMapper.findById(id);
        if (c == null) {
            throw new BusinessException("内容不存在");
        }
        if (approve) {
            c.setSubmitStatus("PUBLISHED");
            c.setPublished(1);
            c.setSubmitAuditRemark(null);
        } else {
            c.setSubmitStatus("REJECTED");
            c.setPublished(0);
            c.setSubmitAuditRemark(remark);
        }
        eduContentMapper.update(c);
    }

    public List<EduContent> listPendingSubmits() {
        return eduContentMapper.listPendingSubmit();
    }

    @Transactional
    public void setCommentHidden(Long commentId, int hidden) {
        contentCommentMapper.setHidden(commentId, hidden);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        contentCommentMapper.delete(commentId);
    }
}
