dd
===

复制文件并对原文件的内容进行转换和格式化处理

## 补充说明

**dd命令** 用于复制文件并对原文件的内容进行转换和格式化处理。dd命令功能很强大的，对于一些比较底层的问题，使用dd命令往往可以得到出人意料的效果。用的比较多的还是用dd来备份裸设备。但是不推荐，如果需要备份oracle裸设备，可以使用rman备份，或使用第三方软件备份，使用dd的话，管理起来不太方便。

建议在有需要的时候使用dd 对物理磁盘操作，如果是文件系统的话还是使用tar backup cpio等其他命令更加方便。另外，使用dd对磁盘操作时，最好使用块设备文件。

### 语法  

```
dd(选项)
```

### 选项  

```
bs=<字节数>：将ibs（输入）与欧巴桑（输出）设成指定的字节数；
cbs=<字节数>：转换时，每次只转换指定的字节数；
conv=<关键字>：指定文件转换的方式；
count=<区块数>：仅读取指定的区块数；
ibs=<字节数>：每次读取的字节数；
obs=<字节数>：每次输出的字节数；
of=<文件>：输出到文件；
seek=<区块数>：一开始输出时，跳过指定的区块数；
skip=<区块数>：一开始读取时，跳过指定的区块数；
--help：帮助；
--version：显示版本信息。
```

### 实例  

```
[root@localhost text]# dd if=/dev/zero of=sun.txt bs=1M count=1
1+0 records in
1+0 records out
1048576 bytes (1.0 MB) copied, 0.006107 seconds, 172 MB/s

[root@localhost text]# du -sh sun.txt 
1.1M    sun.txt
```

该命令创建了一个1M大小的文件sun.txt，其中参数解释：

*    **if**  代表输入文件。如果不指定if，默认就会从stdin中读取输入。
*    **of**  代表输出文件。如果不指定of，默认就会将stdout作为默认输出。
*    **bs**  代表字节为单位的块大小。
*    **count**  代表被复制的块数。
*    **/dev/zero**  是一个字符设备，会不断返回0值字节（\0）。

块大小可以使用的计量单位表

<table>

<tbody>

<tr>

<td>单元大小</td>

<td>代码</td>

</tr>

<tr>

<td>字节（1B）</td>

<td>c</td>

</tr>

<tr>

<td>字节（2B）</td>

<td>w</td>

</tr>

<tr>

<td>块（512B）</td>

<td>b</td>

</tr>

<tr>

<td>千字节（1024B）</td>

<td>k</td>

</tr>

<tr>

<td>兆字节（1024KB）</td>

<td>M</td>

</tr>

<tr>

<td>吉字节（1024MB）</td>

<td>G</td>

</tr>

</tbody>

</table>

以上命令可以看出dd命令来测试内存操作速度：

```
1048576 bytes (1.0 MB) copied, 0.006107 seconds, 172 MB/s
```


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->