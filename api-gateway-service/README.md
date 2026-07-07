# Fleet Guard 360

## 🚀 🛠️ Stack

![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud%20Gateway-6DB33F?logo=spring&logoColor=white)
![GraphQL](https://img.shields.io/badge/GraphQL-E10098?logo=graphql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?logo=JSON%20web%20tokens&logoColor=white)

## 🌐 API Gateway
Este proyecto es el **API Gateway** para la arquitectura de microservicios.  
Funciona como punto de entrada principal para los clientes frontend, gestionando:

- Enrutamiento de peticiones
- Autenticación mediante **JWT**
- Configuración de **CORS**
- Comunicación con los microservicios backend

El gateway centraliza la seguridad, simplifica el acceso desde el frontend y asegura una arquitectura limpia, escalable y mantenible.

👉 La aplicación corre en el puerto **8080** por defecto.

---

## 🛣️ Endpoints Disponibles

El API Gateway enruta solicitudes a dos microservicios principales:

### 1. 🔐 Authentication Service - `/auth/**`
- **Propósito**: Maneja la autenticación de usuarios  
- **Ruta principal**: `POST /auth/login`  
- **Seguridad**: Endpoint abierto (no requiere JWT para login)  
- **Content-Type**: `application/json`

📚 **Repositorio**: [auth-service](https://github.com/CodeFactory-FleetGuard360-EV04/authentication.git)

---

### 2. 🔔 Alerts Service (GraphQL) - `/alerts/graphql/**`
- **Propósito**: Gestión de alertas vía **GraphQL**  
- **Ruta principal**: `POST /alerts/graphql`  
- **Seguridad**: Endpoint protegido (requiere JWT válido)  
- **Content-Type**: `application/json`  
- **Ejemplos**:  
  - `POST /alerts/graphql` → consultas y mutaciones GraphQL  
  - Acceso al **GraphQL Playground** (si está habilitado) en `/alerts/graphql`

📚 **Repositorio**: [alerts-service](https://github.com/CodeFactory-FleetGuard360-EV04/alerts-service.git)

---

## 🔐 Flujo de Autenticación

1. El usuario envía sus credenciales a `POST /auth/login`.
2. El **Authentication Service** responde con un **JWT** válido.
3. El cliente usa este token en la cabecera `Authorization: Bearer <token>`.
4. El **API Gateway** valida el JWT antes de redirigir la petición al microservicio correspondiente.
5. Solo peticiones con un token válido acceden a los servicios protegidos como `/alerts/graphql/**`.

---
