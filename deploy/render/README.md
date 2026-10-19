# Render 免费演示部署

本路径使用 PostgreSQL 演示副本；本机原达梦数据库保持不变。选择 Free 计算方案，数据库有效期30天；本次创建的数据库界面到期日为2026-11-19。不要升级付费方案，除非获得用户另行授权。

## 结构

单个免费 Docker Web Service：Nginx 托管 Vue 并将 /api 转发至容器内 Spring Boot；外部连接走 Render HTTPS。PostgreSQL 与网站同在 Oregon，网站使用数据库内网连接。

环境变量（由控制台设置，绝不提交真实值）：

- SPRING_PROFILES_ACTIVE=postgres
- JDBC_DATABASE_URL=jdbc:postgresql://内网主机:5432/数据库名?sslmode=require
- DB_USERNAME、DB_PASSWORD：新 PostgreSQL 凭据
- JWT_SECRET：独立随机密钥，至少32字符
- SEED_DEMO=false
- UPLOAD_ENABLED=false：免费实例无持久磁盘，关闭文件上传，避免误以为文件会长期保存

Dockerfile 位于仓库根目录；健康检查路径 /api/health；Nginx 监听 Render 注入的 PORT。Java 堆上限256MB，连接池最多3个连接。

## 数据迁移

只读导出本机5237端口 DEMO_APP 的14张业务表和2个演示账号。原5236端口 SYSDBA 用户表不迁移。ExportPostgresDemo 将表/列名规范化为 PostgreSQL 小写、映射字段类型、保留主键及账号哈希，并修复用户自增序列。SQL 快照保存在 private，不进 GitHub 或镜像。

向全新空 PostgreSQL 导入；ImportPostgresDemo 检测非空库会拒绝覆盖。导入后核对37条药品、19个分类、30条药库库存、33条批次和2个演示账号。原备份和导出报告保留本机。

编译迁移工具：javac -encoding UTF-8 -d private tools/CopyExistingBusinessData.java tools/ExportPostgresDemo.java tools/ImportPostgresDemo.java

导出需要达梦JDBC驱动与 DEMO_DB_PASSWORD；导入需要 PostgreSQL JDBC 驱动和 JDBC_DATABASE_URL、DB_USERNAME、DB_PASSWORD。不要将凭据写进命令行参数或日志。

## 验收与限制

验证 HTTPS 首页、/api/health、两个账号登录、药品查询分页与筛选、库存及单据读取、未登录/普通账号权限、Cookie Secure属性。保留本地达梦服务用于对照，不对原数据做写入验收。

免费 Web Service 闲置15分钟后会休眠，首次访问需要等待唤醒。数据库30天到期，需在到期前另行决定迁移或升级，不承诺永久在线。现有 mineral-dashboard 服务保持原样。
