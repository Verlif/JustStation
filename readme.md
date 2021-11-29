# JustStation

基于SpringBoot的开源框架，只包括JavaWeb框架，不包括前端页面与拓展业务实现

[Github主页](https://github.com/Verlif/JustStation) | [Github WIKI](https://github.com/Verlif/JustStation/wiki) | [Gitee主页](https://gitee.com/Verlif/JustStation) | [Gitee WIKI](https://gitee.com/Verlif/JustStation/wikis/Home)

----

## 说明

1. 这是一个基础框架，所以肯定会有无数个没有考虑到的地方，后面也会不定期更新。
2. 一些功能的实现或灵感来源于GitHub、CSDN、简书、若依、mayfly等，所有其他作者的开源代码在协议允许的情况下，都会根据此项目需要有所更改。

----

## 特点

* 非侵入式开发，项目提供了注解与接口进行功能拓展，大部分场景不需要修改源码。
  实现功能就实现对应接口，并用注解注入即可。
* 数据库不相关，框架逻辑与数据库结构没有关系，需要登录与权限的功能只需要实现接口逻辑即可。
* 遵循COC原则（应该吧），所有接口都有内置实现类，不需要特殊处理的话，可以不去重写。

## 功能

* 标识化登录模块，支持单点登录、多设备登录、共享登录等登录模式。
  * 功能由`LoginService`和`CacheHandler`支持，用的就是缓存来实现的。
  * 具体要哪种登录逻辑可以通过实现`LoginHanlder`来完成。
* 注解化权限配置，使用`@Perm`注解来标记接口权限。
  * 基于Spring的AOP方式开发。
  * 写在方法上的注解，支持role和key值判定。
  * 更多判定的话，改`Perm`注解和`PermissionHandler`的方法。
* 简单的文件管理，支持自定义文件域。
  * 就包括了文件的上传、下载和生产预览地址（预览是浏览器支持的）
  * 文件域由枚举类`FileCart`支持
* 参数检测，使用`validation`进行参数校验
* 控制台指令，引入了`SpringShell`来允许在控制台输入自定义指令，执行自定义功能
* 远程URL指令，通过`RemCommad`来编写自定义的远程指令
* 通知发送模块，发送短信、邮件等的通知接口
* 基于方法的日志记录，使用`@LogIt`注解来标记需要记录的方法
  * 内置的逻辑我随便写的，要改的话实现`LogHandler`就行了

----

## 测试

项目中编写了一些测试接口与指令，方便测试与开发
项目中集成了springfox3与knife4j，可以通过 <http://127.0.0.1:8888/doc.html> 访问接口文档

----

> 注：因为我英文不好，所以这个Wiki是中文的  
> 注：我也不会写Wiki
