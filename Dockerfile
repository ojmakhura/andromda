FROM maven:3.8.6-openjdk-11 as builder

RUN apt-get update 
RUN apt-get install -y git && \
    apt-get clean

RUN mkdir -p /app
WORKDIR /app
RUN git clone https://github.com/ojmakhura/doctor-jim.git
WORKDIR /app/doctor-jim
RUN pwd
RUN mvn install -Dmaven.test.skip=true
# ARG CACHEBUST=1
WORKDIR /app/andromda
COPY ./ /app/andromda/
RUN pwd && ls
RUN ls -l
RUN mvn install -f maven/maven-config
RUN mvn install -f maven/maven-parent
RUN mvn install -f maven/model-archiver
RUN mvn install -Dmaven.test.skip=true
RUN mvn install -f andromda-andromdapp/ -Dmaven.test.skip=true

WORKDIR /root
RUN pwd
RUN cd .m2

FROM maven:3.8.6-openjdk-11 
COPY --from=builder /root/.m2 /root/.m2