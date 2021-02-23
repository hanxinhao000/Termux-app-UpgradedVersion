mysqlshow
===

显示MySQL中数据库相关信息

## 补充说明

**mysqlshow命令** 用于显示mysql服务器中数据库、表和列表信息。

### 语法  

```
mysqlshow(选项)(参数)
```

### 选项  

```
-h：MySQL服务器的ip地址或主机名；
-u：连接MySQL服务器的用户名；
-p：连接MySQL服务器的密码；
--count：显示每个数据表中数据的行数；
-k：显示数据表的索引；
-t：显示数据表的类型；
-i：显示数据表的额外信息。
```

### 参数  

数据库信息：指定要显示的数据库信息，可以是一个数据库名，或者是数据库名和表名，或者是数据库名、表名和列名。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->