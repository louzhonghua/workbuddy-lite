# WorkBuddy Lite

一个随课程演进的 Java 21 后端与 AI Agent 项目。当前实现了并发任务实验和内存会话管理。

## 构建与测试

在仓库根目录运行 Maven Wrapper。首次运行会下载项目指定的 Maven 和依赖，需要 JDK 21 与网络连接。

```powershell
.\mvnw.cmd verify
```

macOS 或 Linux 使用 `./mvnw verify`。如果 Maven 使用了旧版本 Java，将当前终端的 `JAVA_HOME` 指向 JDK 21。

## 运行示例

完成构建后，在仓库根目录执行下列命令。也可以直接在 IDE 中运行这些类的 `main` 方法。

```powershell
# 第 1 课，最小 Java 程序
java -cp backend/target/classes io.github.louzhonghua.workbuddylite.demo.HelloWorkBuddy

# 第 1 课，先提交全部任务，再等待结果
java -cp backend/target/classes io.github.louzhonghua.workbuddylite.demo.AgentTaskDemo batch

# 第 1 课，每提交一个任务就等待结果
java -cp backend/target/classes io.github.louzhonghua.workbuddylite.demo.AgentTaskDemo seq

# 第 2、4 课，历史快照与共享会话仓库
java -cp backend/target/classes io.github.louzhonghua.workbuddylite.demo.SessionManagerDemo

# 第 3 课，会话 ID 的值相等与哈希碰撞
java -cp backend/target/classes io.github.louzhonghua.workbuddylite.demo.SessionKeyDemo
```

## 项目结构

```text
workbuddy-lite/
├── pom.xml                          # 项目级 Maven 配置
└── backend/                         # Java 后端模块
    ├── pom.xml
    └── src/
        ├── main/java/…/demo/         # 课程运行入口
        ├── main/java/…/session/      # 会话管理与消息模型
        └── test/java/…/session/      # JUnit 5 测试
```

| 课程 | 入口 | 练习内容 |
| --- | --- | --- |
| 01 Java 任务执行 | `HelloWorkBuddy`、`AgentTaskDemo` | 从 `main` 方法入手，对比批量提交和逐个等待。 |
| 02 会话管理 | `SessionManagerDemo` | 创建会话、追加消息、查询历史，观察两次读取之间的快照变化。 |
| 03 会话键 | `SessionKeyDemo` | 对比引用相同与值相等，观察 `HashMap` 如何区分哈希碰撞的键。 |
| 04 接口与构造器注入 | `SessionManagerDemo` | 通过 `SessionRepository` 把会话操作与存储实现分开，验证两个管理器如何共享状态。 |

后续课程继续在这里扩展后端能力，桌面客户端作为同一仓库里的应用加入。
