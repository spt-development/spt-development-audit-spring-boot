````
  ____  ____ _____   ____                 _                                  _   
 / ___||  _ \_   _| |  _ \  _____   _____| | ___  _ __  _ __ ___   ___ _ __ | |_ 
 \___ \| |_) || |   | | | |/ _ \ \ / / _ \ |/ _ \| '_ \| '_ ` _ \ / _ \ '_ \| __|
  ___) |  __/ | |   | |_| |  __/\ V /  __/ | (_) | |_) | | | | | |  __/ | | | |_ 
 |____/|_|    |_|   |____/ \___| \_/ \___|_|\___/| .__/|_| |_| |_|\___|_| |_|\__|
                                                 |_|                                           
 audit-spring-boot---------------------------------------------------------------
````

[![build_status](https://github.com/spt-development/spt-development-audit-spring-boot/actions/workflows/build.yml/badge.svg)](https://github.com/spt-development/spt-development-audit-spring-boot/actions)

Library for integrating 
[spt-development/spt-development-audit-spring](https://github.com/spt-development/spt-development-audit-spring) 
into a Spring Boot application.

Usage
=====

Add the Spring Boot starter to your Spring Boot project pom.

```xml
<dependency>
    <groupId>com.spt-development</groupId>
    <artifactId>spt-development-audit-spring-boot-starter</artifactId>
    <version>1.0.0</version>
    <scope>runtime</scope>
</dependency>
```

To configure the `JmsAuditEventWriter` rather than the default `Slf4jAuditEventWriter` set the `spt.audit.jms.destination`
property to the name of your JMS audit event queue/topic and ensure a `JmsTemplate` bean is created.

The auto-configuration, relies on build information from the `org.springframework.boot.info.BuildProperties` bean. To
make this bean available, add the `build-info` execution to the `spring-boot-maven-plugin` configuration.

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <id>build-info</id>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Additionally, you should set the `spring.application.name` property to change the name of the application (that is 
added to the audit events) from the default "Spring Boot".

Building locally
================

To build the library, run the following maven command:

```shell
$ mvn clean install
```

Release
=======

To build a release and upload to Maven Central push to `main`.
