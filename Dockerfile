FROM node:24-bookworm-slim AS frontend
WORKDIR /web
COPY ["Geeker-Admin - 副本/package.json", "Geeker-Admin - 副本/package-lock.json", "./"]
RUN npm ci --ignore-scripts
COPY ["Geeker-Admin - 副本/", "./"]
RUN npm run build:pro

FROM maven:3.9-eclipse-temurin-17 AS backend
WORKDIR /build
COPY mySpringboot/pom.xml ./pom.xml
COPY mySpringboot/src ./src
RUN mvn -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre-jammy
RUN apt-get update && apt-get install -y --no-install-recommends nginx gettext-base && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend /build/target/mySpringboot-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=frontend /web/dist /usr/share/nginx/html
COPY deploy/render/nginx.conf.template /app/nginx.conf.template
COPY deploy/render/start.sh /app/start.sh
RUN chmod +x /app/start.sh
ENV SERVER_ADDRESS=127.0.0.1 SERVER_PORT=8081 SPRING_PROFILES_ACTIVE=postgres SEED_DEMO=false JAVA_TOOL_OPTIONS="-Xms64m -Xmx256m -XX:MaxMetaspaceSize=128m" UPLOAD_DIR=/tmp/medicine-uploads
EXPOSE 10000
CMD ["/app/start.sh"]
