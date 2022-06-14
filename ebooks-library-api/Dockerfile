FROM openjdk:17-alpine3.14
EXPOSE "8000:8000"
RUN mkdir /app
COPY ./build/install/ebooks-library-api /app/
WORKDIR /app/bin
CMD ["./ebooks-library-api", "--port", "8000"]
