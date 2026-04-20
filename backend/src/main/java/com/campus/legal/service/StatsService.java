package com.campus.legal.service;

import com.campus.legal.dto.CategoryViewStatRow;
import com.campus.legal.dto.ContentTypeStatRow;
import com.campus.legal.dto.ContentViewStatRow;
import com.campus.legal.dto.DateCountRow;
import com.campus.legal.dto.QuizStatSummary;
import com.campus.legal.dto.ScoreRangeStatRow;
import com.campus.legal.mapper.EduContentMapper;
import com.campus.legal.mapper.ForumPostMapper;
import com.campus.legal.mapper.ForumReplyMapper;
import com.campus.legal.mapper.QuizAttemptMapper;
import com.campus.legal.mapper.SysUserMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final EduContentMapper eduContentMapper;
    private final QuizAttemptMapper quizAttemptMapper;
    private final SysUserMapper sysUserMapper;
    private final ForumPostMapper forumPostMapper;
    private final ForumReplyMapper forumReplyMapper;

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("MM-dd");

    public StatsBundle overview() {
        StatsBundle b = new StatsBundle();
        b.setContentViews(eduContentMapper.statsViewsByType());
        b.setQuiz(quizAttemptMapper.statsSummary());
        return b;
    }

    @Data
    public static class StatsBundle {
        private List<ContentViewStatRow> contentViews;
        private QuizStatSummary quiz;
    }

    @Data
    public static class NameValueItem {
        private String name;
        private Long value;
    }

    @Data
    public static class UserStatResult {
        private Long totalUsers;
        private Long studentCount;
        private Long teacherCount;
        private Long adminCount;
        private List<NameValueItem> roleDistribution;
        private List<DateCountRow> recent7DayNewUsers;
    }

    @Data
    public static class ContentStatResult {
        private Long totalContents;
        private Long totalViews;
        private List<ContentTypeStatRow> typeStats;
        private List<CategoryViewStatRow> categoryViewStats;
    }

    @Data
    public static class ForumStatResult {
        private Long totalPosts;
        private Long totalReplies;
        private Long recent7DayNewPosts;
        private List<DateCountRow> interactionTrend;
    }

    @Data
    public static class QuizStatResult {
        private Long totalAttempts;
        private BigDecimal avgScore;
        private List<ScoreRangeStatRow> scoreRangeStats;
    }

    public UserStatResult userStat() {
        long student = safeLong(sysUserMapper.countByRole("STUDENT"));
        long teacher = safeLong(sysUserMapper.countByRole("TEACHER"));
        long admin = safeLong(sysUserMapper.countByRole("ADMIN"));

        UserStatResult r = new UserStatResult();
        r.setTotalUsers(safeLong(sysUserMapper.countAllUsers()));
        r.setStudentCount(student);
        r.setTeacherCount(teacher);
        r.setAdminCount(admin);
        r.setRoleDistribution(Arrays.asList(
                nv("学生", student),
                nv("教师", teacher),
                nv("管理员", admin)
        ));
        r.setRecent7DayNewUsers(normalizeRecent7Days(sysUserMapper.recent7DayNewUsers()));
        return r;
    }

    public ContentStatResult contentStat() {
        ContentStatResult r = new ContentStatResult();
        r.setTotalContents(safeLong(eduContentMapper.countPublishedContents()));
        r.setTotalViews(safeLong(eduContentMapper.sumPublishedViews()));
        r.setTypeStats(defaultList(eduContentMapper.statsByType()));
        r.setCategoryViewStats(defaultList(eduContentMapper.statsViewsByCategory()));
        return r;
    }

    public ForumStatResult forumStat() {
        ForumStatResult r = new ForumStatResult();
        r.setTotalPosts(safeLong(forumPostMapper.countAllPosts()));
        r.setTotalReplies(safeLong(forumReplyMapper.countAllReplies()));

        List<DateCountRow> postTrend = normalizeRecent7Days(forumPostMapper.recent7DayPostCounts());
        List<DateCountRow> replyTrend = normalizeRecent7Days(forumReplyMapper.recent7DayReplyCounts());
        List<DateCountRow> interaction = new ArrayList<>();

        long recent7DayNewPosts = 0L;
        for (int i = 0; i < postTrend.size(); i++) {
            DateCountRow p = postTrend.get(i);
            DateCountRow q = replyTrend.get(i);
            long pCount = safeLong(p.getTotalCount());
            long qCount = safeLong(q.getTotalCount());
            recent7DayNewPosts += pCount;

            DateCountRow row = new DateCountRow();
            row.setDateLabel(p.getDateLabel());
            row.setTotalCount(pCount + qCount);
            interaction.add(row);
        }

        r.setRecent7DayNewPosts(recent7DayNewPosts);
        r.setInteractionTrend(interaction);
        return r;
    }

    public QuizStatResult quizStat() {
        QuizStatSummary s = quizAttemptMapper.statsSummary();
        QuizStatResult r = new QuizStatResult();
        r.setTotalAttempts(safeLong(s == null ? null : s.getTotalAttempts()));
        r.setAvgScore(safeDecimal(s == null ? null : s.getAvgScore()));
        r.setScoreRangeStats(normalizeScoreRanges(quizAttemptMapper.statsScoreRange()));
        return r;
    }

    public Map<String, Object> simpleDashboard() {
        Map<String, Object> m = new HashMap<>();
        m.put("contentViews", eduContentMapper.statsViewsByType());
        m.put("quiz", quizAttemptMapper.statsSummary());
        return m;
    }

    private NameValueItem nv(String name, long value) {
        NameValueItem item = new NameValueItem();
        item.setName(name);
        item.setValue(value);
        return item;
    }

    private long safeLong(Long v) {
        return v == null ? 0L : v;
    }

    private BigDecimal safeDecimal(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private <T> List<T> defaultList(List<T> rows) {
        return rows == null ? Collections.emptyList() : rows;
    }

    private List<DateCountRow> normalizeRecent7Days(List<DateCountRow> raw) {
        Map<String, Long> indexed = new LinkedHashMap<>();
        if (raw != null) {
            for (DateCountRow row : raw) {
                if (row == null || row.getDateLabel() == null) {
                    continue;
                }
                indexed.put(row.getDateLabel(), safeLong(row.getTotalCount()));
            }
        }

        List<DateCountRow> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String label = LocalDate.now().minusDays(i).format(DAY_FMT);
            DateCountRow row = new DateCountRow();
            row.setDateLabel(label);
            row.setTotalCount(indexed.getOrDefault(label, 0L));
            result.add(row);
        }
        return result;
    }

    private List<ScoreRangeStatRow> normalizeScoreRanges(List<ScoreRangeStatRow> raw) {
        Map<String, Long> indexed = new HashMap<>();
        if (raw != null) {
            for (ScoreRangeStatRow row : raw) {
                if (row == null || row.getScoreRange() == null) {
                    continue;
                }
                indexed.put(row.getScoreRange(), safeLong(row.getTotalCount()));
            }
        }

        List<String> buckets = Arrays.asList("0-59", "60-69", "70-79", "80-89", "90-100");
        List<ScoreRangeStatRow> result = new ArrayList<>();
        for (String bucket : buckets) {
            ScoreRangeStatRow row = new ScoreRangeStatRow();
            row.setScoreRange(bucket);
            row.setTotalCount(indexed.getOrDefault(bucket, 0L));
            result.add(row);
        }
        return result;
    }
}
