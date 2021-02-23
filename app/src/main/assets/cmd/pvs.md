pvs
===

输出物理卷信息报表

## 补充说明

**pvs命令** 用于输出格式化的物理卷信息报表。使用pvs命令仅能得到物理卷的概要信息，如果要得到更加详细的信息可以使用pvdisplay命令。

### 语法  

```
pvs(选项)(参数)
```

### 选项  

```
--noheadings：不输出标题头；
--nosuffix：不输出空间大小的单位。
```

### 参数  

物理卷：要显示报表的物理卷列表。

### 实例  

使用pvs命令显示系统中所有物理卷的信息报表。在命令行中输入下面的命令：

```
pvs                    #输出物理卷信息报表 
```

输出信息如下：

```
PV         VG     fmt  Attr PSize   PFree  
/dev/sdb1  vg1000 lvm2 --   100.00M 100.00M  
/dev/sdb2         lvm2 --   101.98M 101.98M
```


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->