FROM java:8
MAINTAINER Dmitry Rakushev <rakushvdima@gmail.com>
#RUN aws configure set default.region us-east-1
WORKDIR /
ADD target/av-reader-0.0.1-SNAPSHOT.jar av-reader.jar
EXPOSE 8080
CMD java -jar av-reader.jar
