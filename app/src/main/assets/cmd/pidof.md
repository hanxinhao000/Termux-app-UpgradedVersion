pidof
===

查找指定名称的进程的进程号ID号

## 补充说明

**pidof命令** 用于查找指定名称的进程的进程号id号。

### 语法  

```
pidof(选项)(参数)
```

### 选项  

```
-s：仅返回一个进程号；
-c：仅显示具有相同“root”目录的进程；
-x：显示由脚本开启的进程；
-o：指定不显示的进程ID。
```

### 参数  

进程名称：指定要查找的进程名称。

### 实例  

```
pidof nginx
13312 5371

pidof crond
1509

pidof init
1
```


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->