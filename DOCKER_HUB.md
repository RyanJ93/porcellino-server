# Porcellino

Manage your personal finance with Porcellino: this simple web-app available online allows to easily track your incomes and expenses, you can also organize your finance distributing transactions across multiple portfolio supporting different currencies.

## How to use this image

### Running as a single container

To run your own instance of the Porcellino server, use the following command:

````bash
docker run -p 8080:8080 -d --name porcellino-server enricosola/porcellino-server:latest
````

Here you can find all the supported environment variables:

- `SPRING_DATASOURCE_URL`: The connection url form the MySQL database to use.
- `SPRING_DATASOURCE_USERNAME`: The MySQL database authentication username.
- `SPRING_DATASOURCE_PASSWORD`: The MySQL database authentication password.
- `JWT_SECRET`: A 256 bit long secret key used to sign JWTs.
- `JWT_EXPIRATION_MS`: JWTs life spawn.

Note that to have a fully working application, the Porcellino client is required, more information about the Porcellino client's Docker image [here](https://hub.docker.com/r/enricosola/porcellino-client).

### Running via docker compose

Here you can find an example to run this application alongside a MySQL database instance using docker compose:

````yaml
version: '3.8'
services:
    server:
        image: 'enricosola/porcellino-server:latest'
        container_name: 'porcellino-server'
        hostname: 'porcellino-server'
        restart: 'always'
        environment:
            - SPRING_DATASOURCE_URL=jdbc:mysql://porcellino-mysql:3306/porcellino?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
            - SPRING_DATASOURCE_USERNAME=porcellino
            - SPRING_DATASOURCE_PASSWORD=porcellino
            - JWT_SECRET=secret_key
        ports:
            - '8081:8080'
        networks:
            - porcellino-network

    client:
        image: 'enricosola/porcellino-client:latest'
        container_name: 'porcellino-client'
        hostname: 'porcellino-client'
        restart: 'always'
        ports:
            - '8080:8080'
        networks:
            - porcellino-network

    mysql:
        image: 'mysql:8.0'
        container_name: 'porcellino-mysql'
        hostname: 'porcellino-mysql'
        restart: always
        ports:
            - '3306:3306'
        volumes:
            - ./data/db:/var/lib/mysql
            - ./logs/mysql:/var/log/mysql
        environment:
            MYSQL_DATABASE: porcellino
            MYSQL_ROOT_PASSWORD: root
            MYSQL_USER: porcellino
            MYSQL_PASSWORD: porcellino
            MYSQL_ROOT_HOST: 0.0.0.0
        security_opt:
            - seccomp:unconfined
        networks:
            - porcellino-network

networks:
    porcellino-network:
        driver: bridge
````

## License

This work is licensed under a
[Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa].

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

Developed with ❤️ by [Enrico Sola](https://www.enricosola.dev).
