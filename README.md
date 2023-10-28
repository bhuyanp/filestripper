# Usage



### Supported file types:
doc, docx, pdf

### Build Deploy
    mvn clean install
    docker build -t filestripper .
    docker run -p 9090:8080 filestripper

### Access
http://localhost:9090/file/extract

curl -F filename=test.doc -F file=@src/test/resources/test.doc http://localhost:9090/file/extract   




