# QG最终考核-打印机项目文档

## 1.0	项目理解

​	简要概括: 整个项目只是一个"查看系统"，需要程序读取的信息已经由师兄写好的程序给出(或者说已经由机器产生,我们需要做的**只是读取并显示到网页上**)。**产生数据**的规律为:**每秒**输出信息一条到**txt文件**。简单来说就是要实现读取新增信息并解析，再执行录入MySQL数据库和响应前端请求等等功能。

​	相比后两个题目更加注重后台部分的设计。

## 2.0	项目环境

	- **IDE：**我使用的IDE位jetbrains的IDEA
	- **Java版本**: Java SE 8
	- **数据库:** MySQL 8.0.32
 - **服务器:**Tomcat 9.0.73
    - 在IDEA内部署后登录页面为localhost:8080/QGFinal_war/login.html
 - **技术选择:**JavaWeb的各项技术、前端页面使用了Jquery框架和Websocket通信。后台代码使用了自定义的ConnectionPool和CRUDUtil类,导入了FastJSON、jjwt、websocket、httpclient等依赖。

## 3.0	项目架构

​	总体结构:

![myjiegou](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/myjiegou.png)

### 3.1	模块设计

​	本项目遵循MVC设计规范，将代码分为了**表示层**(controller)、**业务逻辑层**(service)、**数据访问层**(dao)三层。

![Snipaste_2023-05-01_21-22-39](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/Snipaste_2023-05-01_21-22-39.png)

​	此外还有常量类和工具类等**辅助模块**,在实体类包下对数据对象进行了**DTO**(数据传输对象,主要面向前端)、**PO**(持久对象,主要面向数据库)、**BO**(业务对象,主要面向前后端和后端内的交互)

### 3.2	模块功能

#### 3.2.1	常量类和配置文件

![Snipaste_2023-05-01_21-35-22](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/Snipaste_2023-05-01_21-35-22.png)

	- 由于项目中存在多角色多权限,所以设计了若干代表角色和权限的常量类,实现RBAC模型配合JWT模块进行认证鉴权
	- 对于项目中打印机可能出现的12种状态码及其附加信息，我使用了一个枚举类PrinterStatus来代表它们在后台中的基本特征
	- 为了便于实现JSON的统一结果集处理,我使用了一个枚举类来包装了常用的http响应的状态码、响应是否成功的布尔值等信息

![Snipaste_2023-05-01_21-39-21](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/Snipaste_2023-05-01_21-39-21.png)

- 对于自定义的数据库连接池和jwt认证鉴权的工具雷,我采用了配置文件的方式软编码存储配置信息，实现直接、简单，参数调整方便。

#### 3.2.2	controller层

![contor](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/contor.png)



 - 首先,我使用了BaseServlet通过**反射**整合了所有的Servlet,所有的Servlet都是BaseServlet的子类,不必再每个方法一个Servlet,但是我还是根据不同的功能大类分开了若干个Servlet类
	- `checkTokenServlet`中包装着检查并解析前端请求中的token，最后返回检查通过/不通过的结果和一些其它数据的方法

![checktoken](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/checktoken.png)



 - `PrinterServlet`中包装着与打印机本身有关的方法:`getPrinter`根据角色权限返回可供角色操作的打印机,`getPrinterName`根据打印机ID获取打印机名

![printerser](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/printerser.png)



- `TxtDataServlet`中包装着若干调用处理打印机数据的方法,并将它们发送回给前端的方法。

![txtdata](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/txtdata.png)

​	为了处理复数打印机的情况,我使用了两个静态的Map来维护绑定打印机id的相关数据。



- `UserServlet`中封装着与用户登陆注册等操作进行相应的方法

![userser](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/userser.png)



- `WebSocketServer`和`MyEndpointConfigurator`是项目中websocket连接的实现类, 其中`MyEndpointConfigurator`实现了`endpointConfigurator`接口,让我的前端可以通过websocket连接,传递url中的参数, 使我的websocket更加灵活高效

    ![websock](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/websock.png)

​	在`WebSocketServer`中我通过一个静态的Map来记录前端正在打开的，所有url相同的打印机详情界面的Session，并在后台做好对应的筛选后进行广播。



#### 3.2.3	dao层

​	![dao](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/dao.png)



1. `PrinterDAO`是打印机的持久化类,封装了在数据库中查询符合条件的打印机的方法

![prindao](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/prindao.png)



2. `TxtDAO`封装了若干与打印机的Txt数据的持久化相关方法

![txtdao](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/txtdao.png)

​	Txt数据的插入还是没能用减少数据库CRUD压力的方式实现,底层还是为一秒一读写。其中**统计时间数据insertData**我使用了LocalTime这个Java8新增的时间类对txt中的时间进行统计分析,因为其api有方法可以直接**根据秒数获取时间**,且此时间超过一天中的秒数的话就会被**取余**



3. `UserDAO`封装了若干与用户的登录注册数据的持久化和从数据库中查找用户的相关方法

![userdao](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/userdao.png)



4. `TxtWatcher`和`TxtWatcherThread`封装着从本地路径中读取扫描并发送给对应Servlet的方法,本项目采用多个线程来模拟多台打印机的情况。`TxtWatcherThread`重写了run方法

![txtwatcher](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/txtwatcher.png)



#### 3.2.4	数据对象pojo层

​	中期进度检查时听取了师兄的建议对这一部分做了改进,才有如今的模样。

##### 3.2.41	BO业务对象

![bo](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/bo.png)

![ps](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/ps.png)![prm](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/prm.png)

​	两个类分别对项目要求中的**统计时间数据**和**txt数据**做最初步的包装,它们的对象会在后台各层间传输并做进一步的打包处理。



##### 3.2.42	DTO数据传输对象

![dto](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/dto.png)

​	此对象面向前端,定义了许多会直接传输给前端的变量以及便于后台自定义响应码的若干方法。

##### 3.2.43	PO持久对象

![po](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/po.png)

​	此包中的对象面向数据库存储,由DTO进一步处理包装而来。



#### 3.2.5	service层

![service](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/service.png)

​	此层中的类中定义了大量Controller层的功能的实际逻辑(方法),Contoller层中的方法大多只是实际逻辑的一层或多层包装。



- `PrinterService`定义了`PrinterServlet`中的两个方法的逻辑

![pser](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/pser.png)



- `TxtDataHttpService`定义了供模拟多台打印机发送txt数据的方法,多个线程读取信息,发送http请求给服务端。实现了**驻留端和服务端的分离**。

![txtdatahttpser](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/txtdatahttpser.png)



- `TxtDataManageService`定义了大量处理打印机的txt数据的方法的实际逻辑

![txtdatamangser](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/txtdatamangser.png)



- `UserService`定义了大量与用户账号操作方法相关的实际逻辑

![userservice](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/userservice.png)



#### 3.2.6	utils包

![utils](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/utils.png)



- `MyConnectionPool`我的连接池一开始是用的LinkedList加上一个锁来实现的,但好像我的并发编程掌握的并不是很熟练,但最后将List替换为线程安全的ConcurrentLinkedQueue就解决了问题

![myconpool](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/myconpool.png)



- `JwtUtil`是jwt认证鉴权的工具类,也是本项目jwt认证功能的实际实现类

![jwtutil](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/jwtutil.png)



#### 3.2.7	webapp部分

![webapp](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/webapp.png)

​	一个html页面,一个js代码，一个css样式。



## 4.0	数据库结构设计

​	![mysqlrep](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/mysqlrep.png)



​	打印机数据的结构图如下:

![printer_statistic](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/printer_statistic.png)

​	

​	用户数据的结构图如下:

![role_permission_map](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/role_permission_map.png)



## 5.0	界面设计

	- 登录页面:

![menu](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/menu.png)



- 注册界面:

![regist](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/regist.png)

​	注册界面使用了正则表达式检测输入,同时对不正确格式的输入又实时红色字体提示。



- 大厅:![hall](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/hall.png)

​	可以显示自己有权限查看的打印机连接



- 用户个人信息中心:

![center](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/center.png)

​	普通用户需要绑定企业个人信息里的绑定码和企业账号名才能查看打印机



- 打印机详情界面:

![detail](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/detail.png)

![prin1](https://mytyporapicute.oss-cn-guangzhou.aliyuncs.com/typoraPics/prin1.png)



## 6.0	部署说明

	1. 如果没有修改localhost:8080的话, 项目在IDEA中部署以后的登陆页面地址为:http://localhost:8080/QGFinal_war/login.html
 	2. 连接池的配置文件中的数据库名默认是qgifinal，实际部署时需要修改成自己导入sql脚本的数据库名，不然会报错。username和password也得修改成自己的数据库用户名和密码



## 7.0	心得体会

​	1. 在考核布置当天,我花了整个下午还有晚上去解读项目需求和勾勒规划项目骨架,现在想来这一部操作十分有用,如果养成习惯对我日后大有裨益。

	2. 在进行考核项目的过程中,我曾非常烦躁郁闷,也遇到了非常非常多以往看来难如登天的问题,但他们之中的98%最后都被解决了,其中也有至少也有100%在时候事后被我总结成文,我想这些才是我通过这次这次考核的收获。
	2. 虽然最后还有温度曲线和前端打印机的注册这些基本功能没有完成,打印机数据的正确监听条件也颇为苛刻,但至少这两周十六天我每天是脚踏实地地去完成了一个能拿得出手的项目,能自豪的展示给同学和家人看的项目。很值。