# Plan: Extract Axinger Modules to Independent Maven Projects

## Context
The user wants to extract the following modules from the existing Spring Boot demo projects into independent Maven projects that can be published to Maven repositories (local or remote):

- axinger-cloud
- axinger-cloud-bom  
- axinger-common
- axinger-common-bom
- axinger-config
- axinger-config-bom

These modules should be separated from the main demo projects and made available as standalone dependencies that can be imported via Maven coordinates.

## Analysis
From examining the codebase, I found:

1. **ax-springboot2-demo** contains all 6 axinger modules
2. **ax-springboot3-demo** contains 2 axinger modules (axinger-spring-boot3-cloud, axinger-spring-boot3-common)
3. **ax-springboot4-demo** contains no axinger modules

The axinger modules are structured as:
- **axinger-common**: Contains utility starters (redis, quartz, mongodb, minio, excel, etc.)
- **axinger-config**: Contains configuration starters (result, doc, base, mybatis-plus, etc.)
- **axinger-cloud**: Contains cloud-related starters (gateway)
- **BOM modules**: Provide dependency management for each category

## Implementation Plan

### ✅ Phase 1: Create Independent Project Structure - COMPLETED
1. ✅ Create a new root directory `axinger-modules/`
2. ✅ Set up independent Maven projects for each module group:
   - ✅ `axinger-common/` (with all sub-modules)
   - ✅ `axinger-config/` (with all sub-modules)
   - ✅ `axinger-cloud/` (with all sub-modules)
   - ✅ `axinger-common-bom/`
   - ✅ `axinger-config-bom/`
   - ✅ `axinger-cloud-bom/`

### ✅ Phase 2: Update POM Files - COMPLETED
1. ✅ Remove parent references to demo projects
2. ✅ Set up proper groupId, artifactId, version
3. ✅ Configure Maven deployment plugins
4. ✅ Set up proper dependency management

### ⚠️ Phase 3: Build and Deploy - PENDING (Maven not available)
1. ⚠️ Test building each module independently
2. ⚠️ Deploy to local Maven repository
3. ⚠️ Update main demo projects to use the extracted modules

### ✅ Phase 4: Update Demo Projects - COMPLETED
1. ✅ Remove axinger modules from demo project modules lists
2. ✅ Add dependencies to the extracted modules  
3. ✅ Update parent POMs to import BOMs

## Critical Files to Modify

### New Independent Projects:
- `/axinger-modules/axinger-common/pom.xml`
- `/axinger-modules/axinger-config/pom.xml` 
- `/axinger-modules/axinger-cloud/pom.xml`
- `/axinger-modules/axinger-common-bom/pom.xml`
- `/axinger-modules/axinger-config-bom/pom.xml`
- `/axinger-modules/axinger-cloud-bom/pom.xml`

### Existing Demo Projects to Update:
- `/ax-springboot2-demo/pom.xml`
- `/ax-springboot3-demo/pom.xml`
- `/ax-springboot4-demo/pom.xml`

## Verification
1. Build each extracted module: `mvn clean install`
2. Verify demo projects can build with new dependencies
3. Test that all starters are properly loaded in demo applications
4. Confirm no breaking changes in existing functionality