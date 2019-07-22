# backstage
基于dubbo的分布式服务后台 

项目结构：
1.bobo-api 与前端交互的api接口
2.bobo-parent maven parent 依赖
3.bobo-support 微服务的支持组件,提供dubbo封装类，注释文档动态生成工具，以及过滤器等
4.bobo-user 独立业务模块,可无限扩展,模块名称bobo-xxx 规范定义

项目特色：
   1.提供动态api接口测试平台,方面后端接口即使修改调试,提供动态api文档，后端接口的修改前端即时可见
   2.mapper-generator 一键生成dao框架,使用mapper接口代理减少dao层代码编写
   3.bobo-user 下com.bobo.core.build.method.BuildMethod 业务框架构建类一键生成业务接口和出参入参bean
   4.框架轻量适合快速开发业务代码
