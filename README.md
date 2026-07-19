# CodificandoAutos

Sistema de gestión para un **taller mecánico automotriz**, desarrollado como proyecto del curso de Análisis y Diseño de Software (26P, UAM Iztapalapa). Es una aplicación de escritorio (JavaFX + Spring Boot) pensada para que el administrador/mecánico del taller lleve el control de clientes, vehículos, citas, historial de reparaciones, inventario de refacciones y contactos de distribuidores, sin depender de libretas ni hojas sueltas.

El detalle completo del negocio y el alcance del proyecto está en los documentos `Documento de Visión y Alcance.pdf` y `Documento de arquitectura.pdf` en la raíz del repo.

## Tecnologías Utilizadas

- **Spring Boot 3.2.12** - Framework de aplicación
- **Java 17** - Lenguaje de programación
- **JavaFX 21.0.2** - Framework de interfaz gráfica
- **Spring Data JPA** - Persistencia de datos
- **H2 Database** - Base de datos en memoria
- **Maven** - Gestión de dependencias (con wrapper `mvnw` incluido, no hace falta tener Maven instalado)
- **JUnit 5 + Mockito** - Pruebas unitarias y de integración
- **TestFX** - Pruebas de interfaz gráfica
- **JaCoCo** - Cobertura de pruebas

## Arquitectura

El sistema sigue una arquitectura **Rich Client**: es una aplicación de escritorio JavaFX que corre "stand-alone" en una sola PC, sin API REST ni portal web para clientes externos. Pensada para un solo equipo de cómputo del taller, usado por un puñado de usuarios internos (administrador/mecánicos).

Se organiza en 3 capas, con un paquete por caso de uso dentro de `presentacion/`:

```
src/
├── main/
│   ├── java/mx/uam/ayd/proyecto/
│   │   ├── datos/           # Capa de datos (Repositorios de Spring Data JPA)
│   │   ├── negocio/         # Capa de negocio (Servicios y entidades de negocio)
│   │   │   └── modelo/      # Entidades JPA (Cliente, Vehiculo, Cita, Distribuidor, Servicio, PiezaInventario, ...)
│   │   ├── presentacion/    # Capa de presentación (JavaFX), un subpaquete por caso de uso
│   │   │   ├── principal/           # Ventana principal / menú
│   │   │   ├── agregarUsuario/
│   │   │   ├── listarUsuarios/
│   │   │   ├── listarGrupos/
│   │   │   ├── registrarPieza/          # HU-31: registro de piezas al inventario
│   │   │   ├── consultarDistribuidores/ # HU-25: consulta de distribuidores
│   │   │   └── registrarServicio/       # HU-29: historial de servicio de un vehículo
│   │   └── ProyectoApplication.java
│   └── resources/
│       ├── application.yml  # Configuración de la aplicación (BD, JPA, puerto)
│       ├── data.sql         # Datos de prueba, se cargan en cada arranque (ver abajo)
│       └── fxml/            # Vistas JavaFX (una por ventana)
└── test/
    ├── java/                # Pruebas unitarias e integración
    └── resources/
        └── application-test.properties
```

Cada caso de uso sigue el mismo patrón: `Vista*`/`Control*` en `presentacion/`, `Servicio*` (lógica de negocio) en `negocio/`, y un repositorio de Spring Data en `datos/`.

## Requisitos del Sistema

- **Java 17 o superior** (JDK 17+)
- No se necesita tener Maven instalado: el repo trae el wrapper `mvnw` / `mvnw.cmd`

## Instalación y Ejecución

### 1. Verificar Java
```bash
java -version
```

### 2. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd CodificandoAutos
```

### 3. Compilar el proyecto
```bash
./mvnw clean compile
```

### 4. Ejecutar la aplicación
```bash
./mvnw javafx:run
```

Esto abre la ventana principal de la app. (`./mvnw spring-boot:run` también funciona, pero como esta app **es** la interfaz JavaFX, `javafx:run` es la forma normal de levantarla).

## Base de Datos

La aplicación usa **H2 en memoria**, configurada en `application.yml`:

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa` / **Contraseña**: (vacía)
- **Consola H2** (mientras la app está corriendo): http://localhost:8080/h2-console
- `hibernate.ddl-auto: create-drop` → la base se **recrea vacía en cada arranque**, no persiste entre ejecuciones

### Datos de prueba

Para no tener que capturar todo a mano cada vez que se reinicia la app, `src/main/resources/data.sql` precarga automáticamente (vía `spring.jpa.defer-datasource-initialization: true`, para que corra después de que Hibernate cree las tablas):

- 2 Clientes con sus Vehículos (placas de prueba: `ABC-123` y `XYZ-789`)
- 3 Distribuidores
- 1 registro de historial de servicio
- 2 piezas de inventario

Si necesitas más datos o distintos, edita ese archivo directamente.

## Funcionalidades Implementadas

| Historia de usuario | Descripción | Estado |
|---|---|---|
| — | Agregar / Listar Usuarios | ✅ |
| — | Listar Grupos | ✅ |
| HU-25 | Consultar distribuidores (listar, buscar por nombre o tipo de refacción, copiar teléfono) | ✅ |
| HU-29 | Historial de servicio de un vehículo (buscar por placas, ver historial, registrar nueva reparación/mantenimiento) | ✅ |
| HU-31 | Registro de piezas al inventario (crea o incrementa existencias) | ✅ |

El resto de las historias de usuario documentadas en `Documento de arquitectura.pdf` (cotizaciones, pedidos a distribuidores, control de calidad, notificaciones, etc.) todavía no están implementadas — ver la sección **Pendientes / Roadmap** más abajo.

## Pendientes / Roadmap

Basado en `Documento de Visión y Alcance.pdf` (roadmap de 5 entregas) y `Documento de arquitectura.pdf` (53 historias de usuario, HU-03 a HU-53). Esta tabla es un resumen de alto nivel — el detalle línea por línea de cada HU vive en esos PDFs, no se duplica aquí.

| Entrega | Tema | Épicas | Estado |
|---|---|---|---|
| 1.0 | Administración básica de clientes y vehículos | EP-01, EP-02, EP-03, EP-12 | 🟡 Parcial — historial de servicio (HU-29) y registro de piezas (HU-31) listos; **falta el alta/edición de Cliente y Vehículo** (hoy no existe ninguna pantalla para darlos de alta, solo se cargan de prueba vía `data.sql`) |
| 2.0 | Gestión de inventario y cotizaciones | EP-04, EP-05, EP-06 | 🟡 Parcial — consulta de distribuidores (HU-25) lista; falta generar cotizaciones (HU-14), consultar manuales/diagramas técnicos (EP-06), seguimiento de pedidos a distribuidores (HU-30) |
| 3.0 | Planeación y ejecución de reparaciones | EP-07, EP-08 | ⬜ Sin empezar — diagnóstico y detalles de falla (HU-07 a HU-11), asignación de tareas a mecánicos (HU-23), control de calidad (HU-40), entrega del vehículo (HU-42 a HU-45), garantía (HU-46 a HU-53), notificaciones de atraso (HU-32/34) |
| 4.0 | Comunicación y seguimiento de clientes | EP-09, EP-11 | ⬜ Sin empezar — **ojo**: EP-09 pide acceso vía PWA desde tabletas/móviles, lo cual contradice el resto del documento de arquitectura (define el sistema como Rich Client de escritorio "stand-alone", sin portal web). Aclarar con el equipo/profesor antes de implementar. EP-11 no está descrito en el Documento de Visión y Alcance |
| 5.0 | Inteligencia operativa y mejora continua | EP-10 | ⬜ Sin empezar — EP-10 tampoco está descrito en el Documento de Visión y Alcance, falta definir su alcance |

### Otros pendientes puntuales

- **Citas** (`Cita.java`): la entidad ya existe pero sin repositorio, servicio ni pantalla propia (HU-03: registrar cita; HU-04: validar fechas duplicadas; HU-05: notificar si la fecha está ocupada).
- No hay pantalla para dar de alta **Clientes** ni **Vehículos** todavía — imprescindible antes de poder usar HU-25/HU-29 con datos reales en vez de los de `data.sql`.
- El **inventario de piezas** (HU-31) solo cubre el alta/incremento; falta la consulta visual del inventario completo (HU-12/HU-13/HU-28) y el seguimiento de pedidos pendientes a distribuidores (HU-30).

## Modelo de Dominio

Entidades JPA existentes hoy en `negocio/modelo/`:

- **Usuario** / **Grupo** — plantilla original del curso
- **Cliente** — nombre, teléfono, email; posee 1 o más Vehículos
- **Vehiculo** — marca, modelo, placas, año, kilometraje; pertenece a un Cliente
- **Cita** — fecha y hora (aún sin relación a Cliente/Vehiculo ni pantalla propia)
- **Distribuidor** — nombre, teléfono, correo, dirección, tipo de refacción
- **Servicio** — reparación o mantenimiento realizado a un vehículo (fecha, descripción, piezas utilizadas, costo de mano de obra, observaciones)
- **PiezaInventario** — nombre, cantidad, proveedor, costo unitario, fecha de recepción

El modelo de dominio completo (incluye entidades futuras como Cotizacion, Reparacion, Pedido, Refaccion) está documentado en `Documento de arquitectura.pdf`, sección "Modelo de dominio".

## Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar pruebas con cobertura
./mvnw jacoco:report
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Migración de Spring Boot 2.x a 3.x

Este proyecto fue migrado de Spring Boot 2.7.3 a 3.2.12. Cambios principales:

1. **Spring Boot**: 2.7.3 → 3.2.12
2. **Java**: 11 → 17
3. **JavaFX**: 17.0.2 → 21.0.2
4. **JPA**: `javax.persistence` → `jakarta.persistence`
5. Actualización de dependencias de prueba

## Desarrollo

### Agregar una nueva historia de usuario / funcionalidad:

1. Crear (o reutilizar) el modelo en `negocio/modelo/`
2. Crear el repositorio en `datos/` (interfaz que extiende `CrudRepository`)
3. Crear el servicio de negocio en `negocio/` (validaciones y reglas de negocio; si el nombre natural choca con el de la entidad, usar un nombre alterno como `Gestion<Entidad>`, ver `GestionServicio`)
4. Crear la ventana (`Vista*`/`Control*` + `.fxml`) en `presentacion/<casoDeUso>/`
5. Conectar el nuevo caso de uso al menú principal (`ControlPrincipal`, `VentanaPrincipal`, `ventana-principal.fxml`)
6. Agregar pruebas unitarias del servicio en `test/`
7. Correr `./mvnw test` completo antes de dar por terminado el cambio

## Licencia

Este proyecto es parte del curso de Análisis y Diseño de Software.

Contactar a Humberto Cervantes Maceda de la UAM Iztapalapa en hcm@xanum.uam.mx
