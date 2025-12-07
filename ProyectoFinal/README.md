# Sistema de Simulación de Buses - Cartago

**Proyecto Final - Programación Paralela y Distribuida**
Universidad Latinoamericana de Ciencia y Tecnología (ULACIT)
Diciembre 2025

---

## 📋 Descripción del Proyecto

Sistema de simulación que modela el transporte público de buses entre San José y Cartago, utilizando programación concurrente y arquitectura cliente-servidor TCP.

### Características Principales

✅ **Hilos (Threads)**
- 10 buses ejecutándose concurrentemente
- Cada bus es un hilo independiente (`Thread`)
- Movimiento autónomo entre 20 paradas

✅ **Sincronización**
- Métodos `synchronized` para control de acceso a paradas
- Prevención de condiciones de carrera
- Uso de `join()` para coordinación de hilos

✅ **Arquitectura Cliente-Servidor TCP**
- Servidor TCP multi-cliente en puerto 45000
- Clientes pueden consultar estado en tiempo real
- Comunicación confiable con protocolo TCP

✅ **Interfaz Gráfica**
- Visualización de mapa con 20 paradas
- Panel de información en tiempo real
- Iconos personalizados para cada bus

✅ **Colecciones y POO**
- `ArrayList<Autobus>` para gestión de buses
- `ArrayList<Parada>` para gestión de paradas
- Enumeraciones: `EstadoBus`, `DireccionRuta`

---

## 🚀 Instrucciones de Uso

### 1. Ejecutar el Simulador (Interfaz Gráfica)

```bash
# Ejecutar la clase principal
java com.mycompany.proyectofinal.ProyectoFinal
```

**Pasos**:
1. Se abre la ventana del simulador
2. Hacer clic en "Iniciar Simulación"
3. Los buses comienzan a moverse automáticamente
4. El servidor TCP se inicia en puerto 45000

### 2. Ejecutar un Cliente de Monitoreo

```bash
# En una nueva terminal/consola
java com.mycompany.proyectofinal.cliente.ClienteMonitoreo
```

**Comandos disponibles**:
- `ESTADO` - Ver estado de todos los buses
- `BUS <id>` - Ver información de un bus específico (id: 1-10)
- `AYUDA` - Mostrar ayuda
- `SALIR` - Desconectar del servidor

**Ejemplo de uso**:
```
>>> ESTADO
========== ESTADO GENERAL DEL SISTEMA ==========
Total de buses en servicio: 10

  Bus #1: Bus Amarillo
    Estado: EN_TRANSITO
    Parada actual: Terminal Lumaca San José
    Dirección: IDA
    Posición: (124, 135)
...

>>> BUS 1
========== INFORMACIÓN DEL BUS #1 ==========
  Nombre: Bus Amarillo
  Estado: EN_PARADA
  Parada actual: Frente a Parque Montealegre
  Dirección: IDA
  ...
```

### 3. Conectar Múltiples Clientes

El servidor soporta **múltiples clientes simultáneos**. Puedes abrir varias consolas y ejecutar el cliente varias veces:

```bash
# Terminal 1
java com.mycompany.proyectofinal.cliente.ClienteMonitoreo

# Terminal 2
java com.mycompany.proyectofinal.cliente.ClienteMonitoreo

# Terminal 3
java com.mycompany.proyectofinal.cliente.ClienteMonitoreo
```

Cada cliente se ejecuta en un hilo separado en el servidor.

---

## 🏗️ Arquitectura del Sistema

### Componentes Principales

#### 1. **Simulador (Paquete: `simulador`)**
- `ProyectoFinal.java` - Clase principal (main)
- `SimuladorControl.java` - Controlador central, gestiona hilos y servidor
- `Autobus.java` - Hilo que representa un bus
- `Ruta.java` - Contiene las 20 paradas
- `Parada.java` - Representa una parada con sincronización
- `MapaPanel.java` - Panel gráfico del mapa
- `InfoPanel.java` - Panel de información
- `EstadoBus.java` - Enum: EN_PARADA, EN_TRANSITO, ESPERANDO
- `DireccionRuta.java` - Enum: IDA, VUELTA

#### 2. **Servidor (Paquete: `servidor`)**
- `ServidorMonitoreo.java` - Servidor TCP principal (Thread)
- `HiloCliente.java` - Hilo para atender a cada cliente

#### 3. **Cliente (Paquete: `cliente`)**
- `ClienteMonitoreo.java` - Cliente TCP para consultar estado

### Flujo de Ejecución

```
1. Usuario inicia el simulador (ProyectoFinal.main())
   ↓
2. SimuladorControl crea 10 objetos Autobus
   ↓
3. Usuario presiona "Iniciar Simulación"
   ↓
4. Se inician:
   - 10 hilos de buses (Autobus.start())
   - 1 hilo del servidor TCP (ServidorMonitoreo.start())
   ↓
5. Servidor espera conexiones en puerto 45000
   ↓
6. Cliente se conecta → Servidor crea HiloCliente
   ↓
7. Cliente envía comandos → HiloCliente responde
```

---

## 🔧 Tecnologías Utilizadas

### Conceptos de Programación Paralela

| Concepto | Implementación | Ubicación |
|----------|---------------|-----------|
| **Hilos** | `extends Thread`, `implements Runnable` | `Autobus.java:10`, `ServidorMonitoreo.java:19` |
| **Sincronización** | `synchronized` methods | `Parada.java:51-76` |
| **Join** | `thread.join()` | `SimuladorControl.java:115-120` |
| **Colecciones** | `ArrayList`, `Collections` | `SimuladorControl.java:30`, `Ruta.java:14` |
| **TCP Sockets** | `ServerSocket`, `Socket` | `ServidorMonitoreo.java:50`, `ClienteMonitoreo.java:53` |
| **Streams TCP** | `InputStream`, `OutputStream` | `HiloCliente.java:47-48` |
| **Enumeraciones** | `enum` | `EstadoBus.java`, `DireccionRuta.java` |
| **POO** | Herencia, Encapsulación | Todo el proyecto |

---

## 📊 Cumplimiento del Sílabo

### ✅ Requisitos Implementados

1. **Hilos y Concurrencia** ✔️
   - 10 buses ejecutándose concurrentemente
   - Servidor TCP con hilos para cada cliente

2. **Sincronización** ✔️
   - Métodos `synchronized` en Parada
   - Uso de `join()` para esperar hilos

3. **Redes y Protocolos** ✔️
   - Servidor TCP multi-cliente
   - Puerto 45000
   - Comunicación cliente-servidor confiable

4. **Colecciones Genéricas** ✔️
   - `ArrayList<Autobus>`
   - `ArrayList<Parada>`

5. **POO** ✔️
   - Clases bien estructuradas
   - Encapsulación
   - Enumeraciones

6. **Solución del Mundo Real** ✔️
   - Simula transporte público real (San José - Cartago)
   - 20 paradas reales
   - Monitoreo remoto

---

## 🎯 Conceptos de Clase Utilizados

### Week 5: Hilos
- ✅ Clase `Thread`
- ✅ Método `run()`
- ✅ Método `start()`
- ✅ Método `isAlive()`

### Week 6: Sincronización
- ✅ Palabra clave `synchronized`
- ✅ Método `join()`
- ✅ Control de acceso concurrente

### Week 8: UDP
- ⚠️ No utilizado (se prefirió TCP)

### Week 9: TCP
- ✅ `ServerSocket`
- ✅ `Socket`
- ✅ `InputStream` / `OutputStream`
- ✅ Servidor multi-cliente

### Week 3: Colecciones
- ✅ `ArrayList`
- ✅ `Collections.unmodifiableList()`

### Week 11: Enumeraciones
- ✅ `enum EstadoBus`
- ✅ `enum DireccionRuta`

---

## 📝 Documentación del Código

Todo el código está completamente documentado con:
- Comentarios JavaDoc en clases y métodos
- Explicaciones de algoritmos
- Descripción de parámetros y retornos
- Ejemplos de uso

---

## 🧪 Pruebas Sugeridas

### Test 1: Concurrencia de Buses
1. Iniciar simulación
2. Observar que los 10 buses se mueven independientemente
3. Verificar que no hay colisiones en paradas

### Test 2: Sincronización
1. Observar que solo un bus ocupa una parada a la vez
2. Los buses esperan si la parada está ocupada

### Test 3: Servidor Multi-Cliente
1. Iniciar simulación
2. Conectar 3 clientes simultáneos
3. Enviar comandos desde cada cliente
4. Verificar que todos reciben respuestas correctas

### Test 4: Join y Detención
1. Iniciar simulación
2. Presionar "Detener Simulación"
3. Verificar que todos los hilos terminan correctamente

---

## 👥 Autores

- Proyecto Final - Programación Paralela y Distribuida
- ULACIT - 3CO2025

---

## 📞 Soporte

Para dudas o problemas:
1. Verificar que el puerto 45000 esté libre
2. Verificar que la simulación esté iniciada antes de conectar clientes
3. Revisar la consola para mensajes de error

---

**¡Sistema listo para su presentación final!** 🚀
