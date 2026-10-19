# Linux 单机部署

先在本地验收，再购买服务器。本目录是部署模板，尚未部署到公网。

1. 使用受支持的 Linux、JDK 8（验证基线；生产部署前选择仍有安全维护的发行版）、Nginx 和合法可用的达梦版本。现有本机达梦启动工具报告许可证 2026-11-02 到期，不能把该安装直接作为长期上线前提。
2. 创建专用系统用户 medicine-demo。目录 /opt/medicine-demo/web 存放前端 dist 内容，/opt/medicine-demo/app.jar 存放后端 JAR；/var/lib/medicine-demo/uploads 归该用户所有。数据库文件另存独立持久化目录。
3. 在全新演示实例建立 DEMO_APP 用户；只导入 private/schema-only.dmp 的表结构，ROWS=N、GRANTS=N、TRIGGERS=N，REMAP_SCHEMA=SYSDBA:DEMO_APP。通过达梦管理工具依次执行 database/001-demo-password.sql 和 database/002-demo-text-width.sql。不要对原库执行初始化。
4. 从 app.env.example 创建 /etc/medicine-demo/app.env，填入随机数据库密码与 JWT_SECRET，文件权限 600。数据库账号仅拥有演示 schema 所需权限，不使用 SYSDBA 运行应用。
5. 首次启动时临时设置 SEED_DEMO=true、SEED_CONFIRM=EMPTY_DEMO_DATABASE、DEMO_ADMIN_PASSWORD 与 DEMO_USER_PASSWORD（均至少12位）。初始化要求用户、分类和药品表为空；完成后移除这些初始化参数再重启。
6. 安装 medicine-demo.service，执行 systemctl daemon-reload，systemctl enable --now medicine-demo。使用 journalctl -u medicine-demo 检查启动日志。
7. 配置域名、有效 TLS 证书，再替换 nginx.conf 中域名及证书路径。配置放入 Nginx http 上下文，执行 nginx -t 后 reload。证书续期后需要 reload。仅开放80/443和受限管理端口，禁止公网访问8081/5236；达梦 dm.ini 设置 LISTEN_IP=127.0.0.1（更改后重启），数据库端口同时通过主机防火墙和云安全组限制。
8. 用 docs/ACCEPTANCE.md 验收。备份数据库及上传目录，备份加密存储并定期验证恢复；不得将 private 或 app.env 放进 web 根目录。

## 构建

前端：npm ci（以 package-lock.json 为准），npm run build:pro。建议使用与本地验证一致的 Node 24 环境；保留锁文件，安装时不更新依赖。
后端：JAVA_HOME 指向完整 JDK，mvn -f mySpringboot/pom.xml clean verify。数据库集成测试需按说明显式启用。
发布产物：mySpringboot/target/mySpringboot-0.0.1-SNAPSHOT.jar 与前端 dist；不打包依赖目录、原库、private、环境配置或开发工具。

## 回滚

每次发布保留上一版 JAR 与 web 目录。失败时恢复上一版、重启 API、reload Nginx。数据库变更前先备份，本次密码字段扩展兼容旧列长度，但不要将不支持哈希校验的旧应用对外开放。
