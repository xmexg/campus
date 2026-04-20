import http from './http'

export const auth = {
  login: (data) => http.post('/auth/login', data),
  register: (data) => http.post('/auth/register', data),
}

export const userApi = {
  me: () => http.get('/user/me'),
  profile: (data) => http.put('/user/profile', data),
  password: (data) => http.put('/user/password', data),
  applyTeacher: (data) => http.post('/user/teacher/apply', data),
  teacherInfo: (data) => http.put('/user/teacher/info', data),
}

export const contentApi = {
  search: (params) => http.get('/content/search', { params }),
  detail: (id) => http.get(`/content/${id}`),
  view: (id) => http.post(`/content/${id}/view`),
  comments: (id) => http.get(`/content/${id}/comments`),
  addComment: (id, body) => http.post(`/content/${id}/comments`, { body }),
  teacherSubmit: (data) => http.post('/content/teacher/submit', data),
}

export const forumApi = {
  list: (params) => http.get('/forum/public/posts', { params }),
  detail: (id) => http.get(`/forum/public/posts/${id}`),
  replies: (id) => http.get(`/forum/public/posts/${id}/replies`),
  myPosts: () => http.get('/forum/my/posts'),
  myPostDetail: (id) => http.get(`/forum/posts/${id}/mine`),
  create: (data) => http.post('/forum/posts', data),
  update: (id, data) => http.put(`/forum/posts/${id}`, data),
  reply: (postId, data) => http.post(`/forum/posts/${postId}/replies`, data),
  modPending: () => http.get('/forum/mod/posts/pending'),
  modListAll: (params) => http.get('/forum/mod/posts/all', { params }),
  modUpdatePost: (id, data) => http.put(`/forum/mod/posts/${id}`, data),
  modAudit: (id, data) => http.post(`/forum/mod/posts/${id}/audit`, data),
  modDelPost: (id) => http.delete(`/forum/mod/posts/${id}`),
  modDelReply: (id) => http.delete(`/forum/mod/replies/${id}`),
}

export const quizApi = {
  questions: (limit) => http.get('/quiz/questions', { params: { limit } }),
  submit: (data) => http.post('/quiz/submit', data),
  ranking: (limit) => http.get('/quiz/ranking', { params: { limit } }),
  teacherQuestions: () => http.get('/quiz/teacher/questions'),
  saveQuestion: (data) => http.post('/quiz/teacher/questions', data),
  delQuestion: (id) => http.delete(`/quiz/teacher/questions/${id}`),
  quizStats: () => http.get('/quiz/stats/summary'),
}

export const statsApi = {
  overview: () => http.get('/stats/overview'),
}

export const adminApi = {
  pendingIdentity: () => http.get('/admin/identity/pending'),
  auditIdentity: (userId, data) => http.post(`/admin/identity/${userId}/audit`, data),
  pendingTeacher: () => http.get('/admin/teacher/pending'),
  auditTeacher: (userId, data) => http.post(`/admin/teacher/${userId}/audit`, data),
  users: (params) => http.get('/admin/users', { params }),
  updateUser: (id, data) => http.put(`/admin/users/${id}`, data),
  quizAttempts: (limit) => http.get('/admin/quiz/attempts', { params: { limit } }),
  exportBackup: () => http.post('/admin/backup/export'),
  backupLogs: () => http.get('/admin/backup/logs'),
  contentList: (params) => http.get('/admin/content/list', { params }),
  contentGet: (id) => http.get(`/admin/content/${id}`),
  contentSave: (data) => http.post('/admin/content/save', data),
  contentDel: (id) => http.delete(`/admin/content/${id}`),
  contentPending: () => http.get('/admin/content/submit/pending'),
  contentAudit: (id, data) => http.post(`/admin/content/submit/${id}/audit`, data),
  commentHide: (id, hidden) => http.post(`/admin/content/comment/${id}/hidden?hidden=${hidden}`),
  commentDel: (id) => http.delete(`/admin/content/comment/${id}`),
}

export const fileApi = {
  upload: (file) => {
    const fd = new FormData()
    fd.append('file', file)
    return http.post('/file/upload', fd, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
