FROM amazoncorretto:17-al2023-headless

ENV IUV_MYSQL_URL=jdbc:mysql://host.docker.internal:3306/iuvmoodle
ENV IUV_MYSQL_USERNAME=eber
ENV IUV_MYSQL_PASSWORD=dsnaT.7q

ENV MAIL_SERVER_HOST=email-smtp.us-west-2.amazonaws.com
ENV MAIL_SERVER_PASSWORD=BATNqFR14X4HL4vLf+DzjlRfgMErv/nMTx/2kRIlHePk
ENV MAIL_SERVER_PORT=587
ENV MAIL_SERVER_USERNAME=AKIA4NXBM5NB6WFS54MX


WORKDIR /app

COPY build/resources/main/application.yml /app/application.yml
COPY build/libs/notificationservice-1.0.jar /app/notificationservice.jar

CMD ["java", "-jar", "/app/notificationservice.jar"]

EXPOSE 8080