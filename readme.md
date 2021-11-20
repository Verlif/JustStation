## 使用说明
### 简介
* JustStation是一个基于Spring Boot的快速开发框架，目的是简化配置，统一接口。
* 项目中避免固定配置，也没有集成其他的工具类，只完成了基础框架的搭建。绝大部分模块以接口的方式展示，开发者只需要实现接口即可完成功能。 比如项目中只有邮件发送模块，如果开发者需要短信发送模块，只需要按照以下步骤，即可完成功能。
  1. 实现`NoticeHandler`，并标记为自动注入的Bean
  2. 在`NoticeTag`中添加新的短信型参数，例如`SMS`
  3. 使用`NoticeService.registerHandler(NoticeTag.SMS, this);`注册到`NoticeService`中
  4. 通过`NoticeService`的`sendNotice`方法，使用短信的枚举型来调用。

### 模块
* 标识化登录模块，支持单点登录、多设备登录、共享登录等登录模式
* 注解化权限配置，使用`@Perm`注解来标记接口权限
* 简单的文件管理，便捷自定义文件域
* 请求参数检测，通过注解的方式，在接口调用前主动检测参数完整性
* 简单的后台指令，允许在控制台输入自定义指令，执行自定义功能
* 通知发送模块，发送短信、邮件等的通知接口

### 举例
* 接口化配置的使用，只需要使用Spring的自动注入即可

  例如：因为项目中需要对权限做限定，所以内置了登录服务。开发者可以通过实现`BaseUserMapper`来完成登录验证
  ```
  @Component
  public class BaseUserCollectorImpl<T> implements BaseUserCollector<T> {
  
      @Autowired
      private UserMapper userMapper;
  
      @Override
      public BaseUser getUserByUsername(String username) {
          return userMapper.getUserByName(username);
      }
  }
  ```
* 注解化权限检测

  这里用的时Spring提供的切面方式进行的注入，需要配合`PermissionMapper`类获取权限
  ```
    @RestController
    @RequestMapping("/permission")
    @Api(tags = "权限测试")
    public class PermissionController {
    
        /**
         * 拥有角色[role]时可访问
         */
        @Operation(summary = "拥有角色 - role")
        @Perm(hasRole = "role")
        // 等效于 @PreAuthorize("@pd.hasRole('role')")
        @GetMapping("/hasRole")
        public BaseResult<Object> hasRole() {
            return new OkResult<>().msg("拥有角色");
        }
    
        /**
         * 拥有关键词[key]时可访问
         */
        @Operation(summary = "拥有关键词 - key")
        @Perm(hasKey = "key")
        // 等效于 @PreAuthorize("@pd.hasKey('key')")
        @GetMapping("/hasKey")
        public BaseResult<Object> hasKey() {
            return new OkResult<>().msg("拥有关键词");
        }
    
        /**
         * 未拥有角色[role]时可访问
         */
        @Operation(summary = "未拥有角色 - role")
        @Perm(noRole = "role")
        // 等效于 @PreAuthorize("@pd.noRole('role')")
        @GetMapping("/noRole")
        public BaseResult<Object> noRole() {
            return new OkResult<>().msg("未拥有角色");
        }
    
        /**
         * 未拥有关键词[key]时可访问
         */
        @Operation(summary = "未拥有关键词 - key")
        @Perm(noKey = "key")
        // 等效于 @PreAuthorize("@pd.noKey('key')")
        @GetMapping("/noKey")
        public BaseResult<Object> noKey() {
            return new OkResult<>().msg("未拥有关键词");
        }
    }

  ```
  
* 请求参数检测，便于自动化检测传入参数

  这里用的是自定义的`Check`注解
  ```
  @Operation(summary = "注册")
  @PostMapping("/register")
  @Check // 方法上的注解表示这个方法需要被拦截
  public BaseResult<?> register(
    // 参数上的注解表示这个参数需要被检测，被检测的参数需要实现Checkable接口（有默认实现）
    @RequestBody @Check User user
  ) {
    return userBiz.register(user);
  }
  ```