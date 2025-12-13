## Maven scope依赖范围详解

```text
compile：默认的scope，运行期有效，需要打入包中
provided：编译期有效，运行期不需要提供，不会打入包中
runtime：编译不需要，在运行期有效，需要导入包中。（接口与实现分离）
test：测试需要，不会打入包中
system：非本地仓库引入、存在系统的某个路径下的jar。（一般不使用）
```
