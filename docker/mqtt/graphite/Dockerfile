# for building a docker graphite metrics web application image
# 
FROM ubuntu:14.04
MAINTAINER David Holiday <dholiday@moesol.com>


# grab needed libraries
#
RUN sudo apt-get update
RUN sudo apt-get -y install graphite-web graphite-carbon vim python-psycopg2 python-pip apache2 libapache2-mod-wsgi expect
RUN sudo pip install django-getenv


# update django config file
# 
RUN sudo sed -i '5 i\from getenv import env' /etc/graphite/local_settings.py

RUN SECRET_KEY_OLD="#SECRET_KEY = 'UNSAFE_DEFAULT'" &&\
  SECRET_KEY_NEW="SECRET_KEY = 'a_salty_string'" &&\
  sudo sed -i "s|$SECRET_KEY_OLD|$SECRET_KEY_NEW|" /etc/graphite/local_settings.py

RUN TIME_ZONE_OLD="#TIME_ZONE = 'America/Los_Angeles'" &&\
  TIME_ZONE_NEW="TIME_ZONE = 'America/Los_Angeles'" &&\
  sed -i "s|$TIME_ZONE_OLD|$TIME_ZONE_NEW|" /etc/graphite/local_settings.py

RUN REMOTE_USER_AUTH_OLD="#USE_REMOTE_USER_AUTHENTICATION = True" &&\
  REMOTE_USER_AUTH_NEW="USE_REMOTE_USER_AUTHENTICATION = True" &&\  
  sed -i "s|$REMOTE_USER_AUTH_OLD|$REMOTE_USER_AUTH_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_NAME_OLD="'NAME': '/var/lib/graphite/graphite.db'," &&\
  DB_DEFAULT_NAME_NEW="'NAME': 'graphite'," &&\
  sed -i "s|$DB_DEFAULT_NAME_OLD|$DB_DEFAULT_NAME_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_ENGINE_OLD="'ENGINE': 'django.db.backends.sqlite3'," &&\
  DB_DEFAULT_ENGINE_NEW="'ENGINE': 'django.db.backends.postgresql_psycopg2'," &&\
  sed -i "s|$DB_DEFAULT_ENGINE_OLD|$DB_DEFAULT_ENGINE_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_USER_OLD="'USER': ''," &&\
  DB_DEFAULT_USER_NEW="'USER': 'graphite'," &&\
  sed -i "s|$DB_DEFAULT_USER_OLD|$DB_DEFAULT_USER_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_PASSWORD_OLD="'PASSWORD': ''," &&\
  DB_DEFAULT_PASSWORD_NEW="'PASSWORD': 'password'," &&\
  sed -i "s|$DB_DEFAULT_PASSWORD_OLD|$DB_DEFAULT_PASSWORD_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_HOST_OLD="'HOST': ''," &&\
  DB_DEFAULT_HOST_NEW="'HOST': env('POSTGRES_PORT_5432_TCP_ADDR')," &&\
  sed -i "s|$DB_DEFAULT_HOST_OLD|$DB_DEFAULT_HOST_NEW|" /etc/graphite/local_settings.py

RUN DB_DEFAULT_PORT_OLD="'PORT': ''" &&\
  DB_DEFAULT_PORT_NEW="'PORT': '5432'" &&\
  sed -i "s|$DB_DEFAULT_PORT_OLD|$DB_DEFAULT_PORT_NEW|" /etc/graphite/local_settings.py

RUN DEFAULT_CACHE_DURATION_OLD="#DEFAULT_CACHE_DURATION = 60" &&\
  DEFAULT_CACHE_DURATION_NEW="DEFAULT_CACHE_DURATION = 1" &&\
  sed -i "s|$DEFAULT_CACHE_DURATION_OLD|$DEFAULT_CACHE_DURATION_NEW|" /etc/graphite/local_settings.py


# configure carbon
#
RUN ENABLE_CARBON_CACHE_OLD="CARBON_CACHE_ENABLED=false" &&\
  ENABLE_CARBON_CACHE_NEW="CARBON_CACHE_ENABLED=true" &&\
  sudo sed -i "s|$ENABLE_CARBON_CACHE_OLD|$ENABLE_CARBON_CACHE_NEW|" /etc/default/graphite-carbon

RUN ENABLE_LOG_ROTATION_OLD="ENABLE_LOGROTATION = False" &&\
  ENABLE_LOG_ROTATION_NEW="ENABLE_LOGROTATION = True" &&\
  sudo sed -i "s|$ENABLE_LOG_ROTATION_OLD|$ENABLE_LOG_ROTATION_NEW|" /etc/carbon/carbon.conf

RUN CARBON_METRIC_PREFIX_OLD="# CARBON_METRIC_PREFIX = carbon" &&\
  CARBON_METRIC_PREFIX_NEW="CARBON_METRIC_PREFIX = carbon" &&\
  sed -i "s|$CARBON_METRIC_PREFIX_OLD|$CARBON_METRIC_PREFIX_NEW|" /etc/carbon/carbon.conf

RUN CARBON_METRIC_INTERVAL_OLD="# CARBON_METRIC_INTERVAL = 60" &&\
  CARBON_METRIC_INTERVAL_NEW="CARBON_METRIC_INTERVAL = 1" &&\
  sed -i "s|$CARBON_METRIC_INTERVAL_OLD|$CARBON_METRIC_INTERVAL_NEW|" /etc/carbon/carbon.conf


# configure storage-schemas
#
RUN STORAGE_RETENTION_OLD="retentions = 60s:1d" &&\
  STORAGE_RETENTION_NEW="retentions = 1s:6h,1min:7d,10min:5y" &&\
  sed -i "s|$STORAGE_RETENTION_OLD|$STORAGE_RETENTION_NEW|" /etc/carbon/storage-schemas.conf

RUN STORAGE_RETENTION_CARBON_OLD="retentions = 60:90d" &&\
  STORAGE_RETENTION_CARBON_NEW="retentions = 1s:6h,1min:7d,10min:5y" &&\
  sed -i "s|$STORAGE_RETENTION_CARBON_OLD|$STORAGE_RETENTION_CARBON_NEW|" /etc/carbon/storage-schemas.conf

RUN sudo sed -i '$ a \\n[storm]\npattern = ^storm\.\nretentions = 1s:6h,1min:7d,10min:5y' etc/carbon/storage-schemas.conf


# sync django and db
# 

# CURRENTLY DOESN'T WORK -- MAY REVISIT LATER TO ACHIEVE FULL AUTOMATION

#ADD setup-django-db.expect /usr/local/bin/setup-django-db.expect
#RUN ["/usr/bin/expect", "/usr/local/bin/setup-django-db.expect"]
#RUN sudo graphite-manage syncdb --noinput


# fire up carbon and ensure it gets started when the machine does 
#
RUN sudo service carbon-cache start
#RUN update-rc.d carbon-cache defaults

# update apache conf file to enable access via localhost
#
RUN sudo sed -i '$ a \\nServerName 127.0.0.1:80' /etc/apache2/apache2.conf


# make deploy directory accessible as a volume and set environment vars
#
VOLUME [ "/var/www/html" ]
WORKDIR /var/www/html


ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_PID_FILE /var/run/apache2.pid
ENV APACHE_RUN_DIR /var/run/apache2
ENV APACHE_LOCK_DIR /var/lock/apache2


# create needed directories and replace default site with graphite application
#
RUN mkdir -p $APACHE_RUN_DIR $APACHE_LOCK_DIR $APACHE_LOG_DIR
RUN sudo a2dissite 000-default
RUN sudo cp /usr/share/graphite-web/apache2-graphite.conf /etc/apache2/sites-available/apache2-graphite.conf
RUN sudo a2ensite apache2-graphite


# expose port and set to run on container start
#
EXPOSE 80
WORKDIR /etc/graphite
ENTRYPOINT [ "/usr/sbin/apache2" ]
CMD ["-D", "FOREGROUND"]














