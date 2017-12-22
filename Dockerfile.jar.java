FROM java:8
RUN mkdir /usr/src/algoapp
COPY algo.jar /usr/src/algoapp
WORKDIR /usr/src/algoapp
CMD java $JAVA_OPTIONS -jar algo.jar