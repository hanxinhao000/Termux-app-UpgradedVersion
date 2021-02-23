mktemp
===

创建临时文件供shell脚本使用

## 补充说明

**mktemp命令** 被用来创建临时文件供shell脚本使用。

### 语法  

```
mktemp(选项)(参数)
```

### 选项  

```
-q：执行时若发生错误，不会显示任何信息；
-u：暂存文件会在mktemp结束前先行删除；
-d：创建一个目录而非文件。
```

### 参数  

文件：指定创建的临时文件。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->