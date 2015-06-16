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


__NOTE__

To Release: 

1. Validate: `mvn -P release clean install`
2. Go the the Bintray Web site and add a new version for the package.
3. Execute the release process as follows: 
    ```
    mvn release:prepare
    mvn release:perform
    ```
4. Go to the Bintray Web site and publish the new version.

