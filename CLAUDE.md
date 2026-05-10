# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

This is a Kotlin Multiplatform (KMP) task management app targeting Android, iOS, and Desktop, built entirely in Kotlin using Compose Multiplatform.

Repository: https://github.com/igorescodro/alkaa

## Build & Development Commands

```bash
# Build
./gradlew :desktop-app:assemble       # Desktop (fastest)
./gradlew :app:assembleDebug          # Android debug APK

# Test
./gradlew desktopTest                 # Desktop unit tests (fastest for iteration)
./gradlew :features:task:desktopTest  # Single module test
./gradlew :domain:test                # Domain layer tests

# Code quality
./gradlew ktlintFormat                # Auto-fix lint
./gradlew :desktop-app:ktlint         # Lint check
./gradlew :desktop-app:detekt         # Static analysis
./gradlew :desktop-app:check          # ktlint + detekt
```

## Architecture

Hexagonal architecture with inward-pointing dependencies:

```
Platform Apps (app, ios-app, desktop-app)
  └─ shared (entry point, Koin init, AlkaaMultiplatformApp)
      └─ features (UI/presentation, ViewModels, navigation)
          └─ domain (use cases, models, repository interfaces)
              └─ data (repository impl, local/SQLDelight, datastore)
                  └─ libraries (designsystem, coroutines, permission, etc.)
```

**Feature modules use API/impl split**: e.g. `features:task-api` exposes interfaces, `features:task` provides implementations bound via Koin. Other features depend only on API modules.

**Data layer has three sub-layers**:
1. `data/local/` — SQLDelight database with DAOs, local data sources, mappers
2. `data/datastore/` — Protobuf-based preferences via DataStore
3. `data/repository/` — Repository implementations combining local + datastore sources

**Model flow**: Domain models → Repository models → Local models (each layer has its own model class + mapper).

## Module Structure

- `app/`, `desktop-app/`, `ios-app/` — Platform entry points; wire the multiplatform app into each target
- `shared/` — Multiplatform app root; Koin initialization via `KoinHelper.kt`, hosts `AlkaaMultiplatformApp`
- `features/` — All feature modules; each feature has an API module (shared interfaces) and an impl module (Koin-bound implementations). Features: task, category, alarm, search, preference, tracker, home, navigation, glance
- `domain/` — Use cases, domain models, and repository interfaces; no framework dependencies
- `data/repository/` — Repository implementations with mappers bridging domain and local models
- `data/local/` — SQLDelight database schema, DAOs, and local data sources
- `data/datastore/` — User preferences stored via DataStore (`alkaa_settings.preferences_pb`)
- `libraries/` — Shared utilities used across features: designsystem (Kuvio), coroutines, navigation, test, parcelable, permission, appstate
- `plugins/` — Gradle convention plugins (`com.escodro.multiplatform`, `com.escodro.kotlin-quality`, `com.escodro.kotlin-parcelable`) that standardize build configuration across modules
- `resources/` — Compose Multiplatform shared resources (strings, drawables) for all platforms

### Feature Module Structure

Each feature follows this pattern:
- `*-api/` module: Exposes interfaces, models, ViewModels that other features depend on
- Impl module: Contains DI module, ViewModels, UI composables, mappers, models, navigation
- Platform-specific source sets (`androidMain`, `desktopMain`, `iosMain`) when needed
- Common tests in `commonTest`

### Key Feature Modules

- **home** — Main scaffold with navigation suite (`HomeScreen`)
- **task** — Task CRUD: `TaskListViewModel`, `TaskDetailViewModel`, `AddTaskViewModel`, `TaskAlarmViewModel`
- **category** — Category management: `CategoryListViewModel`
- **alarm** — Alarm/notification scheduling: `AlarmInteractorImpl`, `NotificationInteractorImpl`, `NotificationScheduler`
- **search** — Task search: `SearchScreen`, `SearchViewModel`
- **preference** — Settings: `PreferenceScreen`, `PreferenceViewModel`, About, Open Source licenses
- **tracker** — Task completion tracking: `TrackerScreen`, `TaskGraph` chart component
- **navigation** — Navigation3 wrapper: `Navigation` composable, `NavEventControllerImpl`
- **glance** — Android-only Jetpack Glance home widgets

## Koin Dependency Injection

- Koin initialization in `shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`
- Module list defined in `SharedModule.kt`: `sharedModule`, `taskModule`, `alarmModule`, `categoryModule`, `searchModule`, `preferenceModule`, `domainModule`, `repositoryModule`, `localModule`, `dataStoreModule`, `coroutinesModule`, `designSystemModule`, `navigationModule`, `permissionModule`, `trackerModule`
- Platform-specific DI modules in each feature's platform source sets (e.g., `androidMain`, `desktopMain`, `iosMain`)

## Navigation

Uses **Navigation3** (navigation3-runtime, navigation3-ui) with a custom wrapper:

- `Destination` sealed interface with `Back`, `TopLevel` markers
- `HomeDestination`: TaskList, Search, CategoryList, Preferences (all TopLevel)
- `NavBackStack<Destination>` manages back stack
- `NavEventController` sends navigation events
- `NavGraph` interface for feature registration
- `Navigation` composable wraps `NavDisplay` with `DialogSceneStrategy` for dialogs
- Markers: `TopLevel`, `TopAppBarVisible`

## Database Schema (SQLDelight)

**Task table**: task_id (PK), task_is_completed, task_title, task_description, task_category_id (FK → Category), task_due_date, task_creation_date, task_completed_date, task_is_repeating, task_alarm_interval

**Category table**: category_id (PK), category_name, category_color

**TaskWithCategory**: JOIN query between Task and Category

SQLDelight schemas at `data/local/src/commonMain/sqldelight/com/escodro/local/`: `Task.sq`, `Category.sq`, `TaskWithCategory.sq`

## KMP Conventions

**Dependencies — Version Catalog**: All dependencies are declared in `gradle/libs.versions.toml` and referenced as `alias(libs.plugins.*)` or `implementation(libs.*)` in build files. Never use raw coordinate strings.

**Build config — Convention Plugins**: Apply the appropriate plugin instead of writing raw build boilerplate. Common plugins:
- `alias(libs.plugins.escodro.multiplatform)` — standard KMP module setup
- `alias(libs.plugins.escodro.kotlin.parcelable)` — adds `@CommonParcelize` support
- `alias(libs.plugins.compose)` + `alias(libs.plugins.compose.compiler)` — Compose Multiplatform

**Platform-specific code — `expect`/`actual`**: Use `expect` declarations in `commonMain` and `actual` implementations in platform source sets (`androidMain`, `iosMain`, `jvmMain`). Never use platform-specific APIs directly in `commonMain`.

**Gradle properties**: JVM args `-Xmx6144m`, parallel builds enabled, config cache enabled, caching enabled, iOS experimental UIKit enabled.

## Testing Approach

- **Unit tests**: `commonTest` in domain, task, local, shared modules
- **E2E/Instrumented tests**: `shared/src/commonTest/` has `HomeScreenTest`, `CategoryFlowTest`, `TaskFlowTest`, `SearchFlowTest`, `PreferenceFlowTest`, `TrackerFlowTest`
- **UI tests**: `features/task/src/commonTest/` has instrumented tests for alarm, category, task detail, task list flows
- **Fakes**: Extensive use of fake implementations for testing (RepositoryFakes, SchedulerFakes, etc.)

## Key Dependencies

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
