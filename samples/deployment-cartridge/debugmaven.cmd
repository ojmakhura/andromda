setlocal
set MAVEN_OPTS=-classic -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y %MAVEN_OPTS%
call mvn -o andromda-cartridge:test
