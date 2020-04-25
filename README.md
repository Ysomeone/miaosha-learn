# 关于swagger 在springboot 的使用

Swagger 是一款RESTFUL接口的、基于YAML、JSON语言的文档在线自动生成、代码自动生成的工具

## swagger官方地址

 https://swagger.io/

## gitHub地址：

https://github.com/swagger-api

## swagger的集成 

在pom.xml 引入仓库

```xml
      <dependency>
            <groupId>io.springfox</groupId>
             <artifactId>springfoxswagger2</artifactId>
            <version>2.6.1</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.6.1</version>
        </dependency>
```

swagger 能将扫码注解的得到一个json文档，并通过swagger-ui展示在页面上，上面引入的springfox-swagger-ui会加载页面，通过springboot启动的访问链接为：http://localhost:8080/swagger-ui.html



静态资源也可以从https://github.com/swagger-api/swagger-ui 获取，其所有的 dist 目录下东西放到需要集成的项目里,但需要更改dist目录下index.html页面的访问url，比如修改  url: "http://localhost:8080/v2/api-docs"

```
<script>
    window.onload = function () {
        // Begin Swagger UI call region
        const ui = SwaggerUIBundle({
            url: "http://localhost:8080/v2/api-docs",
            // apisSorter: "alpha", // can also be a function ,设置排序
            // operationsSorter: function (a, b) {
            //     //自定义排序规格，按方法说明来进行排序，将内容转换成数字，然后进行比较
            //     return parseFloat(a.summary) - (parseFloat(b.summary));
            // } ,
            dom_id: '#swagger-ui',
            deepLinking: true,
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIStandalonePreset
            ],
            plugins: [
                SwaggerUIBundle.plugins.DownloadUrl
            ],
            layout: "StandaloneLayout"
        })
        // End Swagger UI call region

        window.ui = ui
    }
</script>
```



## springboot中 的配置文件

```java
/**
 * @author yuan
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        /**
         * .host("域名") 如果通过域名访问文档，需要加上这个，以免在文档请求因为跨域问题而请求不了
         */
        return new Docket(DocumentationType.SWAGGER_2)//.host("域名")
                .apiInfo(apiInfo()).select()
                //扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage("com.yuan.redis.controller"))
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build().securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("基础平台  API文档")
                .version("1.0.0")
                .build();
    }

    /**
     * 权限控制，在请求参数加上sessionId,
     *
     * @return
     */
    private List<ApiKey> security() {
        List<ApiKey> apiKeyList = new ArrayList();
        ApiKey apiKey = new ApiKey("sessionId", "sessionId", "query");
        apiKeyList.add(apiKey);
        return apiKeyList;
    }

}
```

其中的@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")表示是否开启文档，如果上线了为了安全想把访问链接给禁用，可以在application.properties加上

```
# swagger是否开启
swagger.enable=false 
```

## swagger 注解介绍

1、**@Api()**：用在请求的类上，表示对类的说明，也代表了这个类是swagger2的资源

参数：

```text
tags：说明该类的作用，参数是个数组，可以填多个。
value="该参数没什么意义，在UI界面上不显示，所以不用配置"
description = "用户基本信息操作"
```

2、**@ApiOperation()**：用于方法，表示一个http请求访问该方法的操作

参数：

```text
value="方法的用途和作用"    
notes="方法的注意事项和备注"    
tags：说明该方法的作用，参数是个数组，可以填多个。
格式：tags={"作用1","作用2"} 
（在这里建议不使用这个参数，会使界面看上去有点乱，前两个常用）
```

3、**@ApiModel()**：用于响应实体类上，用于说明实体作用

参数：

```text
description="描述实体的作用"  
```

4、**@ApiModelProperty**：用在属性上，描述实体类的属性

参数：

```text
value="用户名"  描述参数的意义
name="name"    参数的变量名
required=true     参数是否必选
```

5、**@ApiImplicitParams**：用在请求的方法上，包含多@ApiImplicitParam

6、**@ApiImplicitParam**：用于方法，表示单独的请求参数

参数：

```text
name="参数ming" 
value="参数说明" 
dataType="数据类型" 
paramType="query" 表示参数放在哪里
    · header 请求参数的获取：@RequestHeader
    · query   请求参数的获取：@RequestParam
    · path（用于restful接口） 请求参数的获取：@PathVariable
    · body（不常用）
    · form（不常用） 
defaultValue="参数的默认值"
required="true" 表示参数是否必须传
```

7、**@ApiParam()**：用于方法，参数，字段说明 表示对参数的要求和说明

参数：

```text
name="参数名称"
value="参数的简要说明"
defaultValue="参数默认值"
required="true" 表示属性是否必填，默认为false
```

8、**@ApiResponses**：用于请求的方法上，根据响应码表示不同响应

一个@ApiResponses包含多个@ApiResponse

9、**@ApiResponse**：用在请求的方法上，表示不同的响应

**参数**：

```text
code="404"    表示响应码(int型)，可自定义
message="状态码对应的响应信息"   
```

10、**@ApiIgnore()**：用于类或者方法上，不被显示在页面上

11、**@Profile({"dev", "test"})**：用于配置类上，表示只对开发和测试环境有用

## 代码注解示例

```java
/**
 * swagger 文档演示
 */
@RestController
@RequestMapping("/api/common")
@Api(value = "swagger演示", tags = "用来演示Swagger的一些注解")
public class TestController {


    @ApiOperation(value = "修改用户密码", notes = "根据用户id修改密码", authorizations = {@Authorization("sessionId")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "password", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updatePassword.json", method = RequestMethod.POST)
    public String updatePassword(HttpServletRequest request, String password,
                                 String newPassword) {

        if (password.equals(newPassword)) {
            return "新旧密码不能相同";
        }
        return "密码修改成功!";
    }

    @ApiOperation(value = "保存用户信息", notes = "保存用户信息")
    @RequestMapping(value = "/test.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    public Result<String> saveUserInfo(Test test) {
        Test t = new Test();
        Paramap t1 = Paramap.create().put("t", t);
        return Result.jsonStringOk(t);
    }
}
```

```java
@ApiModel(description = "测试数据")
public class Test {
    /** 姓名 */
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /** 联系手机 */
    @ApiModelProperty(value = "联系手机", required = true)
    private String telephone;

    /** 头像 */
    @ApiModelProperty(value = "头像", required = true)
    private String avatar;

    /** 性别 1、男 2、女 */
    @ApiModelProperty(value = "性别 1、男 2、女", required = true)
    private Integer sex;
}
```

## 根据json数据生成pdf文档或者html文档

步骤（方法一）

1、修改pom.xml下的swaggerInput下的swaggerjson数据访问路径

![Image text](https://github.com/Ysomeone/swaggerDocument/raw/master/src/main/webapp/resources/images/1572416310(1).png)

2、运行插件swagger2markup生成asciidoc文档和运行插件asciidoctor生成pdf文档

![Image text](https://github.com/Ysomeone/swaggerDocument/raw/master/src/main/webapp/resources/images/1572416154(1).png)



## 文档预览

![Image text](https://github.com/Ysomeone/swaggerDocument/raw/master/src/main/webapp/resources/images/1572407065(1).png)

[生成文档github链接](https://github.com/Ysomeone/swaggerDocument.git）

