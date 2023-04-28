# framwWork
## 基础类
 ### 注解
* org.basis.framework.annotation
    * DistributeLock   分布式锁注解
    * IgnoreSecurity   忽略登录检查
    * Permissions  权限
 ### 异常类
* org.basis.framework.error
    * ExceptionControllerAdvice   异常控制器建议
    * DefinitionExceptionHandler  定义异常处理程序
    * DistributeLockException     分配锁异常
    * GlobalExceptionHandler      全局异常处理程序
    * UnauthorizedException       未经授权的异常
    * ValidationException         验证异常
    * IgnoreException             忽略异常
 ### 日志监听 
* org.basis.framework.log
    * LoggerDisruptorQueue        日志处理队列
    * ProcessLogAppender          控制台日志处理
  ### 识别
* org.basis.framework.Identify
    * IdentifyPDFProperties       pdf识别 规则配置
    * IdentifyRegexConfig         pdf识别 规则
    * PdfBoxKeyWordPosition       Pdf 获取关键字位置
    * PdfIdentifyConfigUtil       加载pdf识别 规则配置
    * PdfIdentifyUtil             pdf识别 工具类
    * RegexConfig
## 测试工具 test
* BaseTest mvc测试基类
* BaseServiceTest service测试基类 
## 工具类
* DateUtil                      工具类
* EntityUtil                    实体生成器
* ExcelUtils                    excel工具类 (生成，解析)
* GitUtils                      版本管理工具类
* FileUtils                     文件工具类  
* JSONHelper                    json工具类
* JsonReturnApi                 带泛型的api返回结果
* JwtUtil                       Jwt工具类
* PageUtils                     分页工具类
* Query                         Query分页
* BaseConverterUtils            基本类型的转换工具类
* RegexpUtil                    正则工具类
* AmountConversionUtils         金额元分之间转换工具类
* BaiduClientUtil               百度 api (经纬度，正反查询，以及批量)
* CharUtils                     字符工具类 （大小写转换）
* DistanceCalculationUtils      距离计算
* EnumUtils                     枚举工具类
* IdCardValidatorUtils          身份证验证器工具类
* MoneyUtil                     金额校验工具类
* OSSClientUtil                 OSS工具类 
* PinyinUtils                   汉字转拼音工具
* QrCodeUtil                    二维码工具类
* ReflectionInvokeUtils         反射调用工具类
* SnowFlakeUtils                Twitter的分布式自增ID雪花算法snowflake
* TokenGenerator                生成token  
* BarCodeUtil                   条形码生成器
* BigDecimalUtil                BigDecimal类型操作类
* MD5Util                       MD5签名处理工具类
* AsymmetricEncryptUtil         非对称加密
* SM2EncryptUtil                SM2加密
* SymmetryEncryptUtil           对称加密

### 枚举
* FilenameExtensionEnum          文件扩展名枚举

### 打包命令 ./gradlew clean classes -x test
### 强制更新最新依赖，清除构建并构建
### ./gradlew clean build --refresh-dependencies