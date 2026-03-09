# Taller2_Dise-ito_Final



## Parte II – Configuración de operaciones en MyBatis

En esta parte del taller se configuraron diferentes operaciones del sistema de persistencia utilizando **MyBatis**. El objetivo fue mapear métodos de los *mappers* de Java con sentencias SQL definidas en archivos XML.

### 1. Consulta de cliente por ID

Se configuró el método `consultarCliente(int id)` en la interfaz `ClienteMapper`.
Para poder usar el parámetro dentro de la sentencia SQL, se utilizó la anotación `@Param`, asignándole el nombre `idcli`.

```java
public Cliente consultarCliente(@Param("idcli") int id);
```

En el archivo `ClienteMapper.xml` se creó el elemento `<select>` correspondiente, utilizando `parameterType="map"` y el parámetro `#{idcli}` dentro de la cláusula `WHERE`.

Esto permite consultar un cliente específico junto con sus items rentados.

---

### 2. Registrar un item rentado a un cliente

Se configuró el método:

```java
agregarItemRentadoACliente(int idcli, int iditem, Date fechaini, Date fechafin)
```

Este método permite registrar una nueva renta de un item para un cliente.
En el archivo `ClienteMapper.xml` se definió una sentencia `<insert>` que guarda los datos en la tabla `VI_ITEMRENTADO`.

De esta forma el sistema puede almacenar la relación entre el cliente, el item rentado y las fechas de inicio y fin de la renta.

---

### 3. Inserción de nuevos items

Se creó el `ItemMapper` con el método:

```java
insertarItem(@Param("item") Item it)
```

Este método recibe un objeto `Item` y permite registrar un nuevo item en la base de datos.
En el archivo `ItemMapper.xml` se definió la sentencia `<insert>`, utilizando los atributos del objeto `Item` dentro de la consulta SQL, por ejemplo:

```
#{item.id}
#{item.nombre}
#{item.descripcion}
```

---

### 4. Consultas de items

También se configuraron las operaciones:

* `consultarItem(int id)` → consulta un item específico.
* `consultarItems()` → retorna la lista completa de items.

Estas operaciones se implementaron mediante sentencias `<select>` en `ItemMapper.xml`, utilizando el `resultMap` correspondiente para reconstruir los objetos `Item` y su relación con `TipoItem`.

---

### Resultado

Con estas configuraciones se logró:

* Consultar clientes junto con sus items rentados.
* Registrar nuevas rentas de items.
* Insertar nuevos items en la base de datos.
* Consultar uno o varios items mediante MyBatis.

Las pruebas se realizaron utilizando la clase `MyBatisExample`, verificando que los datos se recuperan correctamente desde la base de datos SQLite.
