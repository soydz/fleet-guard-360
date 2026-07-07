# Fleet Guard 360

## 🚀 🛠️ Stack

![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?logo=rabbitmq&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-4E8CFF?logo=websocket&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)
![Spring Mail](https://img.shields.io/badge/Spring%20Mail-6DB33F?logo=spring&logoColor=white)


## Notificaciones
📧 Este microservicio de Notificaciones está diseñado para ser reactivo, 
operando al consumir mensajes de una cola de RabbitMQ cuando se genera una nueva alerta.
Una vez que recibe el mensaje de la cola, envía un correo electrónico detallado con la información
de la alerta a las partes interesadas y, simultáneamente, utiliza WebSockets para emitir
notificaciones push instantáneas a los clientes conectados,
asegurando que los usuarios reciban el aviso de la nueva alerta en tiempo real. 

## ✅ Prerrequisitos

Es **necesario** contar con un servidor de **RabbitMQ** en funcionamiento y accesible.

### **Servidor de RabbitMQ**

Este servicio depende de RabbitMQ para la recepción de los eventos de alerta. Asegúrate de que tienes:

1.  **RabbitMQ Server:** Una instancia del servidor de mensajería instalada y ejecutándose (por ejemplo, a través de Docker).

## ⚙️ Variables de Entorno (`.env`)

Este servicio requiere la configuración de las siguientes variables de entorno en un archivo `.env` en la raíz del proyecto para su correcto funcionamiento.

---

## **Variables del Servicio**

| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| **`NOTIFICATION_PORT`** | Puerto en el que se ejecutará el servicio de notificación. | `8080` |

---

## **Configuración de Correo Electrónico**

| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| **`EMAIL_SENDER`** | Dirección de correo electrónico utilizada para enviar notificaciones. | `mi.servicio@ejemplo.com` |
| **`EMAIL_PASSWORD`** | Contraseña de la cuenta de correo electrónico. | `unaContraseñaSegura123` |

---

## **Configuración de RabbitMQ**

Estas variables son cruciales para la conexión con el broker de mensajes **RabbitMQ** y la definición de las colas e intercambios necesarios.

| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| **`RABBITMQ_HOST`** | Nombre del host o dirección IP donde se encuentra el servidor RabbitMQ. | `rabbitmq` |
| **`RABBITMQ_PORT`** | Puerto de conexión de RabbitMQ. | `5672` |
| **`RABBITMQ_USERNAME`** | Nombre de usuario para la autenticación en RabbitMQ. | `user_mq` |
| **`RABBITMQ_PASSWORD`** | Contraseña para la autenticación en RabbitMQ. | `password_mq` |
| **`RABBITMQ_QUEUE_ALERT_CREATED_NAME`** | Nombre de la cola que recibirá los mensajes de alertas creadas. | `alert_created_queue` |
| **`RABBITMQ_EXCHANGE_NAME`** | Nombre del _exchange_ al que se conectará el servicio. | `notifications_exchange` |
| **`RABBITMQ_ALERT_CREATED_ROUTING_KEY`** | Clave de enrutamiento (_routing key_) para los mensajes de alerta creada. | `alert.created` |

---

## 📥 Estructura del Mensaje de Alerta (Cola de RabbitMQ)

El microservicio espera recibir un mensaje con formato **JSON** desde la cola definida (`RABBITMQ_QUEUE_ALERT_CREATED_NAME`). Esta estructura de datos contiene toda la información necesaria para generar tanto el correo electrónico como la notificación push.

| Campo | Tipo de Dato | Descripción | Ejemplo |
| :--- | :--- | :--- | :--- |
| **`toUsers`** | `Array<String>` | Lista de correos electrónicos de los destinatarios de la alerta (e.g., conductores, gerentes). | `["user1@mail.com", "user2@mail.com"]` |
| **`alertType`** | `String` | Tipo o nombre de la alerta generada (clave para el asunto y la notificación). | `"Exceso de Velocidad"` |
| **`responsible`** | `String` | Nombre de la persona o entidad responsable que recibe el aviso. | `"Juan Pérez"` |
| **`generatingUnit`** | `String` | Unidad, vehículo o sensor que originó la alerta. | `"Vehículo - Unidad 73"` |
| **`generationDate`** | `String` | Fecha y hora exacta en la que se generó la alerta (formato ISO 8601). | `"2025-10-19T12:34:56"` |

### **Ejemplo del Mensaje (Cuerpo JSON)**

```json
{
  "toUsers": ["usuario1@ejemplo.com", "usuario2@ejemplo.com", "usuario3@ejemplo.com"],
  "alertType": "Exceso de Velocidad",
  "responsible": "Seguridad Vial",
  "priority": "Alta",
  "driver": "Ana Gómez",
  "generatingUnit": "Vehículo - Unidad 102",
  "state": "Activo",
  "generationDate": "2025-10-24T14:30:00"
}
```


## 💻 Uso del Cliente WebSocket (Prueba con HTML)

Se ha creado un archivo HTML de prueba para demostrar y verificar el correcto funcionamiento de las **notificaciones push** a través de **WebSockets** utilizando el protocolo **STOMP** (a través de **SockJS** para mayor compatibilidad).

---

```html
<!DOCTYPE html>
<html>
<head>
  <title>STOMP WebSocket Test</title>
  <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
  <h2>WebSocket Cliente STOMP</h2>
  <pre id="output"></pre>

  <script>
    // Dirección del servidor de notificaciones
    const socket = new SockJS('http://localhost:8080/ws-alerts');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
      log("Conectado: " + frame);

      // ✅ Suscripción al canal usando en el backend
      stompClient.subscribe('/all/alerts', function (message) {
        const alert = JSON.parse(message.body);
        log("Mensaje recibido:\n" + JSON.stringify(alert, null, 2));
      });
    });

    function log(message) {
      document.getElementById('output').textContent += message + '\n';
    }
  </script>
</body>
</html>
```

### **Cómo funciona:**

1.  **Conexión:** El script establece una conexión con el endpoint `http://localhost:8080/ws-alerts`, será la dirección del microservicio de notificaciones.
2.  **Protocolo:** Utiliza la librería **SockJS** para manejar la conexión y **STOMP.js** como cliente del protocolo STOMP, que facilita el envío y recepción de mensajes.
3.  **Suscripción:** Una vez conectado, el cliente se **suscribe al canal** `/all/alerts`.
4.  **Recepción:** Cada vez que el backend emite una alerta, el mensaje es recibido instantáneamente por este cliente, se parsea el JSON y se muestra en la sección **"output"** de la página.

Este archivo es útil para la **validación rápida** durante el desarrollo y asegura que el canal de **notificaciones push en tiempo real** está operativo.