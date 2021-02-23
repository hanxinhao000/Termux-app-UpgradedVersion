bzip2recover
===

恢复被破坏的.bz2压缩包中的文件

## 补充说明

**bzip2recover命令** 可用于恢复被破坏的“.bz2”压缩包中的文件。

bzip2是以区块的方式来压缩文件，每个区块视为独立的单位。因此，当某一区块损坏时，便可利用bzip2recover，试着将文件中的区块隔开来，以便解压缩正常的区块。通常只适用在压缩文件很大的情况。

### 语法  

```
bzip2recover(参数)
```

### 参数  

文件：指定要恢复数据的.bz2压缩包。


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->