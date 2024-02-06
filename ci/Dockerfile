FROM openjdk:8-jdk-alpine

USER root

RUN \
	apk upgrade && \
	apk add --no-cache \
			bash \
			curl \
			jq \
			unzip

RUN export GRADLE_VER=$(curl -s https://services.gradle.org/versions/current | jq -r '.version') \
 && apk --no-cache --update --virtual .build-deps add curl \
 && apk add --no-cache libstdc++ \
 && curl -sLO https://services.gradle.org/distributions/gradle-${GRADLE_VER}-bin.zip \
 && unzip gradle-${GRADLE_VER}-bin.zip \
 && ln -s gradle-${GRADLE_VER} gradle \
 && rm gradle-${GRADLE_VER}-bin.zip \
 && apk del .build-deps


ENV GRADLE_HOME=/usr/bin/gradle
ENV PATH=$PATH:$GRADLE_HOME/bin
WORKDIR /var/jenkins

ENTRYPOINT [ "" ]