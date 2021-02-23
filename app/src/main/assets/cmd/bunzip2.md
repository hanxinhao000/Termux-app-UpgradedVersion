bunzip2
===

创一个bz2文件压缩包

## 补充说明

**bunzip2命令** 解压缩由bzip2指令创建的”.bz2"压缩包。对文件进行压缩与解压缩。此命令类似于“gzip/gunzip”命令，只能对文件进行压缩。对于目录只能压缩目录下的所有文件，压缩完成后，在目录下生成以“.bz2”为后缀的压缩包。bunzip2其实是bzip2的符号链接，即软链接，因此压缩解压都可以通过bzip2实现。

### 语法  

```
bunzip2(选项)(参数)
```

### 选项  

```
-f或--force：解压缩时，若输出的文件与现有文件同名时，预设不会覆盖现有的文件；
-k或——keep：在解压缩后，预设会删除原来的压缩文件。若要保留压缩文件，请使用此参数；
-s或——small：降低程序执行时，内存的使用量；
-v或——verbose：解压缩文件时，显示详细的信息；
-l，--license，-V或——version：显示版本信息。
```

### 参数  

.bz2压缩包：指定需要解压缩的.bz2压缩包。

### 实例  

将`/opt`目录下的etc.zip、var.zip和backup.zip进行压缩，设置压缩率为最高，同时在压缩完毕后不删除原始文件，显示压缩过程的详细信息。

```
bzip2 -9vk /opt/etc.zip /opt/var.zip /opt/backup.zip
```

压缩完毕后，在`/opt`下就会生成相应的etc.zip.bz2、var.zip.bz2和backup.zip.bz2文件。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->