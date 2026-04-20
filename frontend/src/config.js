/** 管理端独立站点（默认与 admin 项目 dev 端口一致） */
export const adminBase = (import.meta.env.VITE_ADMIN_BASE || 'http://localhost:5174').replace(/\/$/, '')
