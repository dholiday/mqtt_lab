# FOR BUILDING A DOCKERIZED POSTGRES CONTAINER FOR USE BY 
# A GRAPHITE SERVER CONTAINER  
#
FROM postgres
MAINTAINER David Holiday <dholiday@moesol.com>


# SETUP GRAPHITE USER CREDENTIALS
#
ENV POSTGRES_USER graphite
ENV POSTGRES_PASSWORD password


# PROVISION WITH REQUIRED PACKAGES
#
RUN apt-get update
RUN apt-get install -y apt-utils
RUN apt-get install -y libpq-dev python-psycopg2




