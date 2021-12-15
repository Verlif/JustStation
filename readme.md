# JustStation

基于SpringBoot的开源框架，只包括JavaWeb框架，不包括前端页面与拓展业务实现

[Github主页](https://github.com/Verlif/JustStation) | [Github WIKI](https://github.com/Verlif/JustStation/wiki) | [Gitee主页](https://gitee.com/Verlif/JustStation) | [Gitee WIKI](https://gitee.com/Verlif/JustStation/wikis/Home)

WIKI上有比较详细的功能实现说明。

----

## 说明

1. 这是一个基础框架，所以肯定会有无数个没有考虑到的地方，后面也会不定期更新。
2. 一些功能的实现或灵感来源于GitHub、CSDN、简书、若依、mayfly、oxygen等，所有其他作者的开源代码在协议允许的情况下，都会根据此项目需要有所更改。
3. 注意：当前默认的缓存机制是使用的MemCache，没有持续化处理。内置了一个Redis实现的CacheHandler，需要的话
，请在RedisCache上添加`@Component`注解以更改。或是使用自己的缓存机制，实现`CacheHandler`接口并标记为Bean。
4. 我个人是比较喜欢用空间来换时间，所以框架内的大部分实现都是在尽量不生成新对象的前提下，取变量来换取运算时间。
5. 大部分的功能都可以在`idea.verlif.juststation.core.test`包下找到

----

## 特点

* 简化，将常用功能通过注解的方式实现，降低了代码量与维护难度。
* 拓展性，为常用功能，例如日志、限流等提供了处理器选项，可以针对不同接口选择不同的处理类。
* 非侵入式开发，项目提供了注解与接口进行功能拓展，大部分场景不需要修改源码。
  实现功能就实现对应接口，并用注解注入即可。
* 数据库不相关，框架逻辑与数据库结构没有关系，需要登录与权限的功能只需要实现接口逻辑即可。
* 遵循COC原则（应该吧），所有接口也都有内置实现类，不需要特殊处理的话，可以直接使用。

----

## 功能点

* 标识化登录模块，支持单点登录、多设备登录、共享登录等登录模式。
  * 功能由`LoginService`和`CacheHandler`支持，用的就是缓存来实现的。
  * 具体要哪种登录逻辑可以通过实现`LoginHanlder`来完成。
* 注解化权限配置，使用`@Perm`注解来标记接口权限。
  * 基于Spring的AOP方式开发。
  * 写在方法上的注解，支持role和key值判定。
  * 更多判定的话，改`Perm`注解和`PermissionHandler`的方法。
* 接口访问限制，允许不同接口配置不同的限制方案，内置了一个固定窗口限制器。
  * 基于Spring的AOP方式开发。
  * 可以作为限流器或是特殊入口。
  * 使用`@Limit`注解即可，限流参数只有Key和限制器，与接口无关。
  * `@Limit`需要选择限制，这里没有做默认处理。
* 简单的数据脱敏，内置了一个脱敏器。
  * 基于Jackson的序列化操作。
  * 内置的只做了置空处理，要改的话，修改`SensitiveSerialize`就行了。
  * 使用方法就是在属性上加上`@Sensitive`注解，然后选择一个脱敏策略就行了。
* 简单的文件管理，支持自定义文件域。
  * 就包括了文件的上传、下载和生产预览地址（预览是浏览器支持的）。
  * 添加了文件解析处理器，通过文件解析接口实现文件导入与导出。
  * 文件域由枚举类`FileCart`支持。
  * 文件类型由枚举类`FileType`支持。
* 参数检测，使用`validation`进行参数校验，使用注解完成自动入参检测。
  * 使用的话就是在数据属性上添加需要的校验注解，例如`@NotNull`、`@Email`，然后在需要校验的地方给参数添加`@Validated`注解。
  * 更多的使用说明可以参考Aldeo的简书 - [优雅的校验参数-javax.validation](https://www.jianshu.com/p/67d3637493c7)
* 全局异常处理组件化，通过实现`ExceptionHolder`并利用注解自动注入即可完成特定的异常处理。
  * 目前的策略是由当前异常类来获取处理类，未定义时查询其父类，直到找到相应处理类或是到达顶级父类。
  * 目前来说`ExceptionHolder`需要实现两个方法，一个是注册的异常类，一个是处理逻辑
* 控制台指令，引入了`SpringShell`来允许在控制台输入自定义指令，执行自定义功能。
  * 此功能默认关闭
* 远程URL指令，通过`RemCommad`来编写自定义的远程指令。
  * 这个指令只是一个小模块，用来做测试或是其他的一些功能还是挺方便的。
* 通知发送模块，发送短信、邮件等的通知接口。
* 基于接口的日志记录，使用`@LogIt`注解来标记需要记录的方法。
  * 内置的逻辑我随便写的，要改的话实现`LogHandler`就行了。
  * `@LogIt`中可以选择想要的`LogHandler`，便于针对不同接口采取不同的记录策略

----

## 开发ING

* *MemCache的持久化实现*
* *脱敏注解的分组实现*

----

## 举例

以实现权限接口为例。

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
