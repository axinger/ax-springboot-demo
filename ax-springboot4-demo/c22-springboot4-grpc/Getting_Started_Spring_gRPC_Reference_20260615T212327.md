# Getting Started :: Spring gRPC Reference

You should follow the steps in each of the following section according to your needs.

### [](https://docs.spring.io/spring-grpc/reference/getting-started.html#repositories)Add Milestone and Snapshot Repositories

If you prefer to add the dependency snippets by hand, follow the directions in the following sections.

To use the Milestone and Snapshot version, you need to add references to the Spring Milestone and/or Snapshot repositories in your build file.

For Maven, add the following repository definitions as needed (if you are using snapshots or milestones):

```xml
<repositories>
  <repository>
    <id>spring-milestones</id>
    <name>Spring Milestones</name>
    <url>https://repo.spring.io/milestone</url>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
  <repository>
    <id>spring-snapshots</id>
    <name>Spring Snapshots</name>
    <url>https://repo.spring.io/snapshot</url>
    <releases>
      <enabled>false</enabled>
    </releases>
  </repository>
</repositories>
```

For Gradle, add the following repository definitions as needed:

```groovy
repositories {
  mavenCentral()
  maven { url 'https://repo.spring.io/milestone' }
  maven { url 'https://repo.spring.io/snapshot' }
}
```

### [](https://docs.spring.io/spring-grpc/reference/getting-started.html#dependency-management)Dependency Management

The `spring-grpc-dependencies` artifact declares the recommended versions of the dependencies used by a given release of Spring gRPC, excluding dependencies already managed by Spring Boot dependency management.

The `spring-grpc-build-dependencies` artifact declares the recommended versions of all the dependencies used by a given release of Spring gRPC, including dependencies already managed by Spring Boot dependency management.

If you are running Spring gRPC in a Spring Boot application then use `spring-grpc-dependencies`, otherwise use `spring-grpc-build-dependencies`.

Using one of these dependency modules avoids the need for you to specify and maintain the dependency versions yourself.
Instead, the version of the dependency module you are using determines the utilized dependency versions.
It also ensures that you’re using supported and tested versions of the dependencies by default, unless you choose to override them.

|  | The examples below assume you are running inside a Spring Boot application and therefore use `spring-grpc-dependencies`. |
| --- | ------------------------------------------------------------------------------------------------------------------------ |

If you’re a Maven user, you can use the dependencies by adding the following to your pom.xml file \-

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.grpc</groupId>
            <artifactId>spring-grpc-dependencies</artifactId>
            <version>1.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Gradle users can also use the dependencies by leveraging Gradle (5\.0\+) native support for declaring dependency constraints using a Maven BOM.
This is implemented by adding a 'platform' dependency handler method to the dependencies section of your Gradle build script.
As shown in the snippet below this can then be followed by version\-less declarations of the Starter Dependencies for the one or more spring\-grpc modules you wish to use, e.g. spring\-grpc\-openai.

```gradle
dependencies {
  implementation platform("org.springframework.grpc:spring-grpc-dependencies:1.0.3")
}
```

You need a Protobuf file that defines your service and messages, and you will need to configure your build tools to compile it into Java sources. This is a standard part of gRPC development (i.e. nothing to do with Spring). We now come to the Spring gRPC features.

### [](https://docs.spring.io/spring-grpc/reference/getting-started.html#_gprc_server)gPRC Server

Create a `@Bean` of type `BindableService`. For example:

```java
@Service
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {
...
}
```

(`BindableService` is the interface that gRPC uses to bind services to the server and `SimpleImplBase` was created for you from your Protobuf file.)

Then, you can just run your application and the gRPC server will be started on the default port (9090\). Here’s a simple example (standard Spring Boot application):

```java
@SpringBootApplication
public class GrpcServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}
}
```

Run it from your IDE, or on the command line with `./mvnw spring-boot:run` or `./gradlew bootRun`.

### [](https://docs.spring.io/spring-grpc/reference/getting-started.html#_grpc_client)gRPC Client

To create a simple gRPC client, you can use the Spring Boot starter (see above \- it’s the same as for the server). Then you can inject a bean of type `GrpcChannelFactory` and use it to create a gRPC channel. The most common usage of a channel is to create a client that binds to a service, such as the one above. The Protobuf\-generated sources in your project will contain the stub classes, and they just need to be bound to a channel. For example, to bind to the `SimpleGrpc` service on a local server:

```java
@Bean
SimpleGrpc.SimpleBlockingStub stub(GrpcChannelFactory channels) {
	return SimpleGrpc.newBlockingStub(channels.createChannel("0.0.0.0:9090"));
}
```

Then you can inject the stub and use it in your application.

The default `GrpcChannelFactory` implementation can also create a "named" channel, which you can then use to extract the configuration to connect to the server. For example:

```java
@Bean
SimpleGrpc.SimpleBlockingStub stub(GrpcChannelFactory channels) {
	return SimpleGrpc.newBlockingStub(channels.createChannel("local"));
}
```

then in `application.properties`:

```properties
spring.grpc.client.channels.local.address=0.0.0.0:9090
```

There is a default named channel that you can configure in the same way via `spring.grpc.client.default-channel.*`. It will be used by default if there is no channel with the name specified in the channel creation.

### [](https://docs.spring.io/spring-grpc/reference/getting-started.html#_native_images)Native Images

Native images are supported for gRPC servers and clients. You can build in the [normal Spring Boot](https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html) way for your build tool (Maven or Gradle).
