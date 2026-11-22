# WorkBuddy Lite 开发约定

- 后续课程代码在本仓库中逐步演进。可复用的 Java 代码放在 `backend/src/main/java`，测试放在 `backend/src/test/java`，可运行的教学入口放在 `demo` 包。
- 保留学员对现有练习的修改。同一功能在已有代码上扩展，新增模块时按功能组织。
- 提交前在仓库根目录运行 Maven Wrapper 的 `verify`，并验证受影响的教学入口。
- 完成每次课程代码改动后，提交并推送到本仓库的 `origin`。提交范围包括本课相关的源码、测试和运行说明。
