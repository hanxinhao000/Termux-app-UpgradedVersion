vgscan
===

扫描并显示系统中的卷组

## 补充说明

**vgscan命令** 查找系统中存在的LVM卷组，并显示找到的卷组列表。vgscan命令仅显示找到的卷组的名称和LVM元数据类型，要得到卷组的详细信息需要使用vgdisplay命令。

### 语法  

```
vgscan(选项)
```

### 选项  

```
-d：调试模式；
--ignorerlockingfailure：忽略锁定失败的错误。
```

### 实例  

使用vgscan命令扫描系统中所有的卷组。在命令行中输入下面的命令：

```
[root@localhost ~]# vgscan     #扫描并显示LVM卷组列表
```

输出信息如下：

```
Found volume group "vg2000" using metadata type lvm2  
Found volume group "vg1000" using metadata type lvm2 
```

说明：本例中，vgscan指令找到了两个LVM2卷组"vg1000"和"vg2000"。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->