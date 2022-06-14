
clean:
	./gradlew clean

ebooks-library-api/build/libs/ebooks-library-api-1.0.0.jar:
	./gradlew installDist

image: clean ebooks-library-api/build/libs/ebooks-library-api-1.0.0.jar
	docker build -t ebooks-library:latest ./
	docker tag ebooks-library:latest jfbrainard/ebooks-library:latest

push: image
	docker push jfbrainard/ebooks-library:latest
