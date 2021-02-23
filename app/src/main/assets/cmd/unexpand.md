unexpand
===

将文件的空白字符转换为制表符

## 补充说明

**unexpand命令** 用于将给定文件中的空白字符（space）转换为制表符（TAB），并把转换结果显示在标准输出设备（显示终端）。

### 语法  

```
unexpand(选项)(参数)
```

### 选项  

```
-a或--all：转换文件中所有的空白字符；
--first-only：仅转换开头的空白字符；
-t<N>：指定TAB所代表的N个（N为整数）字符数，默认N值是8。
```

### 参数  

文件：指定要转换空白为TAB的文件列表。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->