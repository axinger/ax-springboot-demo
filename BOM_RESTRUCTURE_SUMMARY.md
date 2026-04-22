# BOM 结构调整总结

## 调整目标
将原来的三个独立 BOM 模块（axinger-common-bom、axinger-config-bom、axinger-cloud-bom）重新组织为一个总 BOM + 分类 BOM 的层次结构，让用户可以灵活选择使用方式。

## 新的 BOM 结构

### 1. 总 BOM（推荐使用）
- **artifactId**: `axinger-bom`
- **功能**: 包含所有 axinger starters 的统一依赖管理
- **位置**: `axinger-modules/axinger-bom/`
- **使用场景**: 需要使用多个分类的 starters 时

### 2. 分类 BOM（按需使用）
- **axinger-common-bom**: 通用工具类 starters
- **axinger-config-bom**: 配置类 starters  
- **axinger-cloud-bom**: 云相关 starters
- **特点**: 继承自总 BOM，保持版本一致性
- **使用场景**: 只需要特定分类的 starters 时

## 结构调整详情

### 原结构
```
axinger-modules/
├── axinger-common-bom/     # 独立 BOM
├── axinger-config-bom/     # 独立 BOM
└── axinger-cloud-bom/      # 独立 BOM
```

### 新结构
```
axinger-modules/
└── axinger-bom/                    # 总 BOM
    ├── pom.xml                     # 主 BOM，包含所有依赖
    ├── axinger-common-bom/         # 分类 BOM（继承自主 BOM）
    ├── axinger-config-bom/         # 分类 BOM（继承自主 BOM）
    └── axinger-cloud-bom/          # 分类 BOM（继承自主 BOM）
```

## 使用方式对比

### 调整前（必须引入三个 BOM）
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-common-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-config-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-cloud-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 调整后（灵活选择）

#### 方式1：使用总 BOM（最推荐）
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 方式2：按需引入分类 BOM
```xml
<dependencyManagement>
    <dependencies>
        <!-- 只需要通用工具类 -->
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-common-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        
        <!-- 只需要配置类 -->
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-config-bom</artifactId>
            <version>2026.01.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 优势

### 1. 简化配置
- 使用总 BOM 时只需引入一个依赖
- 减少配置复杂度
- 避免遗漏必要的 BOM

### 2. 保持灵活性
- 仍可按需引入分类 BOM
- 适应不同项目的需求
- 避免引入不必要的依赖管理

### 3. 版本一致性
- 分类 BOM 继承自总 BOM
- 确保版本统一管理
- 避免版本冲突

### 4. 向后兼容
- 原有的分类 BOM 仍然可用
- 现有项目无需强制修改
- 平滑迁移

## 文件变更

### 新增文件
- `/axinger-modules/axinger-bom/pom.xml` - 总 BOM 配置

### 修改文件
- `/axinger-modules/axinger-bom/axinger-common-bom/pom.xml` - 添加 parent 配置
- `/axinger-modules/axinger-bom/axinger-config-bom/pom.xml` - 添加 parent 配置
- `/axinger-modules/axinger-bom/axinger-cloud-bom/pom.xml` - 添加 parent 配置
- `/ax-springboot2-demo/pom.xml` - 更新 BOM 导入
- `/ax-springboot3-demo/pom.xml` - 更新 BOM 导入
- `/ax-springboot4-demo/pom.xml` - 更新 BOM 导入
- `/axinger-modules/README.md` - 更新使用说明

### 目录结构调整
- 将原有的三个 BOM 目录移动到 `axinger-bom/` 子目录下
- 保持文件内容不变，仅调整组织结构

## 使用建议

### 新项目推荐
- **首选**: 使用总 BOM（`axinger-bom`）
- **优点**: 配置简单，包含所有功能

### 现有项目
- **可以继续使用**: 分类 BOM
- **建议逐步迁移**: 到总 BOM 以简化配置

### 特定需求项目
- **按需选择**: 只引入需要的分类 BOM
- **例如**: 只需要工具类的项目只引入 `axinger-common-bom`

## 后续维护

1. **版本更新**: 只需更新总 BOM 的版本
2. **新增模块**: 在总 BOM 中添加，分类 BOM 会自动继承
3. **依赖管理**: 统一在总 BOM 中管理版本

---

**结构调整完成，使用更加灵活便捷** ✅