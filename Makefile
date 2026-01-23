clean-config:
	mvn clean -f maven/maven-config

clean-parent:
	mvn clean -f maven/maven-parent

clean-model-archiver:
	mvn clean -f maven/model-archiver -Dmaven.test.skip=true

clean-maven:
	mvn clean -f maven

clean:
	mvn clean

maven-config:
	mvn install -f maven/maven-config

maven-parent:
	mvn install -f maven/maven-parent

model-archiver:
	mvn install -f maven/model-archiver -Dmaven.test.skip=true

install:
	mvn install -Dmaven.test.skip=true

andromdapp:
	mvn install -f andromda-andromdapp/ -Dmaven.test.skip=true

full: clean-config clean-parent clean-model-archiver clean-maven clean maven-config maven-parent model-archiver install andromdapp