## Pull Request: 
https://github.com/CP-Diroky/java-explore-with-me/pull/4

## Project Structure

Проект состоит из двух основных сервисов:

- **main-service** — основной сервис приложения Explore With Me, содержащий бизнес-логику и REST API.
- **stats** — сервис статистики, отвечающий за сохранение и получение информации о просмотрах событий.

```
java-explore-with-me
│
├── main-service
│   ├── src/main/java/ru/practicum
│   │   ├── controllers
│   │   ├── dto
│   │   ├── exceptions
│   │   ├── mappers
│   │   ├── models
│   │   ├── repositories
│   │   ├── services
│   │   └── MainService
│   └── resources
│
└── stats
    ├── stats-client
    ├── stats-dto
    └── stats-server
```

### Main Service

Структура основного сервиса организована по слоям приложения.

| Пакет | Назначение |
|--------|------------|
| `controllers` | REST-контроллеры приложения. Разделены на три группы: `Public` (доступные всем пользователям), `Private` (для авторизованных пользователей) и `Admin` (для администраторов). |
| `dto` | Data Transfer Objects, используемые для обмена данными между клиентом и сервером. |
| `exceptions` | Пользовательские исключения и глобальные обработчики ошибок (`@RestControllerAdvice`). |
| `mappers` | Преобразование между сущностями (`Entity`) и DTO. |
| `models` | JPA-сущности приложения. |
| `repositories` | Интерфейсы Spring Data JPA для работы с базой данных. |
| `services` | Бизнес-логика приложения. |
| `MainService` | Точка входа Spring Boot приложения. |

### Stats Service

Модуль статистики разделён на три независимых компонента:

- **stats-server** — REST API для сохранения и получения статистики просмотров.
- **stats-client** — HTTP-клиент для взаимодействия основного сервиса со статистикой.
- **stats-dto** — общие DTO, используемые обоими сервисами.