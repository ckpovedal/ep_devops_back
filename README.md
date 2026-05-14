Backend REST de Pacientes para la EP02. El servicio esta construido con Spring Boot 3.5, Java 21, Gradle y MySQL.

## Arquitectura

```text
Frontend EC2 publica
  -> Backend EC2 privada: puerto 8080
    -> MySQL EC2 privada: puerto 3306
```

## Endpoints

| Metodo | Ruta | Uso |
| ------ | ---- | --- |
| GET | `/api/pacientes` | Listar pacientes |
| GET | `/api/pacientes/{id}` | Buscar paciente |
| POST | `/api/pacientes` | Crear paciente |
| PUT | `/api/pacientes/{id}` | Actualizar paciente |
| DELETE | `/api/pacientes/{id}` | Eliminar paciente |
| GET | `/actuator/health` | Healthcheck |

## Variables de entorno

| Variable | Descripcion |
| -------- | ----------- |
| `SPRING_DATASOURCE_URL` | URL JDBC de MySQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de MySQL |
| `SPRING_DATASOURCE_PASSWORD` | Contrasena de MySQL |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Modo de esquema JPA |
| `SERVER_PORT` | Puerto del backend |

Puedes copiar `.env.example` a `.env` para desarrollo local.

## Docker

Construir imagen:

```bash
docker build -t backend:v1.0.0 .
```

Ejecutar solo la imagen, usando una base de datos existente:

```bash
docker run -p 8080:8080 --env-file .env backend:v1.0.0
```

Levantar solo el backend con Docker Compose:

```bash
docker compose up -d --build
```

Verificar:

```bash
docker compose ps
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/pacientes
```

## Base de datos

La base de datos MySQL se levanta directamente en la EC2 Database con su propio `docker-compose.yml`.

Este repositorio no contiene la base de datos ni administra su persistencia. El backend solo se conecta a MySQL usando `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`.

## ECR

Ejemplo de publicacion manual:

```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 058264156030.dkr.ecr.us-east-1.amazonaws.com
docker tag backend:v1.0.0 058264156030.dkr.ecr.us-east-1.amazonaws.com/eva2-backend:latest
docker push 058264156030.dkr.ecr.us-east-1.amazonaws.com/eva2-backend:latest
```

## GitHub Actions

El workflow `.github/workflows/cicd-backend.yml` corre al hacer push a la rama `deploy`.

Secrets necesarios:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_REGION
ECR_REGISTRY
ECR_REPOSITORY_BACKEND
EC2_BACKEND_HOST
EC2_BACKEND_USER
EC2_BACKEND_SSH_KEY
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

Valores usados en esta entrega:

```text
AWS_REGION=us-east-1
ECR_REGISTRY=058264156030.dkr.ecr.us-east-1.amazonaws.com
ECR_REPOSITORY_BACKEND=eva2-backend
```

## Despliegue EC2

La instancia backend debe tener Docker instalado, acceso a ECR y reglas de Security Group que permitan el puerto 8080 solo desde la instancia frontend.

La base de datos debe permitir el puerto 3306 solo desde la instancia backend.

## Troubleshooting

Ver logs:

```bash
docker logs -f pacientes-app
```

Ver contenedores:

```bash
docker ps
```

Probar conexion al healthcheck:

```bash
curl http://localhost:8080/actuator/health
```
