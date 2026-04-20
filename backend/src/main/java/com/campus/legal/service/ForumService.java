package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.dto.ForumPostVO;
import com.campus.legal.dto.ForumReplyVO;
import com.campus.legal.entity.ForumPost;
import com.campus.legal.entity.ForumReply;
import com.campus.legal.mapper.ForumPostMapper;
import com.campus.legal.mapper.ForumReplyMapper;
import com.campus.legal.security.LoginUser;
import com.campus.legal.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumPostMapper forumPostMapper;
    private final ForumReplyMapper forumReplyMapper;
    private final UserService userService;

    public List<ForumPostVO> listPublic(String keyword) {
        return forumPostMapper.listPublic(keyword);
    }

    @Transactional
    public ForumPostVO getPublicPost(Long id) {
        ForumPostVO v = forumPostMapper.findPublicVoById(id);
        if (v == null) {
            throw new BusinessException("帖子不存在");
        }
        forumPostMapper.incrementView(id);
        int vc = v.getViewCount() == null ? 0 : v.getViewCount();
        v.setViewCount(vc + 1);
        return v;
    }

    public List<ForumReplyVO> listReplies(Long postId) {
        ForumPostVO v = forumPostMapper.findPublicVoById(postId);
        if (v == null) {
            throw new BusinessException("帖子不存在");
        }
        return forumReplyMapper.listByPost(postId);
    }

    public ForumPostVO myPostDetail(Long id) {
        Long uid = SecurityUtils.requireUserId();
        ForumPostVO v = forumPostMapper.findVoByIdAndUser(id, uid);
        if (v == null) {
            throw new BusinessException("帖子不存在");
        }
        return v;
    }

    public List<ForumPostVO> myPosts() {
        return forumPostMapper.listByUser(SecurityUtils.requireUserId());
    }

    @Transactional
    public Long createPost(String title, String body) {
        LoginUser lu = requireLoginUser();
        userService.requireIdentityApproved(lu);
        ForumPost p = new ForumPost();
        p.setUserId(lu.getUserId());
        p.setTitle(title);
        p.setBody(body);
        p.setAuditStatus("PENDING");
        p.setAuditRemark(null);
        p.setViewCount(0);
        forumPostMapper.insert(p);
        return p.getId();
    }

    @Transactional
    public void updatePost(Long id, String title, String body) {
        LoginUser lu = requireLoginUser();
        ForumPost p = forumPostMapper.findById(id);
        if (p == null || !p.getUserId().equals(lu.getUserId())) {
            throw new BusinessException("无权编辑");
        }
        if ("APPROVED".equals(p.getAuditStatus())) {
            throw new BusinessException("已通过审核的帖子不可直接修改，请联系管理员");
        }
        p.setTitle(title);
        p.setBody(body);
        p.setAuditStatus("PENDING");
        p.setAuditRemark(null);
        forumPostMapper.update(p);
    }

    @Transactional
    public void reply(Long postId, String text, Long parentId) {
        LoginUser lu = requireLoginUser();
        userService.requireIdentityApproved(lu);
        ForumPostVO pub = forumPostMapper.findPublicVoById(postId);
        if (pub == null) {
            throw new BusinessException("帖子不存在或未通过审核");
        }
        ForumReply r = new ForumReply();
        r.setPostId(postId);
        r.setUserId(lu.getUserId());
        r.setParentId(parentId);
        r.setBody(text);
        forumReplyMapper.insert(r);
    }

    public List<ForumPostVO> listPendingAudit() {
        return forumPostMapper.listPendingAudit();
    }

    public List<ForumPostVO> listAllForMod(String keyword, String auditStatus) {
        return forumPostMapper.listAllForMod(
                keyword == null || keyword.isEmpty() ? null : keyword,
                auditStatus == null || auditStatus.isEmpty() ? null : auditStatus);
    }

    @Transactional
    public void adminUpdatePost(Long id, String title, String body) {
        ForumPost p = forumPostMapper.findById(id);
        if (p == null) {
            throw new BusinessException("帖子不存在");
        }
        p.setTitle(title);
        p.setBody(body);
        forumPostMapper.update(p);
    }

    @Transactional
    public void auditPost(Long id, boolean approve, String remark) {
        ForumPost p = forumPostMapper.findById(id);
        if (p == null) {
            throw new BusinessException("帖子不存在");
        }
        if (approve) {
            p.setAuditStatus("APPROVED");
            p.setAuditRemark(null);
        } else {
            p.setAuditStatus("REJECTED");
            p.setAuditRemark(remark);
        }
        forumPostMapper.update(p);
    }

    @Transactional
    public void modDeletePost(Long id) {
        forumReplyMapper.deleteByPost(id);
        forumPostMapper.delete(id);
    }

    @Transactional
    public void modDeleteReply(Long replyId) {
        forumReplyMapper.delete(replyId);
    }

    private LoginUser requireLoginUser() {
        LoginUser lu = com.campus.legal.security.SecurityUtils.currentUser();
        if (lu == null) {
            throw new BusinessException(401, "请先登录");
        }
        return lu;
    }
}
