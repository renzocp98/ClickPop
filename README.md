### ClickPop

¡Bienvenido a ClickPop! En este juego, tu objetivo es poner a prueba tu rapidez y reflejos. Regístrate como usuario, ingresa al tablero y comienza a hacer desaparecer los puntos.
Cada clic cuenta, pero ¡el tiempo es limitado! Compite por el mejor puntaje antes de que se agote el tiempo. ClickPop está en desarrollo y pronto estará disponible

### **Objetivo del juego**

ClickPop es un juego desafiante en el que los jugadores deben hacer desaparecer puntos que aparecen al azar en un tablero cuadrado.
El objetivo es eliminar la mayor cantidad de puntos posible antes de que se agote el tiempo, todo mientras se ponen a prueba los reflejos y la rapidez de los jugadores.
Los usuarios se registran, y una vez que comienzan, deben hacer clic rápidamente en los puntos que surgen, sin dejar que el tiempo se acabe o exceder un límite de clics.
¡Cada segundo cuenta y el reto es mantener el ritmo!

### **Tecnologías Utilizadas**

**Backend:**

- **Java con Spring Boot**: Para el backend de **ClickPop**, hemos utilizado **Java** junto con **Spring Boot**. Este framework nos permite crear aplicaciones  con una arquitectura sencilla y flexible.
- **Hibernate (JPA)**: Para manejar la base de datos, usamos **Hibernate** junto con **JPA** (Java Persistence API). Esto nos permite gestionar las entidades y las relaciones entre ellas sin necesidad de escribir SQL manualmente, haciendo que las operaciones CRUD sean mucho más sencillas y eficientes.
- **WebSocket**: **WebSocket** se utiliza para habilitar la comunicación en tiempo real entre el servidor y el cliente. Esto es idealya que nos permite actualizar el estado del juego instantáneamente y mantener una interacción constante con el usuario sin necesidad de recargar la página. WebSocket permite una comunicación bidireccional.

**Base de Datos:**

- **MySQL**: La base de datos está gestionada con **MySQL**, un sistema de gestión de bases de datos relacional. Usamos MySQL para almacenar toda la información de los usuarios, sus puntuaciones, y los registros de las partidas.

**Frontend:**

- **React**: En el frontend, utilizamos **React** para crear una interfaz de usuario. React permite que la aplicación sea rápida y eficiente, actualizando solo los elementos necesarios en la pantalla sin recargar toda la página. Diseño muy simple ya que no es el objetivo de este proyecto ni se dispone de conocimiento suficiente para desarrollar una interfaz elaborada.

**Tecnologías Adicionales:**

- **Maven**: Para gestionar las dependencias y el ciclo de vida del proyecto en el backend, utilizamos **Maven**. Esta herramienta facilita la automatización de la construcción, asegurando que todas las dependencias estén correctamente configuradas y simplificando el proceso de despliegue y empaquetado.
- **Visual Studio Code**: Para el desarrollo del frontend y backend, utilizamos **Visual Studio Code**. Con su amplia gama de extensiones, VS Code proporciona soporte para trabajar de manera eficiente con tecnologías como Java, React, y SQL, además de permitir una edición de código ágil y flexible.

### **Características del Juego**
**Registro e Inicio de Sesión**

- Habrá un usuario administrador (admin) predefinido en la base de datos.

- Cualquier persona podrá registrarse como usuario estándar, proporcionando sus datos correspondientes.
- Los usuarios podrán iniciar sesión para acceder a las funcionalidades del juego.

**Inicio de Partida y Registro de Datos**

- Una vez autenticado, el usuario podrá iniciar una partida.

- Durante la partida, se registrarán en la base de datos,el número de puntos en la partida

**Sistema de Clasificación y Tabla de Resultados**

- El juego contará con una tabla de clasificación dinámica donde los jugadores podrán visualizar los puntajes y estadísticas.

- La tabla permitirá filtrar y ordenar los resultados según distintos criterios, como:

- Mejores puntuaciones.

- Últimas partidas jugadas.

- Rankings por usuario.

**Gestión de Usuarios (Solo para Admin)**

- Si el usuario autenticado es el administrador, tendrá acceso a herramientas adicionales:

- Podrá visualizar un listado de usuarios.

- Tendrá la opción de eliminar usuarios desde la tabla de clasificación.

**Zona de Juego**

- En la pantalla del juego habrá un tablero interactivo, donde aparecerán puntos de manera aleatoria.

- El objetivo es hacer clic en la mayor cantidad de puntos posible antes de que termine el tiempo.

- La puntuación se calculará según la cantidad de puntos eliminados dentro del límite de tiempo y clicks fallidos.

### **Futuras Mejoras**

A medida que ClickPop evolucione, se planean nuevas funcionalidades y optimizaciones para mejorar la seguridad, la escalabilidad y la facilidad de despliegue del juego:

- Implementación de Spring Security: Se integrará Spring Security para mejorar la autenticación y autorización de los usuarios, asegurando un acceso más seguro y permitiendo una gestión de roles más eficiente.

- Uso de Docker para contenerización: Se trabajará en la creación de un contenedor Docker para ejecutar el juego de manera más flexible en distintos entornos, como una máquina virtual. Esto facilitará la portabilidad y el despliegue del proyecto sin preocuparse por diferencias en la configuración del sistema.

