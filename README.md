[English](README-en.md) | [中文](README.md)

# thymeleaf-extras-db
## 简介
thymeleaf-extras-db是针对thymeleaf的扩展，主要是简化前端select标签数据的获取，让select标签直接从数据库加载数据，而不需要单独写接口，支持缓存。

## 导入
```
<dependency>
    <groupId>com.github.jeesun.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-db</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 使用教程
thymeleaf-extras-db目前支持两种自定义标签t:dict和t:select，两个标签仅一个属性不同，其他属性两者都支持。t:dict和t:select都支持普通select标签属性，也支持select2和easyui-combobox属性。需要注意的是，t:dict标签的数据，是从表t_dict_type和t_dict_type_group查询的，需要建表[mysql.sql](mysql.sql)。
```
在html页面上，需要给html标签添加属性xmlns:t="http://www.w3.org/1999/xhtml"。
使用示例：
<t:dict class="form-control select2" id="add_menu_type" name="menuType" dict-name="menu_type" style="width:100%"></t:dict>
<t:select id="add_menu_group_id" name="pid" order="desc" query="t_side_menu,name,id,pid is null" class="form-control select2" data-live-search="true" style="width:100%"></t:select>

easyui中使用方式：
<t:dict class="easyui-combobox" id="search_authority" name="authority" dict-name="role_type"  style="width:160px" allow-empty="true"></t:dict>
```

### 1. 新建配置类
在Spring Boot中，使用thymeleaf-extras-db很简单，先新建一个配置类：
```
@Configuration
public class CustomDialectConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public DbDialect dbDialect(){
        //return new DbDialect(jdbcTemplate);
        return new DbDialect(jdbcTemplate, cacheManager);
    }
}
```
### 2. 配置缓存
请在application.yml中添加如下配置：
```
spring:
  cache:
    cache-names: listOptionCache
```
如果你使用的是ehcache，那么还需要在ehcache.xml中新增如下类似配置：
```
<cache name="listOptionCache"
    maxElementsInMemory="0"
    eternal="true"
    overflowToDisk="true"
    diskPersistent="true"
    memoryStoreEvictionPolicy="LRU">
</cache>
```

### 3. 标签属性及含义
属性 | 含义 | 是否必填 | 可选值 | 默认值
---|---|---|---|---
id | id | 否 | | 
class | class | 否 | |
name | name | 否 | |
style | style | 否 | | 
order | 排序方式 | 否 | | 
allow-empty | 允许空值 | 否 | true,false | true
empty-message | 空值显示内容 | 否 | | \&nbsp;
cacheable | 是否允许缓存 | 否 | true,false | true
data-live-search | select2专有属性 | 否 | true,false |
multiple | select2专有属性 | 否 | multiple | 
data-options | easyui-combobox专有属性 | 否 | | 
dict_name | （t:dict独有）字典名称，只能填t_dict_type_group的type_group_code字段的值 | 是 | | 
query | （t:select独有）属性规则：表名,显示的字段名[,作为option的value的字段名][,查询条件] | 是 |  | 
