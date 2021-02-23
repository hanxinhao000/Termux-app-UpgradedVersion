exportfs
===

管理NFS共享文件系统列表

## 补充说明

exportfs 命令用来管理当前NFS共享的文件系统列表。

参数：

```
-a 打开或取消所有目录共享。
-o options,...指定一列共享选项，与 exports(5) 中讲到的类似。
-i 忽略 /etc/exports 文件，从而只使用默认的和命令行指定的选项。
-r 重新共享所有目录。它使 /var/lib/nfs/xtab 和 /etc/exports 同步。 它将 /etc/exports 中已删除的条目从 /var/lib/nfs/xtab 中删除，将内核共享表中任何不再有效的条目移除。
-u 取消一个或多个目录的共享。
-f 在“新”模式下，刷新内核共享表之外的任何东西。 任何活动的客户程序将在它们的下次请求中得到 mountd添加的新的共享条目。
-v 输出详细信息。当共享或者取消共享时，显示在做什么。 显示当前共享列表的时候，同时显示共享的选项。
```


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->