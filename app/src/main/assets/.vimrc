set nocompatible"关闭兼容模式，最最基础的设置，设置后才能使用vim许多特有的 特性
set number "设置显示行号
set cindent"设置自动缩进，根据的是c
set tabstop=4"设置tab宽度是4
set history=1000
set shiftwidth=4"用于程序中自动缩进所使用的空白长度指示的
set hlsearch"高亮搜索的关键字
set incsearch"以同时高亮所有匹配的字符
set ignorecase"搜索忽略大小写
set showmatch"括号匹配
set cursorline"高亮光标所在的行
set encoding=utf-8
behave mswin


"PATHOGEN插件管理工具的配置（全都是官方要求）
execute pathogen#infect()
syntax on
filetype plugin on
filetype indent on"自动根据文件类型来加载对应的缩进插件、
"另一款插件管理工具（官方标准配置）
"vundle
set nocompatible              " be iMproved, required
"filetype off                  " required

" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
Plugin 'Valloric/YouCompleteMe'

Plugin 'fatih/vim-go'
"所有的插件都要在这上面添加
" All of your Plugins must be added before the following line
call vundle#end()            " required
filetype plugin indent on    " required

"关于ycm的配置
" Required for YouCompleteMe
set encoding=utf-8
let g:ycm_global_ycm_extra_conf = '~/.vim/bundle/YouCompleteMe/third_party/ycmd/examples/.ycm_extra_conf.py'
"关键字补全
let g:ycm_seed_identifiers_with_syntax = 1
" 在接受补全后不分裂出一个窗口显示接受的项
set completeopt-=preview
" 让补全行为与一般的IDE一致
set completeopt=longest,menu
" 输入第一个字符就开始补全
let g:ycm_min_num_of_chars_for_completion=1
" 每次重新生成匹配项，禁止缓存匹配项
let g:ycm_cache_omnifunc=0
