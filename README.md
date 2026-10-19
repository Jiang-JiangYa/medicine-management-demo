# 药品管理毕业设计 · 求职演示版

Vue 3 + Spring Boot 2.7 + 达梦。保留原页面与药品业务，当前本机演示使用原毕设业务数据副本，保留独立演示账号。

## 当前本机使用

访问 http://127.0.0.1:4173 。账号为 `demo_user` 和 `demo_admin`，随机密码保存在 `private/local-config.json` 的 `DEMO_USER_PASSWORD` / `DEMO_ADMIN_PASSWORD` 中。不要上传该文件或整个 private 目录。

本地数据库是本次新建的 `private/DEMODB`，连接地址为127.0.0.1:5237（已设置LISTEN_IP，但系统仍报告泛地址监听，部署时必须验证防火墙隔离）；原库在5236，已仅导出表结构，已只读复制14张业务表到演示库，未复制原用户账号或修改原表。

- 启动数据库：在 PowerShell 执行 `Start-Process -FilePath 'D:\dmdbms\bin\dmserver.exe' -ArgumentList ('"' + (Join-Path $PWD 'private\DEMODB\dm.ini') + '"') -WorkingDirectory 'D:\dmdbms\bin' -WindowStyle Hidden`。
- 启动网站：`./tools/start-local.ps1`，需要完整 JDK（默认使用本机 D:\Program Files\Java\jdk1.8.0_152）和 Python。
- 原库结构：`private/schema-only.dmp`，导出15张表，明确 `ROWS=N`；核对结果在 `private/schema-report.md`。其中2张是原库附加表，代码映射需要13张。后续已按用户要求复制原业务数据，核对见 private/existing-data-report.md。
- `-Seed` 只用于空演示库的首次初始化；当前已初始化，后续启动不要添加。
- 停止网站：读取 `private/local-pids.json` 后停止对应的本次 Java/Python 进程；停止数据库应使用达梦正常关闭方式，不要强制结束进程。

本地脚本未注册开机启动服务，电脑重启后需要重新启动；正式Linux部署使用systemd。

## 验证

本机已完成类型检查、生产打包、后端9项测试，以及登录权限、库存单据、效期、文件和重启检查。详细证据与限制见 [实施状态](docs/IMPLEMENTATION_STATUS.md)。

当前副本可只读核对：`python tools/check_existing_data.py`。原虚拟数据阶段脚本：`python tools/smoke_api.py`、`python tools/business_smoke.py`、`python tools/file_smoke.py`、`python tools/expiry_smoke.py`。当前已切换原业务数据副本，业务和效期写入测试已禁用，避免污染展示数据。`python tools/restart_check.py` 核对最近一次业务和文件测试留下的结果。

## 上线

[Linux部署说明](deploy/README.md)提供 Nginx、HTTPS、systemd、外部配置及数据库迁移步骤。公网服务器、域名和证书尚未配置，当前网址只在本机可访问。待服务器就绪后完成部署侧验收。

本机达梦工具报告许可证将于2026-11-02到期；上线需要确认服务器达梦版本及有效许可。当前验证使用旧JDK和既有依赖，尚未进行完整依赖漏洞审计或真实业务认证。
