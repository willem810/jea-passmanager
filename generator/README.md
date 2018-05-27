# Build
mvn clean package && docker build -t jea.passmanager.generator/passmanager_generator .

# RUN

docker rm -f passmanager_generator || true && docker run -d -p 8080:8080 -p 4848:4848 --name passmanager_generator jea.passmanager.generator/passmanager_generator 