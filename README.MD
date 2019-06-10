docker build --no-cache -t "rede/ok-retro-backend:0.0.1" .

docker network create ok-retro
docker run -d --name mongodb-ok-retro -p 27017:27017 --net=ok-retro mvertes/alpine-mongo
docker run -d --name backend-ok-retro -p 8080:8080 --net=ok-retro rede/ok-retro-backend:0.0.1

docker network create abu
docker run --name abu-mysql -p 33306:3306 --net=abu -e MYSQL_ROOT_PASSWORD=wtappl@123 -e MYSQL_USER=abuappl -e MYSQL_PASSWORD=abuappl@123 -e MYSQL_DATABASE=dbabu -d mysql
docker run --name abu-microservice -p 8080:8080 --net=abu rede/abu-microservice:0.0.1