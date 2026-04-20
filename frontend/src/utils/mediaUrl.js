/**
 * 将后端返回的静态资源路径转为当前站点可访问的 URL（开发环境 Vite 代理 /api）
 */
export function resolveApiStaticUrl(path) {
  if (path == null || path === '') return ''
  const s = String(path).trim()
  if (/^https?:\/\//i.test(s)) return s
  let p = s.startsWith('/') ? s : `/${s}`
  if (p.startsWith('/api/')) return p
  if (p.startsWith('/uploads/')) return `/api${p}`
  return p
}
