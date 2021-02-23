pkill
===

可以按照进程名杀死进程

## 补充说明

**pkill命令** 可以按照进程名杀死进程。pkill和killall应用方法差不多，也是直接杀死运行中的程序；如果您想杀掉单个进程，请用kill来杀掉。

### 语法  

```
pkill(选项)(参数)
```

### 选项  

```
-o：仅向找到的最小（起始）进程号发送信号；
-n：仅向找到的最大（结束）进程号发送信号；
-P：指定父进程号发送信号；
-g：指定进程组；
-t：指定开启进程的终端。
```

### 参数  

进程名称：指定要查找的进程名称，同时也支持类似grep指令中的匹配模式。

### 实例  

```
pgrep -l gaim
2979 gaim

pkill gaim
```

也就是说：kill对应的是PID，pkill对应的是command。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->