# JustStation

基于SpringBoot的开源框架，只包括JavaWeb框架，不包括前端页面与拓展业务实现

[Github主页](https://github.com/Verlif/JustStation) | [Github WIKI](https://github.com/Verlif/JustStation/wiki) | [Gitee主页](https://gitee.com/Verlif/JustStation) | [Gitee WIKI](https://gitee.com/Verlif/JustStation/wikis/Home)

WIKI上有比较详细的功能实现说明。

----

## 说明

1. 这是一个基础框架，所以肯定会有无数个没有考虑到的地方，后面也会不定期更新。
2. 一些功能的实现或灵感来源于GitHub、CSDN、简书、若依、mayfly、oxygen等，所有引入的其他作者的开源代码在协议允许的情况下，都会根据此项目需要有所更改。
3. 注意：当前默认的缓存机制是使用的MemCache，没有持续化处理。内置了一个Redis实现的CacheHandler，需要的话
，请在RedisCache上添加`@Component`注解以更改。或是使用自己的缓存机制，实现`CacheHandler`接口并标记为Bean。
4. 我个人是比较喜欢用空间来换时间，所以框架内的大部分实现都是在尽量不生成新对象的前提下，取变量来换取运算时间。
5. 大部分的功能用例都可以在`idea.verlif.juststation.core.test`包下找到

----

## 使用

*本框架采用即开即用方式，所以需要拉取项目代码，在此代码基础上进行开发*

1. 通过test包下`idea.verlif.juststation.core.test`的模拟业务代码进行测试（模拟业务中使用了MySQL数据库，所以需要配置application-druid参数。数据库建表sql在sql包下。）
2. 框架中没有前端代码，需要浏览器访问本地链接<localhost:8888/index.html>打开swagger文档进行接口测试。
3. `idea.verlif.juststation.core.test`包仅用于测试与演示，可直接删除。

----

## 特点

* 简化与易用，将常用功能通过注解的方式实现，并通过接口的方式进行功能实现，降低了代码量与维护难度。
* 拓展性，为常用功能，例如日志、限流等提供了处理器选项，可以针对不同接口选择不同的处理类。
* 环境分离，得益于接口注入方式，可以通过`@Profile`注解在不同环境下配置不同实现类与配置，方便开发与测试。
* 数据库不相关，框架逻辑与数据库结构没有关系，需要登录与权限的功能只需要实现接口逻辑即可。（pom中的数据库相关依赖是为了测试Controller，不需要可直接去除）
* 遵循COC原则（应该吧），所有接口也都有内置实现类，不需要特殊处理的话，可以直接使用。

----

## 功能点

* 标识化登录模块，支持单点登录、多设备登录、共享登录等登录模式。
* 注解化权限配置，使用`@Perm`注解来标记接口权限。
* 接口访问限制，允许不同接口配置不同的限制方案，支持限流、特殊标识接口。
* 简单的数据脱敏，使用`@Sensitive`注解来标记目标属性，在返回值时自动脱敏。
* 简单的文件管理，支持自定义文件域，包括上传、下载、导入、导出等。
* 基于接口的日志记录，使用`@LogIt`注解来标记需要记录的方法。
* 参数检测，使用`validation`进行参数校验，使用注解完成自动入参检测。
* 全局异常处理组件化，通过实现`ExceptionHolder`来完成特定异常的处理。
* 统一定时任务，由`SchedulingServer`接管，组件化实现方式，可以在配置文件中配置允许名单与屏蔽名单。
* 控制台指令（**默认关闭**），引入了`SpringShell`来允许在控制台输入自定义指令，执行自定义功能。
* 远程URL指令，通过`RemCommad`来编写自定义的远程指令。
* 通知发送模块，发送短信（由于平台太多，所以**未内置**）、邮件等的通知接口。

----

## 开发ING

* *MemCache的持久化实现*
* *脱敏注解的分组实现*

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

2. 实现`PermissionDetector`，完成角色判定。内置了一个实现类，逻辑是从登录用户的缓存中获取权限列表，然后判定。缓存中的权限列表是在登录时获取并加载的，具体细节在内置的`UserDetailsService`实现类中。开发者自己实现的时候，需要加上`@Component`注解将其注入Bean池。

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

项目中编写了一些测试接口与指令，方便测试与开发
项目中集成了springfox3与knife4j，可以通过 <http://127.0.0.1:8888/doc.html> 访问接口文档

----

> 注：因为我英文不好，所以这个Wiki是中文的  
> 注：我也不会写Wiki
