# Запуск приложения

## Запуск интеграционных тестов 
    1) Запустить бд в docker из папки ./db (postgres + pgadmin + liquibase): 
      'docker compose up -d'
    2) Запустить тесты:
      'mvn test'
      
## Запуск приложения для разработки
    1) Создать image для обновления патчей бд
    1) Запустить бд в docker из папки ./db (postgres + pgadmin + liquibase):
       'docker compose up'
    2) Запустить spring приложение через main метод класса
      ru.lappi.users.UsersApplication
    3) Приложение будет принимать запросы по url:
      http://localhost:8082/users-api/users

## Запуск в докере бд с приложением
    1) Собираем проект:
      'mvn clean install'
    2) Создаём docker image из корня проекта (image с 1 war файлом):
      docker build -t users-app:v1 .
    3) Запуск приложения в docker из корня проекта (postgres + pgadmin + liquibase + app):
      'docker compose up -d'
    3) Приложение будет принимать запросы по url:
      http://localhost:8082/users-api/users
