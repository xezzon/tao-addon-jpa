# tao-addon-jpa

依赖于 JPA，对 [tao-core](https://github.com/xezzon/tao) 核心组件进行扩展与实现。

## 功能清单

- 从 CommonQuery 生成 JPA 查询语句（含筛选、排序、分页）
- 生成局部更新语句。
- 使用 NewType 对 JpaRepository 进行封装。 
- 定义 AbstractDataset 抽象类，用于初始化数据。

## 快速开始

### Maven

```xml
<project>
  <dependencies>
    <dependency>
      <groupId>io.github.xezzon</groupId>
      <artifactId>tao-core</artifactId>
      <version>0.11.0</version>
    </dependency>
    <dependency>
      <groupId>io.github.xezzon</groupId>
      <artifactId>tao-addon-jpa</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
</project>
```