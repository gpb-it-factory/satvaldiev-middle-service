# Middle service
Middle service является вторым компонентом проекта "Мини-банк". Сервис взаимодействует с остальными частями проекта: Frontend компонентом (Telegram-бот) и Backend компонентом (АБС).
В функции сервиса входят принятие и валидация запросов от бота, выполнение бизнес-логики и маршрутизация запросов в АБС.

```plantuml
@startuml
skinparam backgroundColor #EEEBDC

skinparam sequence {
ArrowColor DeepSkyBlue
ArrowFontSize 18
ArrowFontColor darkviolet
ArrowFontName Impact
LifeLineBorderColor blue

ParticipantBorderColor DeepSkyBlue
ParticipantBackgroundColor DodgerBlue
ParticipantFontName Impact
ParticipantFontSize 25
ParticipantFontColor #EEEBDC
}
participant Client
participant Frontend 
participant Middle 
participant Backend 


Client -> Frontend: HTTP
Frontend -> Middle: HTTP
Middle -> Backend: HTTP
Backend --> Middle: HTTP
Middle --> Frontend: HTTP
Frontend --> Client: HTTP
@enduml
```

![Image](image.png)

### Локальный запуск
Раздел в разработке