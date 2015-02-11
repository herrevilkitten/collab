# colLab
## Building colLab

### Requirements
* Java 7+ JDK
* Maven

### Maven Profiles and Goals

* h2

This profile will configure the H2 database in pom.xml.  This is mostly used in conjuction with
the `generate-db` profile.

* generate-db

This profile will run the configured SQL in order to generate a fresh database.  It works best
when used with the `h2` profile and `clean` goal.

* clean

Goal

* package

Goal

* tomcat7:run

Goal

#### Running with embedded Tomcat 7

    mvn -Ph2,generate-db clean tomcat7:run

