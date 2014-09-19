@setlocal

@REM @set OPTS=-e
@rem @set JAVA_HOME=C:\Programs\JDK\7
@set OFFLINE= 
@set OPTS= -Pexternal
@set VERSIONS=org.codehaus.mojo:versions-maven-plugin:2.2.1-SNAPSHOT
@set IGNORE=-Dmaven.test.failure.ignore=true
@set A35=C:\Workspaces\A34\andromda35

cd %A35%
call mvn %OFFLINE% %OPTS% clean > clean.txt
cd %A35%\maven\maven-config
call mvn %OFFLINE% %OPTS% install %IGNORE% > %A35%\install.log
cd %A35%\maven-model-archiver
call mvn %OFFLINE% %OPTS% install %IGNORE% >> %A35%\install.log
cd %A35%\maven\bootstrap
call mvn %OFFLINE% %OPTS% install %IGNORE% >> %A35%\install.log
cd %A35%\andromda-etc\andromda-script-wrappers
call mvn %OFFLINE% %OPTS% install %IGNORE% >> %A35%\install.log
cd %A35%
call mvn %OFFLINE% %OPTS% clean > clean.txt
call mvn %OFFLINE% %OPTS% install %IGNORE% >> %A35%\install.log

c:
cd %A35%\maven\maven-parent
call mvn %OFFLINE% %OPTS% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml > %A35%\versions.txt
call mvn %OFFLINE% %OPTS% dependency:tree >> tree.txt
cd %A35%\maven\model-archiver
call mvn %OFFLINE% %OPTS% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
call mvn %OFFLINE% %OPTS% dependency:tree >> tree.txt
cd %A35%
call mvn %OFFLINE% %OPTS% dependency:tree > tree.txt
call mvn %OFFLINE% %OPTS% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
cd %A35%\samples\animal-quiz
call mvn %OFFLINE% %OPTS% %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml >> %A35%\versions.txt
call mvn %OFFLINE% %OPTS% dependency:tree >> tree.txt

cd %A35%\maven\maven-parent
call mvn %OFFLINE% %OPTS% install site site:deploy -Plocal,site-full -Dmaven.version.rules=file:/%A35%/maven/maven-config/src/main/resources/andromda/versions-rules.xml > %A35%\site.txt
cd %A35%
call mvn %OFFLINE% %OPTS% install site site:deploy -Plocal,site-full -Dmaven.version.rules=file:/%A35%/maven/maven-config/src/main/resources/andromda/versions-rules.xml >> %A35%\site.txt
@REM call mvn deploy -Pjavadocs,sonatype > sonatype.txt
@REM to deploy a single artifact - sonatype profile is never activated!
@REM call mvn deploy -Pjavadocs,sonatype -DaltDeploymentRepository=sonatype-nexus-snapshots::default::https://oss.sonatype.org/content/repositories/snapshots/ > sonatype.txt

c:
cd %A35%\samples\animal-quiz
@REM call mvn %OFFLINE% clean > %A35%\samples\clean.txt
@REM call mvn %OFFLINE% %OPTS% install > %A35%\samples\install.log
@REM call mvn deploy -Pjavadocs,local,gpg-andromda > %A35%\samples\deploy.txt
cd %A35%\samples\car-rental-system
@REM call mvn %OFFLINE% clean >> %A35%\samples\clean.txt
@REM call mvn %OFFLINE% %OPTS% install >> %A35%\samples\install.log
@REM call mvn deploy -Pjavadocs,local,gpg-andromda >> %A35%\samples\deploy.txt
cd %A35%\samples\crud
@REM call mvn %OFFLINE% clean >> %A35%\samples\clean.txt
@REM call mvn %OFFLINE% %OPTS% install >> %A35%\samples\install.log
@REM call mvn deploy -Pjavadocs,local,gpg-andromda >> %A35%\samples\deploy.txt
cd %A35%\samples\online-store
@REM call mvn %OFFLINE% clean >> %A35%\samples\clean.txt
@REM call mvn %OFFLINE% %OPTS% install >> %A35%\samples\install.log
@REM call mvn deploy -Pjavadocs,local,gpg-andromda >> %A35%\samples\deploy.txt

cd %A35%
@REM call mvn deploy -Pjavadocs,local > deploy.txt
@REM call mvn deploy -Pjavadocs,local,gpg-andromda >> %A35%\deploy.txt
@REM Sonatype problem with "peer not authenticated" see http://stackoverflow.com/questions/18325724/peer-not-authenticated-in-maven-when-trying-to-run-a-job-in-jenkins
@REM make sure you are on the latest maven version and have generated javadocs locally before uploading to remote
@REM call mvn deploy -Pjavadocs,sonatype -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true >> %A35%\sonatype.txt

@set MAVEN_OPTS=%MAVEN_OPTS% -javaagent:C:\\Programs\\m2repo\\org\\jacoco\\org.jacoco.agent\\0.7.1.201405082137\\org.jacoco.agent-0.7.1.201405082137-runtime.jar=destfile=C:\\Workspaces\\A34\\andromda35\\target\\jacoco-all.exec,append=true,includes=org.andromda.*
@REM Skip internal jacoco instrumentation since mvn command is instrumented
call mvn install -Djacoco.skip=true > %A35%\jacoco.log
call ant -f JacocoReport.xml > jacoco.txt
xcopy target\site\jacoco \scratch\hudson\workspace\Andromda\site\jacoco /Q /Y

@endlocal
pause
