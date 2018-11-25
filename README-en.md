[English](README-en.md) | [中文](README.md)

# thymeleaf-extras-db
thymeleaf-extras-db is a thymeleaf extra plugin for select tag.It aims to load and cache data for select tag from database directly, instead of web api.
## Use tutorial
thymeleaf-extras-db currently supports two custom tag `t:dict` and `t:select`, they have most of the same attributes, only one difference. `t:dict` and `t:select` both support normal select tag attributes, also support `select2` and `easyui-combobox` attributes.But what needs attention is `t:dict` tag's data is from table `t_dict_type` and `t_dict_type_group`, that means you need create table [mysql.sql](mysql.sql)。
```
In html page, you need to add attributes xmlns:t="http://www.w3.org/1999/xhtml" for html tag。
Use example: 
<t:dict class="form-control select2" id="add_menu_type" name="menuType" dict-name="menu_type" style="width:100%"></t:dict>
<t:select id="add_menu_group_id" name="pid" order="desc" query="t_side_menu,name,id,pid is null" class="form-control select2" data-live-search="true" style="width:100%"></t:select>

easyui use example: 
<t:dict class="easyui-combobox" id="search_authority" name="authority" dict-name="role_type"  style="width:160px" allow-empty="true"></t:dict>
```

### 1. New Configuration Class

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
### 2. Config Cache
In application.yml, add configuration as below：
```
spring:
  cache:
    cache-names: listOptionCache
```
If you are use ehcache，you need add config like below in ehcache.xml:
```
<cache name="listOptionCache"
    maxElementsInMemory="0"
    eternal="true"
    overflowToDisk="true"
    diskPersistent="true"
    memoryStoreEvictionPolicy="LRU">
</cache>
```

### 3. tag attribute and description
attribute | description | required | Optional value | default value
---|---|---|---|---
id | id | no | | 
class | class | no | |
name | name | no | |
style | style | no | | 
order | order type | no | | 
allow-empty | allow empty | no | true,false | true
empty-message | empty message | no | | \&nbsp;
cacheable | cacheable or not | no | true,false | true
data-live-search | select2 proprietary attribute | no | true,false |
multiple | select2 proprietary attribute | no | multiple | 
data-options | easyui-combobox proprietary attribute | no | | 
dict_name | (t:dict only) dictionary name, only table `t_dict_type_group`'s column `type_group_code`'s value is effective | Yes | | 
query | (t:select only) attribute rule:table name,displayed column[,value column][,query conditions ] | Yes |  | 
