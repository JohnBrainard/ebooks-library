FROM openjdk:17-alpine3.14 as builder

COPY . /build/
WORKDIR /build
RUN ["./gradlew", "installDist"]
RUN find . -name "*.bat"

FROM openjdk:17-alpine3.14
COPY --from=builder /build/api/build/install/api /app/

EXPOSE 8080:8080
WORKDIR /app/bin
ENV LIBRARY_PATH=/library/

CMD ["./api"]
