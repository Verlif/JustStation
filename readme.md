# JustStation

基于SpringBoot的开源框架，只包括JavaWeb框架，不包括前端页面与拓展业务实现。  
为了更方便的功能拓展、更好的维护管理与乱七八糟的需求服务。

[Github主页](https://github.com/Verlif/JustStation) | [Github WIKI](https://github.com/Verlif/JustStation/wiki)
| [Gitee主页](https://gitee.com/Verlif/JustStation) | [Gitee WIKI](https://gitee.com/Verlif/JustStation/wikis/Home)

WIKI上有比较详细的功能实现说明。

----

## 使用

*本框架是辅助型框架，所以需要拉取项目代码，在此代码基础上进行拓展开发*

1. `just-demo`子模块包括了大部分的功能使用演示。
2. 未来可能会出一个包括前端代码的例子，现在只能通过Swagger的文档页面进行功能试验。

* Java版本：1.8+
* Maven版本：3.6.0+
* 需要Lombok插件

----

## 特点

* 低侵入，大部分模块都通过注解或配置文件的方式进行使用，降低了代码量与维护难度
* 自由配置，大部分模块都提供了配置文件选项，可直接通过配置文件控制其中的功能细节。
* 环境分离，所有功能都通过接口自动注入的方式实现，便于使用Spring的方式进行环境分离。
* 数据库不相关，框架逻辑与数据库结构没有关系，需要登录与权限的功能只需要实现接口逻辑即可。
* 遵循COC原则，所有接口都有内置实现类，大部分功能也可以通过配置文件来关闭或启用。
* 低依赖，项目尽量使用spring组件或是Java内置实现，并提供拓展接口，方便自定义替换。

----

## 功能点

* 开放式登录，快速实现单点、互斥、共享等登录模式，密码、令牌等登录方式。
* 权限配置，使用`@Perm`注解来标记接口权限。
* 接口访问限制，可以在不使用中间件的方式下使用`@Limit`注解完成接口访问限制，例如限流。
* 简单的数据脱敏，使用`@Sensitive`注解来标记目标属性，在返回值时自动脱敏。
* 简单的文件管理，支持自定义文件域，功能包括上传、下载、导入、导出等。
* 接口日志记录，使用`@LogIt`注解来标记需要记录的接口。
* 参数检测，使用`validation`进行参数校验，使用注解完成自动入参检测。
* （独立）*组件式全局异常处理，通过实现`ExceptionHolder`来完成特定异常的全局处理。*
* （独立）统一任务调度，定时任务与多线程任务都由`TaskServer`管理。定时任务组件化实现方式，可以在配置文件中配置允许名单与屏蔽名单。
* （独立）~~简单指令（默认关闭），通过`SimCommad`来编写自定义的简单指令。~~
* 接口屏蔽（默认关闭），当前版本可以直接通过配置文件的方式关闭指定API的访问（404）

----

## 技术选型

* 核心框架 - spring boot
* *~~持久层 - mybatis plus、druid~~（移至demo中）*
* 登录模式 - spring-security、token
* 安全相关 - spring-security、RSA加密
* 缓存实现 - 内存非持久化缓存、*~~Redis~~（移至demo中）*
* 日志记录 - 默认java.util.logging
* 调度任务 - ThreadPoolTaskScheduler
* 数据脱敏 - Jackson
* 参数校验 - spring-validation
* API文档 - springfox3、knife4j

----

## 开发ING

* 示例项目 - 博客系统（暂定）
* 代码与框架的规范性调整（持续）
* 将每个功能模块独立为单个依赖  
  [√] [全局异常处理](https://github.com/Verlif/exception-spring-boot-starter.git)  
  [√] [指令生成器](https://github.com/Verlif/JustSimmand)  
  [√] [任务调度服务](https://github.com/Verlif/task-spring-boot-starter.git)
* 拓展文件配置，在不影响性能的情况下拓展每个模块的可配置项（持续）

----

## 举例

以实现权限接口为例，内置的`PermissionDetector`已完成常规的角色判定，所以一般情况下只需要实现`PermissionMapper`即可（内置的`PermissionMapper`对于所有的角色都会返回空角色与空关键词）。

1. 实现`PermissionMapper`，完成权限获取接口。内置的实现类只会返回空列表，也就是没有权限。开发者自己实现的时候，需要加上`@Component`注解将其注入Bean池。

    ```java
    public interface PermissionMapper {

        /**
        * 通过用户名标识获取角色组
        *
        * @param username 用户名
        * @return 用户所在的角色组
        */
        Set<String> getUserRoleSet(String username);

        /**
        * 通过用户名标识获取关键词组
        *
        * @param username 用户名
        * @return 用户所在的关键词组
        */
        Set<String> getUserKeySet(String username);
    }
    ```

2. 实现`PermissionDetector`，完成角色判定。内置了一个实现类，逻辑是从登录用户的缓存中获取权限列表，然后判定。缓存中的权限列表是在登录时获取并加载的，具体细节在内置的`UserDetailsService`
   实现类中。开发者自己实现的时候，需要加上`@Component`注解将其注入Bean池。

    ```java
    public class PermissionDetectorImpl implements PermissionDetector {

        public PermissionDetectorImpl() {
        }

        @Override
        public boolean hasRole(String role) {
            LoginUser<?> loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                throw new CustomException(MessagesUtils.message("result.fail.login.not"));
            }
            return loginUser.getRoleSet().stream().anyMatch(s -> s.equals(role));
        }

        @Override
        public boolean hasKey(String key) {
            LoginUser<?> loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                throw new CustomException(MessagesUtils.message("result.fail.login.not"));
            }
            return loginUser.getKeySet().stream().anyMatch(s -> s.equals(key));
        }

    }
    ```

3. 使用`Perm`注解标记接口，用就完事儿了。具体逻辑在`PermissionHandler`中，需要修改的话可以改这里。

    ```java
        /**
        * 拥有角色[role]时可访问
        */
        @Operation(summary = "拥有角色 - user")
        @Perm(hasRole = "user")
        @GetMapping("/hasRole")
        public BaseResult<Object> hasRole() {
            return new OkResult<>().msg("拥有角色");
        }

        /**
        * 拥有关键词[key]时可访问
        */
        @Operation(summary = "拥有关键词 - key")
        @Perm(hasKey = "key")
        @GetMapping("/hasKey")
        public BaseResult<Object> hasKey() {
            return new OkResult<>().msg("拥有关键词");
        }

        /**
        * 未拥有角色[role]时可访问
        */
        @Operation(summary = "未拥有角色 - user")
        @Perm(noRole = "user")
        @GetMapping("/noRole")
        public BaseResult<Object> noRole() {
            return new OkResult<>().msg("未拥有角色");
        }

        /**
        * 未拥有关键词[key]时可访问
        */
        @Operation(summary = "未拥有关键词 - key")
        @Perm(noKey = "key")
        @GetMapping("/noKey")
        public BaseResult<Object> noKey() {
            return new OkResult<>().msg("未拥有关键词");
        }
    ```

----

## 测试

项目中编写了一些测试接口与指令，方便测试与开发 项目中集成了springfox3与knife4j，可以通过 <http://127.0.0.1:8888/doc.html> 访问接口文档

----

> 注：因为我英文不好，所以这个Wiki是中文的  
> 注：我也不会写Wiki
