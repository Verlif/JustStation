# JustStation

基于SpringBoot的开源框架，只包括JavaWeb框架，不包括前端页面与拓展业务实现

[Github主页](https://github.com/Verlif/JustStation) | [Github WIKI](https://github.com/Verlif/JustStation/wiki) | [Gitee主页](https://gitee.com/Verlif/JustStation) | [Gitee WIKI](https://gitee.com/Verlif/JustStation/wikis/Home)

WIKI上有比较详细的功能实现说明。

----

## 概要

1. 框架中几乎所有的功能都是可以快捷自定义的，通过实现特定的接口，并使用`@component`、`@Bean`或是某些特定注解进行功能注入。

2. 框架中使用`Server`作为模块入口，`Handler`作为功能切点，在使用模块功能时，推荐使用`Server`而不是直接使用`Handler`。

----

## 命名说明（后缀）

| | 说明 |
| ---- | ---- |
| Mapper | 对数据的CURD实现类 |
| Server | 对某个功能或是模块的调用实例 |
| Manager | 对某个功能或是模块的对象或是数据的管理，一般不提供调用服务 |
| Handler | 实现某功能的处理接口 |
| Config | 功能或模块的配置参数 |
| Exception | 异常对象 |

----

## 允许自定义的模块

| 模块 | 接口 | 说明 | 内置实现类说明 | 允许数量 |
| ---- | ---- | ---- | ---- | ---- |
| 缓存 | `CacheHandler` | 包括了缓存的常用功能点（put、get、expire等） | 内置了一个内存（默认启用，但没有持久化）缓存与Redis缓存 | 1 |
| URL指令 | `RemCommand` | URL指令的功能实现，需配合`@Rci`注解 | 内置了`HelpCommand`用于展示所有的URL指令 | 1 |
| 异常处理 | `ExceptionHolder` | 注册指定的异常，并添加异常处理 | 内置了很多，写不下了 | 无限 |
| 文件管理 | `FileHandler` | 文件的基础处理（上传、下载、获取等） | 本地文件系统 | 1 |
| 文件管理 | `FileParser4List` | 列表数据文件解析器，需配合`@Parser4List`注解 | 内置了Excel文件解析 | 无限 |
| 文件管理 | `FileParser4Single` | 单体数据文件解析器，需配合`@Parser4Single`注解 | 无 | 无限 |
| 访问限制 | `LimitHandler` | 接口限制器，设定接口拦截策略 | 随机访问、固定窗口访问限制 | 无限 |
| 定时任务 | `@TaskTip` | 定时任务注解，需要实现`Runnable`接口 | 固定窗口访问限制在使用 | 无限 |
| 接口日志 | `LogHandler` | 接口访问前与执行后两个端口 | 控制台输出 | 无限 |
| 通知服务 | `NoticeHandler` | 通知发送处理器，需配合`@NoticeComponent`注解 | 邮件通知（需添加邮箱配置） | 无限 |
| RSA密钥管理 | `RsaServer` | RSA服务，包括了密钥生成、加密解密等 | 一次性解密（每次解密都会销毁密钥Key） | 1 |
| 登录模块 | `BaseUserCollector` | 用户信息获取接口 | 返回null | 1 |
| 登录模块 | `LoginHandler` | 登录处理（登录前、重复登录、登录后与登出处理） | 每个用户的同tag的登录，会挤掉之前的同tag登录标志 | 1 |
| 登录模块 | `TokenHandler` | 登录Token的相关处理 | 使用缓存进行Token存储 | 1 |
| 用户模块 | `PasswordEncoder` | 密码编码 | `BCryptPasswordEncoder` | 1 |
| 权限模块 | `PermissionMapper` | 用户权限获取（只在成功登录后触发） | 所有用户返回空角色与空关键词 | 1 |
| 权限模块 | `PermissionDetector` | 权限判定 | 从用户在登录时获取的角色与关键词判定 | 1 |

----

> 注：因为我英文不好，所以这个Wiki是中文的  
> 注：我也不会写Wiki
