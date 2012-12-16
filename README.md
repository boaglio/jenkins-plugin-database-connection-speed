jenkins-plugin-database-connection-speed
========================================

Jenkins plugin to inform database connection speed.

For installing Oracle JDBC Driver:

1. Download the latest Oracle JDBC driver here: http://www.oracle.com/technetwork/indexes/downloads/index.html.
Do a search on "JDBC" to find the correct download link.


2. Now, we can install the JAR file with the following command:

mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6
  -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=ojdbc6.jar -DgeneratePom=true

Be sure to check the parameters, like the version number.
