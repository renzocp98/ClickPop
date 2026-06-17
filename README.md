# ClickPop — Backend

API REST + WebSocket en tiempo real para un juego de reflejos. El jugador debe acertar 15 puntos aleatorios en un canvas de 300×300px. Cada acierto suma 1 punto; cada fallo resta 2. La partida termina cuando se eliminan todos los puntos.

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 3.5.5 |
| Persistencia | Spring Data JPA + Hibernate + MySQL |
| Tiempo real | WebSocket (STOMP / SockJS) |
| Autenticación | HttpSession (Jakarta Servlet) |
| Validación | Jakarta Bean Validation (`@NotBlank`, `@Size`) |
| Tests | JUnit 5 + Mockito + H2 (in-memory) |
| Build | Maven |

---

## Arquitectura

El proyecto sigue una arquitectura en capas estándar de Spring Boot:

```
Controller  →  Service (interfaz)  →  ServiceImpl  →  Repository  →  MySQL
                                                                   →  H2 (tests)
```

### Dos capas de estado

Durante una partida, el backend mantiene **dos tipos de estado** separados:

| Estado | Dónde vive | Cuándo se usa |
|---|---|---|
| Puntos activos y score en curso | `ConcurrentHashMap` en memoria | Durante el juego (click a click) |
| Score final | Base de datos MySQL | Solo al terminar la partida |

Esto evita escribir a la base de datos en cada click, manteniendo la latencia baja durante el juego.

### Problema de singleton resuelto

Los `@Service` de Spring son **singleton**: existe una sola instancia para toda la aplicación. Si el score y los puntos se guardaban como atributos de instancia (`private int score`, `private List<int[]> points`), todas las partidas concurrentes compartían el mismo estado.

**Solución:** reemplazar los atributos de instancia por `ConcurrentHashMap` indexados por `gameId`:

```java
private final Map<Integer, Integer>     scoreByGame  = new ConcurrentHashMap<>();
private final Map<Integer, List<int[]>> pointsByGame = new ConcurrentHashMap<>();
```

`ConcurrentHashMap` en vez de `HashMap` porque múltiples jugadores acceden desde distintos threads en paralelo.

---

## Flujo de una partida

```
1. POST /users/register          → el usuario se registra (validación de campos)
2. POST /SessionInfo/login       → autenticación por sesión HTTP
3. POST /game/create             → crea la partida en BD, genera 15 puntos aleatorios
                                   y los envía por WebSocket a /backsend/points
4. WS  /click/registerClick      → el frontend manda { gameId, x, y } por cada click
5. WS  /backsend/score           → el backend responde { valid, points, finished }
6. PUT /game/score/{score}       → cuando finished=true, el frontend persiste el score final
```

---

## API

### REST

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/users/register` | Registrar usuario. Valida `username` y `password`. |
| `GET` | `/users` | Listar todos los usuarios. |
| `GET` | `/users/username/{username}` | Buscar usuario por nombre. |
| `POST` | `/SessionInfo/login` | Login. Crea sesión HTTP. |
| `POST` | `/SessionInfo/logout` | Logout. Invalida la sesión. |
| `POST` | `/game/create` | Crear partida para un usuario. |
| `PUT` | `/game/score/{score}` | Persistir score final al terminar la partida. |

### WebSocket (STOMP)

**Endpoint de conexión:** `/game-WS` (con fallback SockJS)

| Canal | Dirección | Payload | Descripción |
|---|---|---|---|
| `/click/registerClick` | Frontend → Backend | `{ gameId, x, y }` | Registrar un click |
| `/backsend/points` | Backend → Frontend | `{ points: [[x,y],...] }` | Puntos iniciales al crear partida |
| `/backsend/score` | Backend → Frontend | `{ valid, points, finished }` | Resultado de cada click |

---

## Tests

El proyecto tiene tres niveles de tests:

### Unitarios — capa de servicio

Sin contexto de Spring. Mockito reemplaza los repositorios con objetos falsos para aislar la lógica de negocio.

```
UserServiceTests              → save, findByUsername, delete, update, findAll
GameWebSocketServiceImplTests → RandomPoints, isOutOfLimit, compareGamePoint,
                                controlScore, isGameFinished, AddScore, createGame
```

### Integración — capa de repositorio

`@DataJpaTest` levanta solo la capa JPA con H2 en memoria. Verifica que las queries funcionan correctamente contra una base de datos real.

```
UserRepositoryTests   → save, findById, findAll, findByUsername
GameRepositoryTests   → save, findById, findAll
```

### Integración — capa de controlador

`@SpringBootTest` + `MockMvc` levanta el contexto completo de Spring. Los servicios se reemplazan con mocks (`@MockitoBean`) para no depender de la base de datos.

```
UserControllerTests    → registro (válido, username vacío, password corta), list, getUser
GameControllerTests    → createGame (ok / usuario no encontrado), addScore (ok / 404)
SessionControllerTests → login (ok / password incorrecta / usuario no encontrado), logout
```

### Ejecutar tests

```bash
mvn test
```

Los tests de repositorio y controlador usan el perfil `test` con H2. No requieren MySQL.

---

## Instalación y ejecución

### Requisitos

- Java 21
- Maven 3.8+
- MySQL 8+

### Configuración

Crear la base de datos en MySQL:

```sql
CREATE DATABASE clickgame;
```

Configurar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clickgame
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
```

Insertar el rol base (requerido para el registro de usuarios):

```sql
INSERT INTO role (name) VALUES ('USER');
```

### Ejecutar

```bash
mvn spring-boot:run
```

El servidor arranca en `http://localhost:8080`.

---

## Limitaciones conocidas

- Las contraseñas se almacenan en texto plano. La mejora pendiente es integrar **Spring Security con BCrypt**.
- La autenticación por `HttpSession` no es stateless. Para una API consumida por múltiples clientes se debería migrar a **JWT**.
- El endpoint `rankUsers()` en `UserRepository` tiene una query JPQL incorrecta (pendiente de corregir).