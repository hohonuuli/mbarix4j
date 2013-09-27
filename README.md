mbarix4j
========

Java extensions used with MBARI software applications. This project hosts Java extensions and convenience classes used by MBARI projects. It's only external dependency is [SLF4J](http://www.slf4j.org/).

To include it in your maven project add the following to your pom.xml file:

```xml
<!-- Add the MBARI repository -->
<repository>
    <id>mbari-maven-repository</id>
    <name>MBARI Maven Repository</name>
    <url>http://mbari-maven-repository.googlecode.com/svn/repository/</url>
</repository>


<!-- Add mbarix4j dependency -->
<dependency>
    <groupId>org.mbari</groupId>
    <artifactId>mbarix4j</artifactId>
    <version>1.9.2</version>
</dependency>

```
