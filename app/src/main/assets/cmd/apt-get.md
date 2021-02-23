apt-get
===

Debian Linux发行版中的APT软件包管理工具

## 补充说明

**apt-get命令** 是Debian Linux发行版中的APT软件包管理工具。所有基于Debian的发行都使用这个包管理系统。deb包可以把一个应用的文件包在一起，大体就如同Windows上的安装文件。

### 语法  

```
apt-get(选项)(参数)
```

### 选项  

```
-c：指定配置文件。
```

### 参数  

*   管理指令：对APT软件包的管理操作；
*   软件包：指定要操纵的软件包。

### 实例  

使用apt-get命令的第一步就是引入必需的软件库，Debian的软件库也就是所有Debian软件包的集合，它们存在互联网上的一些公共站点上。把它们的地址加入，apt-get就能搜索到我们想要的软件。/etc/apt/sources.list是存放这些地址列表的配置文件，其格式如下：

```
deb web或[ftp地址] [发行版名字] main/contrib/non-[free]
```

我们常用的Ubuntu就是一个基于Debian的发行，我们使用apt-get命令获取这个列表，以下是我整理的常用命令：

在修改`/etc/apt/sources.list`或者`/etc/apt/preferences`之后运行该命令。此外您需要定期运行这一命令以确保您的软件包列表是最新的：

```
apt-get update
```

安装一个新软件包：

```
apt-get install packagename
```

卸载一个已安装的软件包（保留配置文件）：

```
apt-get remove packagename
```

卸载一个已安装的软件包（删除配置文件）：

```
apt-get –purge remove packagename
```

会把已装或已卸的软件都备份在硬盘上，所以如果需要空间的话，可以让这个命令来删除你已经删掉的软件：

```
apt-get autoclean apt
```

这个命令会把安装的软件的备份也删除，不过这样不会影响软件的使用的：

```
apt-get clean
```

更新所有已安装的软件包：

```
apt-get upgrade
```

将系统升级到新版本：

```
apt-get dist-upgrade
```

定期运行这个命令来清除那些已经卸载的软件包的.deb文件。通过这种方式，您可以释放大量的磁盘空间。如果您的需求十分迫切，可以使用`apt-get clean`以释放更多空间。这个命令会将已安装软件包裹的.deb文件一并删除。大多数情况下您不会再用到这些.debs文件，因此如果您为磁盘空间不足 而感到焦头烂额，这个办法也许值得一试：

```
apt-get autoclean
```


<!-- Linux命令行搜索引擎：https://jaywcjlove.github.io/linux-command/ -->