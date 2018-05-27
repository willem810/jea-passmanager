# Build
mvn clean package && docker build -t jea.passmanager.passservice/passmanager-passservice .

# RUN

docker rm -f passmanager-passservice || true && docker run -d -p 8080:8080 -p 4848:4848 --name passmanager-passservice jea.passmanager.passservice/passmanager-passservice 