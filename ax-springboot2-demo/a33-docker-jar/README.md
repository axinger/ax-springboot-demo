构建自身和依赖

```shell
mvn clean package -pl a33-docker-jar -DskipTests -am
```

构建自身和自身被依赖的模块

```
mvn clean package -pl a33-docker-jar -DskipTests -am -amd
```

```text
这条命令会：
构建 a33-docker-jar 模块所依赖的所有模块（-am 的作用）
构建 a33-docker-jar 模块本身
构建所有依赖于 a33-docker-jar 模块的其他模块（-amd 的作用）
实际应用场景
在您的项目中，如果 a33-docker-jar 模块依赖于一些公共模块（如 axinger-common 等），同时可能有其他模块依赖于 a33-docker-jar，那么使用这个完整的命令可以确保：
所有 a33-docker-jar 依赖的模块都被正确构建
a33-docker-jar 模块本身被构建
所有依赖于 a33-docker-jar 的模块也被构建
这样可以保证整个依赖链的完整性和正确性，避免因为依赖模块未构建而导致的构建失败。
```
