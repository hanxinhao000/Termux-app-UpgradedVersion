pathchk
===

检查文件中不可移植的部分

## 补充说明

**pathchk命令** 用来检查文件中不可移植的部分。

### 语法  

```
pathchk(选项)(参数)
```

### 选项  

```
-p：检查大多数的POSIX系统；
-P：检查空名字和“-”开头的文件；
--portability：检查所有的POSIX系统，等同于“-P-p”选项；
--help：显示帮助；
--wersion：显示版本号。
```

### 参数  

*   文件：带路径信息的文件；
*   后缀：可选参数，指定要去除的文件后缀字符串。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->