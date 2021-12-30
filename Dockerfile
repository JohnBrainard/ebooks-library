FROM openjdk:17-alpine3.14 as builder

COPY . /build/
WORKDIR /build

RUN ["./gradlew", "--stacktrace", "ebooks-library-api:installDist"]

FROM openjdk:17-alpine3.14

RUN mkdir /app
COPY --from=builder /build/ebooks-library-api/build/install/ebooks-library-api/ /app/

EXPOSE 8080:8080
WORKDIR /app/bin

CMD ["./ebooks-library-api", "--port", "8000"]
