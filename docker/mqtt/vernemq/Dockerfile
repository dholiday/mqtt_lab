# for standing up an instance of vernemq that amtches what we're using in dev/prod
#
FROM vernemq/vernemq:1.7.1-2
MAINTAINER David Holiday <dholiday@life360.com>

# so long as the containers are linked this env variable should be set and should resolve to the IP of the graphite
# container
# *DARF* - vernemq isn't up at this point so the command will fail. ideally this will be automated somewhere but for now
# we'll do it manually (probably SED time ...)
#RUN vmq-admin set graphite_host=$GRAPHITE_PORT_80_TCP_ADDR graphite_enabled=on -n VerneMQ@127.0.0.1