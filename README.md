# 一、说明
未来道具11号-KK Maid是一款适配ASMR ONE的桌面级客户端，目前已实现其的部分功能，包括浏览，收藏，播放，本地字幕支持等。有着更出色的UI,更强大的桌面歌词。主要用于对于自建音乐的管理。
## 由于开发者懒，部分功能未实现，目前版本安装包暂未在github上发布，如需获取请进群下载。
**本项目仅供学习交流使用，请在下载后24小时内删除。**
**交流企鹅群：221915393**
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22760263/1689820288640-9da8ed18-2cd2-434f-abb9-e7679dfa748f.png#averageHue=%23344050&clientId=u0a798f1d-1ba0-4&from=paste&height=930&id=u696cb9d6&originHeight=930&originWidth=1540&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1018509&status=done&style=none&taskId=u71c598f8-fcd3-46f1-95bd-73d62eaa0c7&title=&width=1540)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22760263/1689820427346-c372a757-99c2-47e2-a5ee-2169668bbce1.png#averageHue=%237f7f74&clientId=u0a798f1d-1ba0-4&from=paste&height=925&id=ubbd45155&originHeight=925&originWidth=1538&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1068493&status=done&style=none&taskId=ud93bab53-2b52-483c-8f20-b2ecb3bb851&title=&width=1538)
# 二、使用说明
### 请记住：未来道具11号不是一款商业软件，没有精力去考虑与处理方方面面的异常。开发者的设计理念一直是在约定的范围内合理使用，则有着良好的体验。
#### 配置服务器和Token
程序在使用前必须打开设置界面，配置服务器地址，不配置Token则部分功能无法使用，如收藏，评分等。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22760263/1689820505325-5d0d3833-754a-40dc-af4b-9144746b1c09.png#averageHue=%231c1c23&clientId=u0a798f1d-1ba0-4&from=paste&height=213&id=u5f659ad8&originHeight=213&originWidth=1350&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11579&status=done&style=none&taskId=uee16eee5-f31b-4653-8f16-d9ed3ad819d&title=&width=1350)
服务器Api : 假设自建的Kikoeru的服务器地址是https://baidu.com,那么输入框填写的Api地址是https://api.baidu.com   请严格按照格式来填写。
Token: 傻瓜式操作，点击获取Token登录即可。
配置完上面两项就可以愉快使用了。
#### 本地字幕配置
KK Maid支持使用本地字幕文件，支持本地zip字幕包。可以在设置-字幕管理中配置本地字幕根文件夹和本地压缩包文件夹。
##### 本地字幕根文件夹：这个是所有字幕文件夹的根文件夹，里面放置的的是每个音声的字幕文件夹。请注意暂不支持子字幕文件夹的查找与读取。
##### 本地压缩包文件夹：里面放的是所有zip格式的字幕压缩包。请注意暂不支持子字幕文件夹的查找与读取
     同样在播放界面也可以手动导入字幕文件，具体请在播放界面自行研究。
# 三、问题与解答
#### 关于字幕文件相关，乱码问题
KK Maid只支持UTF-8格式的LRC字幕文件，非UTF-8格式的字幕无法正常读取，会产生乱码文字，倘若其他格式请使用其他编辑器更改。
如果在使用本地Zip字幕包时，字幕标题乱码，这是因为字幕包的打包环境编码格式导致的。例如MAC打包在Windows中读取会导致标题乱码，因为MAC的默认编码是UTF-8，而Windows默认编码是GBK。解决方法是手动重新打包,当然这并不影响字幕文本。
#### 字幕文件与当前播放并不配对问题
由于各种字幕标准不一，有的字幕名称是日文，有的是中文等其他语言，有的有特典字幕，有的没有特典字幕，自然难以保证百分百配对。当然KK Maid提供解决方法。你可以在播放界面，选择字幕管理功能，对不匹配的字幕进行手动适配。
# 四、感谢以下开源项目

1. [JFoenix](https://github.com/sshahine/JFoenix)
2. [FXTrayIcon](https://github.com/dustinkredmond/FXTrayIcon)
3. [ControlsFX](https://github.com/controlsfx/controlsfx)
4. [Jackson](https://github.com/FasterXML/jackson)
5. [Atlantafx](https://github.com/mkpaz/atlantafx)
6. [Openjfx](https://openjfx.io/)
7. [Zip4j](https://github.com/srikanth-lingala/zip4j)
8. [EventBus](https://github.com/greenrobot/EventBus)
# 五、如果该项目对您有所帮助，欢迎赞助支持。
