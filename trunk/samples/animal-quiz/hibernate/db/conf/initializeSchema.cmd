rem
rem This script does something to the HSQL database which is part of the
rem JBoss distribution. The script is _not_ meant to run from the source tree
rem but from the same directory as the *.sql files that the build script
rem has generated.
rem
rem Matthias Bohlen
rem
java -classpath %JBOSS_HOME%\server\default\lib\hsqldb.jar org.hsqldb.util.ScriptTool -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:hsql: -database //localhost:1701 -script hibernate-schema-initialize.sql
