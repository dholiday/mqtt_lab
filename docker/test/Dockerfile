FROM ubuntu:latest
LABEL maintainer="your.name@youremail.com"

# copy the helper scripts from the root 'Dockerfile' into docker context
COPY . /

# spin up and grab some dependencies
RUN apt-get update
RUN apt-get install -y wget openjdk-8-jre git

# grab kafka quickstart
RUN wget http://www.gtlib.gatech.edu/pub/apache/kafka/2.1.0/kafka_2.11-2.1.0.tgz
RUN tar -xzf kafka_2.11-2.1.0.tgz

# make sure the kafka broker port is exposed
EXPOSE 9092

# override the entrypoint specificed by the mongodb dockerfile
ENTRYPOINT ["sh"]

# set default command to our bootstrap script
CMD ["docker_contents_bootstrap.sh"]