# TODO Issues

## 1. 用户自定义分类名称自动翻译为英文

**状态：** 待定

**描述：** 当前数据库中，用户自定义的分类名称以原文存储（如"运动"、"健身"等），只有预装默认分类（Personal、Work、Shopping List）保证为英文 key。

**影响：** 用户自定义分类在切换语言后不会自动本地化显示。

**可行方案：**
- 集成在线翻译 API（如 Google Translate、DeepL）—— 需要网络和 API key
- 本地维护中文→英文词汇映射表 —— 只能覆盖常用词

**优先级：** 低，不影响大作业核心功能

---

## 2. iOS 运行时语言切换

**状态：** 暂不支持

**描述：** iOS 平台的 `NSUserDefaults AppleLocale` 设置在运行时不生效，系统限制导致需要重启 app 才能切换语言。iOS 13+ 后 Apple 限制了程序化语言切换。

**影响：** iOS 端语言切换功能不可用。

**可行方案：**
- iOS 端使用 `Bundle.main.localizations` + 自定义 Bundle 机制实现运行时切换
- 或者 iOS 端提示用户需要重启 app 生效

**优先级：** 低，当前专注 Android 和 PC 端
