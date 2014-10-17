@setlocal
@REM Same as m2cinstall without -o

@SET A35=C:\Workspaces\A34\andromda35
@set OFFLINE= 
@REM Add -P and -D options on command line
@set OPTS=-e %1 %2 %3
@REM special version created where updateable dependencies are shown only once for all subprojects
@set VERSIONS=org.codehaus.mojo:versions-maven-plugin:2.2.1-SNAPSHOT
@set IGNORE=-Dmaven.test.failure.ignore=true

C:
cd %A35%
call mvn %OFFLINE% clean > %A35%\clean.txt
@REM Build sub-dependencies in the right order - circular/bootstrap relationships
cd %A35%\maven\maven-config
call mvn %OFFLINE% install %OPTS% > %A35%\install.log
cd %A35%\maven\maven-parent
call mvn %OFFLINE% install %OPTS% >> %A35%\install.log
cd %A35%\maven\model-archiver
call mvn %OFFLINE% install %OPTS% >> %A35%\install.log
cd %A35%\andromda-etc\andromda-script-wrappers
@REM call mvn %OFFLINE% install %OPTS% %IGNORE% >> %A35%\install.log
cd %A35%\cartridges\andromda-meta
@REM call mvn %OFFLINE% install %OPTS% >> %A35%\install.log
cd %A35%\metafacades
@REM call mvn %OFFLINE% install %OPTS% >> %A35%\install.log
cd %A35%\maven\bootstrap
@REM call mvn %OFFLINE% install %OPTS% %IGNORE% >> %A35%\install.log
cd %A35%
call mvn %OFFLINE% install %OPTS% >> %A35%\install.log
@REM create consolidated report from consolidated exec log in default location http://www.eclemma.org/jacoco/trunk/doc/report-mojo.html
@REM Measure overall code coverage with jacoco. Must create HTML report 'mvn jacoco:report' after completion. Do not run at the same time as -Pcoverage because of double instrumentation
@set MAVEN_OPTS=%MAVEN_OPTS% -javaagent:C:\\Programs\\m2repo\\org\\jacoco\\org.jacoco.agent\\0.7.1.201405082137\\org.jacoco.agent-0.7.1.201405082137-runtime.jar=destfile=C:\\Workspaces\\A34\\andromda35\\target\\jacoco-all.exec,append=true,includes=org.andromda.*
@REM Skip internal jacoco instrumentation since mvn command is instrumented
call mvn install -Djacoco.skip=true > %A35%\jacoco.log

@REM Check/display all dependency version updates for parent, andromda, and generated projects
cd %A35%\maven\maven-parent
@REM call mvn %OFFLINE% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates %OPTS% -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml > %A35%\versions.txt
@REM call mvn %OFFLINE% dependency:tree %OPTS% >> tree.txt
cd %A35%\maven\model-archiver
@REM call mvn %OFFLINE% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates %OPTS% -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
@REM call mvn %OFFLINE% dependency:tree %OPTS% >> tree.txt
cd %A35%
@REM call mvn %OFFLINE% dependency:tree %OPTS% > tree.txt
@REM call mvn %OFFLINE% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates %OPTS% -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
cd %A35%\samples\animal-quiz
@REM call mvn %OFFLINE% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates %OPTS% -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
@REM call mvn %OFFLINE% dependency:tree %OPTS% >> tree.txt

cd %A35%
call ant -f JacocoReport.xml > jacoco.txt

@endlocal
