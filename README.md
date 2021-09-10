# CAAIN
人工智能协会评奖系统

- 专家可以在该平台浏览申报项目的PDF文件并参与投票
- 平台提供管理员页面，用于发起某奖项投票&生成每个专家投票结果的PDF文件和总的评奖结果文件
- 采用约定大于配置的设计，尽量减少配置文件数量，将参评项目信息按照指定格式存放到指定文件夹下，系统可以自动解析生成参评信息

# 代码说明:

webapp：
   - css/font/image: 渲染资源及定义
   - view: jsp页面
   - script: 页面引用的javascript，使用ajax支持实时渲染

resources:
   - config:
      - kjcdoc: 奖项文件初始化路径配置
      - kjcexpert: 专家ip配置，匹配请求ip到专家名，在投票时将投票结果存到数据库
   - spring:
      - spring-data: 内置HSOL数据库配置
      - spring-mvc: springmvc基本配置
      - spring-tx: transactionmanager配置
   - sql: 数据库建表sql

main:
   - controller: 页面请求拦截及服务调用模块，包括后台管理界面(admin), 后台数据查看界面(view), 前台展示界面(home), 前台投票界面(vote)
   - model: DAO定义模块，包括专家、奖项
   - pojo: pojo定义模块，包括专家、奖项
   - service：基于dao封装controller需求的服务，包括参选项目展示、投票、投票结果展示等

# 部署说明

代码打包后用tomcat(v8.5.3)部署，运行初始会基于数据库建表sql新建一个内置的hsql数据库，并根据kjcdoc内定义的路径发现参评项目资源、填充到数据库。评选流程中给专家看的主要是home和vote界面，维护人员通过admin及view界面调度及监控系统。对应页面的请求会根据申明的mapper分发到指定controller, controller通过调用内部service反馈请求。 
