````
  ____  ____ _____   ____                 _                                  _   
 / ___||  _ \_   _| |  _ \  _____   _____| | ___  _ __  _ __ ___   ___ _ __ | |_ 
 \___ \| |_) || |   | | | |/ _ \ \ / / _ \ |/ _ \| '_ \| '_ ` _ \ / _ \ '_ \| __|
  ___) |  __/ | |   | |_| |  __/\ V /  __/ | (_) | |_) | | | | | |  __/ | | | |_ 
 |____/|_|    |_|   |____/ \___| \_/ \___|_|\___/| .__/|_| |_| |_|\___|_| |_|\__|
                                                 |_|                                           
 audit-spring-boot---------------------------------------------------------------
````

[![build_status](https://travis-ci.com/spt-development/spt-development-cid-audit-boot.svg?branch=main)](https://travis-ci.com/spt-development/spt-development-audit-spring-boot)

Library for integrating 
[spt-development/spt-development-audit-spring](https://github.com/spt-development/spt-development-audit-spring) 
into a Spring Boot application.

Usage
=====

Simply add the Spring Boot starter to your Spring Boot project pom.

    <dependency>
        <groupId>com.spt-development</groupId>
        <artifactId>spt-development-audit-spring-boot-starter</artifactId>
        <version>1.0.0</version>
        <scope>runtime</scope>
    </dependency>

To configure the `JmsAuditEventWriter` rather than the default `Slf4jAuditEventWriter` set the `spt.audit.jms.destination`
property to the name of your JMS audit event queue/topic and ensure a `JmsTemplate` bean is created.

Building locally
================

To build the library, run the following maven command:

    $ mvn clean install

Release
=======

To build a release and upload to Maven Central run the following maven command:

    $ export GPG_TTY=$(tty) # Required on Mac OS X
    $ mvn deploy -DskipTests -Prelease

NOTE. This is currently a manual step as not currently integrated into the build.
