# Build
mvn clean package && docker build -t jea.passmanager.authservice/passmanager-authservice .

# RUN

docker rm -f passmanager-authservice || true && docker run -d -p 8080:8080 -p 4848:4848 --name passmanager-authservice jea.passmanager.authservice/passmanager-authservice 