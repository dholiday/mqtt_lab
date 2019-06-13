#!/bin/bash

# set advertised.listeners kafka property
# using '$' as a delimiter to keep us from having to escape a bunch of '/' chars
sed -i 's$#advertised.listeners=PLAINTEXT://your.host.name:9092$advertised.listeners=PLAINTEXT://127.0.0.1:9092$g' /kafka_2.11-2.1.0/config/server.properties

# fire up zookeeper and wait indefinitely until it's up and runing
/kafka_2.11-2.1.0/bin/zookeeper-server-start.sh /kafka_2.11-2.1.0/config/zookeeper.properties &
/wait-for-it.sh localhost:2181 --timeout=0 -- echo "*** ZOOKEEPER IS UP ***"

# this allows us to reuse the same container -- when we shut down a container and start it back up we end up with a
# stale ephemeral node from the previous startup of kafka. default lifespan of an ephemeral node is 2 ticks (default
# tickTime is 2s) when the node's owner-session isn't connected.
echo sleeping for 5s to allow any stale ephemeral nodes from previous runs to age off
sleep 5

# fire up kafka now that zookeeper is up - wait until it's up and running
/kafka_2.11-2.1.0/bin/kafka-server-start.sh /kafka_2.11-2.1.0/config/server.properties &
/wait-for-it.sh localhost:9092 --timeout=0 -- echo "*** KAFKA IS UP ***"