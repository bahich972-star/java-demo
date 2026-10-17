# Используем легковесный образ с Java 21
FROM eclipse-temurin:21-jre-alpine

# Создаем рабочую директорию
WORKDIR /app

# Копируем собранный .jar файл внутрь контейнера
# Обратите внимание: имя файла должно совпадать с тем, что собирает Maven
COPY target/java-demo-1.0-SNAPSHOT.jar app.jar

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
