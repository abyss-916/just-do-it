![构建](https://github.com/abyss-916/just-do-it/actions/workflows/build.yml/badge.svg)
![测试](https://github.com/abyss-916/just-do-it/actions/workflows/tests.yml/badge.svg)
![发布](https://github.com/abyss-916/just-do-it/actions/workflows/release.yml/badge.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/abyss-916/just-do-it/badge/main)](https://www.codefactor.io/repository/github/abyss-916/just-do-it/overview/main)
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>

<img src="desktop-app/src/desktopMain/resources/ic_launcher.png" width="256" alt="Just Do It 图标">

# Just Do It - 多平台

Just Do It 是一款任务管理应用，用于学习和实践最新的多平台开发组件、架构和工具。项目支持 Android 和桌面端！❤️

整个应用已完全使用 **Kotlin 和 Compose Multiplatform** 构建！

## 📚 技术栈

Just Do It 的目标是使用所有最新的库和工具。

### 🧑🏻‍💻 多平台开发

- 应用完全使用 [Kotlin](https://kotlinlang.org) 编写
- UI 使用 [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) 开发
- UI 测试使用 [Compose Multiplatform UI Testing](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html)
- 遵循 [Material You](https://m3.material.io/) 设计规范，支持动态颜色（仅 Android）
- 使用 [Compose Adaptive](https://developer.android.com/develop/ui/compose/layouts/adaptive) 实现自适应布局
- 使用 [Navigation3](https://developer.android.com/jetpack/compose/navigation) 进行导航管理
- 使用 [Coroutines](https://kotlin.github.io/kotlinx.coroutines/) 进行异步处理
- 使用 [Jetpack Glance](https://developer.android.com/jetpack/androidx/releases/glance) 开发桌面小组件
- 使用 [Koin](https://insert-koin.io) 进行依赖注入
- 使用 [SQLDelight](https://github.com/cashapp/sqldelight) 作为数据库
- 使用 [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) 进行本地存储
- 使用 [AboutLibraries](https://github.com/mikepenz/AboutLibraries) 管理开源许可证

有关项目使用的所有依赖，请访问
[依赖文件](https://github.com/abyss-916/just-do-it/blob/main/gradle/libs.versions.toml)

如果想查看之前的版本，请参阅
[V1](https://github.com/abyss-916/just-do-it/tree/v1.7.0) 或 [V2](https://github.com/abyss-916/just-do-it/tree/v2.3.0) 历史版本。

### 🧪 代码质量

- [ktlint](https://github.com/pinterest/ktlint)
- [detekt](https://github.com/arturbosch/detekt)
- [compose-rules](https://github.com/twitter/compose-rules)
- [lint](https://developer.android.com/studio/write/lint)
- [CodeFactor](https://www.codefactor.io/)

## 🏛 架构

Just Do It 的架构基于 [六边形架构](https://alistair.cockburn.us/hexagonal-architecture/)
由 Alistair Cockburn 提出。应用还大量使用了模块化设计以更好地分离关注点和封装。

以下是应用各主要模块的说明：

* **app** 和 **desktop-app** — 平台特定的入口模块，包含各平台的初始化逻辑
* **shared** — 所有平台共享代码的模块
* **features** — 包含应用各功能（视觉或非视觉）的模块
* **domain** — 包含业务逻辑的模块，这些模块只依赖自身，所有交互通过依赖倒置完成
* **data** — 包含数据逻辑（本地存储、仓库等）的模块
* **libraries** — 包含小型工具库的模块，如设计系统、导航、测试等

这种架构保护了应用中最重要的模块。为此，所有依赖都指向中心，模块组织方式遵循 **越靠近中心的模块越重要** 的原则。
