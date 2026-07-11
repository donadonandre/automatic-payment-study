FROM debezium/connect:3.0

ENV MONGO_CONNECTOR_VERSION=1.13.0

USER root
RUN curl -sSL "https://repo1.maven.org/maven2/org/mongodb/kafka/mongo-kafka-connect/${MONGO_CONNECTOR_VERSION}/mongo-kafka-connect-${MONGO_CONNECTOR_VERSION}-all.jar" \
    -o "/kafka/connect/mongo-kafka-connect-${MONGO_CONNECTOR_VERSION}-all.jar"
USER kafka
