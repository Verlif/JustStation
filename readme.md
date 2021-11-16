## 使用说明
### 简介
JustStation是一个基于Spring Boot的快速开发框架，目的是简化配置，专注逻辑

### 功能
* 接口化配置，大多数配置以接口的方式提供，降低耦合
* 注解化权限配置，支持角色与关键词检测
* 标识化登录，支持单点登录、多设备登录、共享登录等登录模式
* 简单的文件系统，支持自定义文件域
* 请求参数检测，通过注解的方式标注请求参数即可检测参数完整性

### 举例
* 接口化配置的使用

  因为项目中需要对权限做限定，所以内置了登录服务。开发者可以通过实现`BaseUserMapper`来完成登录验证
  ```
    @Component
    public class BaseUserMapperImpl<T> implements BaseUserMapper<T> {

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
  
* 请求参数检测

  这里用的是自定义的Check注解
  ```
  @Operation(summary = "注册")
  @PostMapping("/register")
  @Check // 方法上的注解表示这个方法需要被拦截
  public BaseResult<?> register(
    // 参数上的注解表示这个参数需要被检测，被检测的参数需要实现Checkable接口
    @RequestBody @Check User user
  ) {
    return userBiz.register(user);
  }
  ```
  
### 本项目提供的自定义组件列表
* `PrintInfoHandler` 信息打印接口，便于统一信息打印格式与信息打印控制
* `FileHandler` 文件管理接口，实现独立的文件管理
* `LoginHanlder` 登录接口，可以按照业务需要自行实现
* `CacheHandler` 缓存组件接口，有更好的缓存解决方案的话可以实现此接口来对缓存进行管理
* `PermissionDetector` 权限验证接口，实现一些特殊的验证方式。比如不想用权限就直接全部返回`true`就OK
* `PermissionMapper` 权限获取接口，实现不同源的权限获取方式
* `BaseUserCollector` 用户信息获取接口，实现不同源的登录用户信息获取方式
