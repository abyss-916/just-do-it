# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 提供在此仓库中工作的指导信息。

这是一款使用 Kotlin 和 Compose Multiplatform 构建的 Kotlin Multiplatform (KMP) 任务管理应用，支持 Android、iOS 和桌面端。

仓库地址: https://github.com/abyss-916/just-do-it

## 构建与开发命令

```bash
# 构建
./gradlew :desktop-app:assemble       # 桌面端（最快）
./gradlew :app:assembleDebug          # Android debug APK

# 测试
./gradlew desktopTest                 # 桌面端单元测试（迭代最快）
./gradlew :features:task:desktopTest  # 单个模块测试
./gradlew :domain:test                # 领域层测试

# 代码质量
./gradlew ktlintFormat                # 自动修复 lint 问题
./gradlew :desktop-app:ktlint         # Lint 检查
./gradlew :desktop-app:detekt         # 静态分析
./gradlew :desktop-app:check          # ktlint + detekt
```

## 架构

采用依赖向内指向的六边形架构：

```
Platform Apps (app, ios-app, desktop-app)
  └─ shared (入口点, Koin 初始化, AlkaaMultiplatformApp)
      └─ features (UI/展示层, ViewModels, 导航)
          └─ domain (用例, 模型, 仓库接口)
              └─ data (仓库实现, 本地/SQLDelight, 数据存储)
                  └─ libraries (designsystem, coroutines, permission 等)
```

**功能模块采用 API/impl 分离模式**：例如 `features:task-api` 暴露接口，`features:task` 提供通过 Koin 绑定的具体实现。其他功能模块仅依赖 API 模块。

**数据层包含三个子层**：
1. `data/local/` — SQLDelight 数据库，包含 DAO、本地数据源、映射器
2. `data/datastore/` — 基于 DataStore 和 Protobuf 的偏好设置
3. `data/repository/` — 组合本地 + 数据存储来源的仓库实现

**模型流向**：领域模型 → 仓库模型 → 本地模型（每层都有自己的模型类和映射器）。

## 模块结构

- `app/`, `desktop-app/`, `ios-app/` — 平台入口点，将多平台应用接入各平台
- `shared/` — 多平台应用根模块；通过 `KoinHelper.kt` 进行 Koin 初始化，包含 `AlkaaMultiplatformApp`
- `features/` — 所有功能模块；每个功能有 API 模块（共享接口）和 impl 模块（Koin 绑定的实现）
  功能包括：task, category, alarm, search, preference, tracker, home, navigation, glance
- `domain/` — 用例、领域模型和仓库接口；无框架依赖
- `data/repository/` — 仓库实现，通过映射器桥接领域和本地模型
- `data/local/` — SQLDelight 数据库 schema、DAO 和本地数据源
- `data/datastore/` — 用户偏好设置通过 DataStore 存储（`alkaa_settings.preferences_pb`）
- `libraries/` — 跨功能使用的共享工具库：designsystem (Kuvio), coroutines, navigation, test, parcelable, permission, appstate
- `plugins/` — Gradle 约定插件（`com.escodro.multiplatform`, `com.escodro.kotlin-quality`, `com.escodro.kotlin-parcelable`），统一各模块的构建配置
- `resources/` — Compose Multiplatform 共享资源（字符串、drawable），供所有平台使用

### 功能模块结构

每个功能模块遵循以下模式：
- `*-api/` 模块：暴露接口、模型、ViewModel 供其他功能依赖
- Impl 模块：包含 DI 模块、ViewModel、UI Composable、映射器、模型、导航
- 平台特定的源集（`androidMain`, `desktopMain`, `iosMain`）按需使用
- 公共测试位于 `commonTest`

### 核心功能模块

- **home** — 主框架与导航套件（`HomeScreen`）
- **task** — 任务增删改查：`TaskListViewModel`, `TaskDetailViewModel`, `AddTaskViewModel`, `TaskAlarmViewModel`
- **category** — 分类管理：`CategoryListViewModel`
- **alarm** — 闹钟/通知调度：`AlarmInteractorImpl`, `NotificationInteractorImpl`, `NotificationScheduler`
- **search** — 任务搜索：`SearchScreen`, `SearchViewModel`
- **preference** — 设置页：`PreferenceScreen`, `PreferenceViewModel`, 关于、开源许可证
- **tracker** — 任务完成统计：`TrackerScreen`, `TaskGraph` 图表组件
- **navigation** — Navigation3 封装：`Navigation` composable, `NavEventControllerImpl`
- **glance** — 仅 Android 的 Jetpack Glance 桌面小组件

## Koin 依赖注入

- Koin 初始化位于 `shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`
- 模块列表定义在 `SharedModule.kt`：`sharedModule`, `taskModule`, `alarmModule`, `categoryModule`, `searchModule`, `preferenceModule`, `domainModule`, `repositoryModule`, `localModule`, `dataStoreModule`, `coroutinesModule`, `designSystemModule`, `navigationModule`, `permissionModule`, `trackerModule`
- 各功能模块的平台特定 DI 模块位于对应的平台源集（如 `androidMain`, `desktopMain`, `iosMain`）

## 导航

使用 **Navigation3**（navigation3-runtime, navigation3-ui）配合自定义封装：

- `Destination` 密封接口，带有 `Back`, `TopLevel` 标记
- `HomeDestination`: TaskList, Search, CategoryList, Preferences（均为 TopLevel）
- `NavBackStack<Destination>` 管理回退栈
- `NavEventController` 发送导航事件
- `NavGraph` 接口用于功能注册
- `Navigation` composable 封装 `NavDisplay`，配合 `DialogSceneStrategy` 用于对话框
- 标记：`TopLevel`, `TopAppBarVisible`

## 数据库 Schema (SQLDelight)

**Task 表**: task_id (主键), task_is_completed, task_title, task_description, task_category_id (外键 → Category), task_due_date, task_creation_date, task_completed_date, task_is_repeating, task_alarm_interval

**Category 表**: category_id (主键), category_name, category_color

**TaskWithCategory**: Task 和 Category 的 JOIN 查询

SQLDelight schema 位于 `data/local/src/commonMain/sqldelight/com/escodro/local/`：`Task.sq`, `Category.sq`, `TaskWithCategory.sq`

## KMP 约定

**依赖 — 版本目录**：所有依赖声明在 `gradle/libs.versions.toml` 中，构建文件中引用为 `alias(libs.plugins.*)` 或 `implementation(libs.*)`。不使用原始坐标字符串。

**构建配置 — 约定插件**：应用适当的插件而非手写构建模板。常用插件：
- `alias(libs.plugins.escodro.multiplatform)` — 标准 KMP 模块设置
- `alias(libs.plugins.escodro.kotlin.parcelable)` — 添加 `@CommonParcelize` 支持
- `alias(libs.plugins.compose)` + `alias(libs.plugins.compose.compiler)` — Compose Multiplatform

**平台特定代码 — `expect`/`actual`**：在 `commonMain` 中使用 `expect` 声明，在平台源集（`androidMain`, `iosMain`, `jvmMain`）中提供 `actual` 实现。绝不在 `commonMain` 中直接使用平台特定 API。

**Gradle 属性**：JVM 参数 `-Xmx6144m`，启用并行构建，启用配置缓存，启用缓存，启用 iOS 实验性 UIKit。

## 测试方案

- **单元测试**：domain, task, local, shared 模块的 `commonTest`
- **E2E/仪器化测试**：`shared/src/commonTest/` 包含 `HomeScreenTest`, `CategoryFlowTest`, `TaskFlowTest`, `SearchFlowTest`, `PreferenceFlowTest`, `TrackerFlowTest`
- **UI 测试**：`features/task/src/commonTest/` 包含闹钟、分类、任务详情、任务列表流程的仪器化测试
- **Fake 实现**：大量使用 Fake 实现进行测试（RepositoryFakes, SchedulerFakes 等）

## 核心依赖

- **Kotlin**: 2.3.21
- **Compose Compiler**: 1.10.3
- **Compose Material3**: 1.10.0-alpha05
- **Koin**: 4.2.1 (koin-core, koin-compose-viewmodel)
- **SQLDelight**: 2.3.2
- **Navigation3**: 1.0.0-alpha06
- **Coroutines**: 1.10.2
- **Serialization**: 1.11.0
- **DateTime**: 0.8.0-0.6.x-compat
- **Immutable Collections**: 0.4.0
- **DataStore**: 1.2.1
- **Moko Permissions**: 0.20.1
- **Quality**: ktlint 1.8.0, detekt 2.0.0-alpha.2, compose-rules 0.5.8
