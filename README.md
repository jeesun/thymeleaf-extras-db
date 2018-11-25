# thymeleaf-extras-db
## 简介
thymeleaf-extras-db是针对thymeleaf的扩展，主要是简化前端select标签数据的获取。

## 使用教程
thymeleaf-extras-db目前支持两种自定义标签，支持普通select标签，也支持select2和easyui-combobox。

### t:dict
属性 | 是否必填 | 可选值 | 默认值
---|---|---|---
class | 否 | |
id | 否 | | 
name | 否 | |
dict_name | 是 | | 
allow-empty | 否 | true,false | true