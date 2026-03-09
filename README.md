# Taller2_Dise-ito_Final

## Parte I – Configuración básica de MyBatis

En esta primera parte del taller se realizó la configuración inicial de MyBatis para permitir el mapeo entre las entidades del sistema y las tablas de la base de datos. El objetivo fue configurar los aliases, crear una consulta SQL con relaciones entre tablas y definir los resultMap necesarios para reconstruir los objetos del sistema a partir de los resultados de la base de datos.

### 1. Configuración de TypeAliases

Para simplificar la escritura de los archivos XML de MyBatis, se definieron alias de tipo en el archivo mybatis-config.xml.

Esto permite utilizar nombres cortos para las entidades en lugar de escribir el nombre completo del paquete cada vez que se utilicen en los mappers.

Código agregado en mybatis-config.xml:

<typeAliases>
    <typeAlias type="edu.unisabana.dyas.samples.entities.Cliente" alias="Cliente"/>
    <typeAlias type="edu.unisabana.dyas.samples.entities.Item" alias="Item"/>
    <typeAlias type="edu.unisabana.dyas.samples.entities.ItemRentado" alias="ItemRentado"/>
    <typeAlias type="edu.unisabana.dyas.samples.entities.TipoItem" alias="TipoItem"/>
</typeAliases>

Gracias a esto, dentro de los archivos XML de los mappers se pueden usar directamente los nombres Cliente, Item, ItemRentado y TipoItem.

---

### 2. Construcción de la consulta SQL

Antes de configurar el mapper, se construyó una consulta SQL que permite recuperar la información completa de los clientes junto con los items que han rentado.

La consulta realiza un LEFT JOIN entre las tablas principales del sistema:

VI_CLIENTES  
VI_ITEMRENTADO  
VI_ITEMS  
VI_TIPOITEM

Consulta utilizada:

SELECT
    c.nombre,
    c.documento,
    c.telefono,
    c.direccion,
    c.email,
    c.vetado,
    ir.id,
    ir.fechainiciorenta,
    ir.fechafinrenta,
    i.id,
    i.nombre,
    i.descripcion,
    ti.descripcion
FROM VI_CLIENTES c
LEFT JOIN VI_ITEMRENTADO ir ON c.documento = ir.CLIENTES_documento
LEFT JOIN VI_ITEMS i ON ir.ITEMS_id = i.id
LEFT JOIN VI_TIPOITEM ti ON i.TIPOITEM_id = ti.id;

Esta consulta permite obtener la información necesaria para reconstruir la relación entre las entidades del sistema.

Cliente → ItemRentado → Item → TipoItem

---

### 3. Configuración del select en ClienteMapper.xml

Una vez probada la consulta SQL, se configuró el método consultarClientes en el archivo ClienteMapper.xml.

Este método utiliza el atributo resultMap para indicar cómo se deben mapear los resultados de la consulta hacia los objetos Java.

Configuración del select:

<select id="consultarClientes" parameterType="map" resultMap="ClienteResult">
    SELECT ...
    FROM VI_CLIENTES c
    LEFT JOIN VI_ITEMRENTADO ir ON c.documento = ir.CLIENTES_documento
    LEFT JOIN VI_ITEMS i ON ir.ITEMS_id = i.id
    LEFT JOIN VI_TIPOITEM ti ON i.TIPOITEM_id = ti.id
</select>

El uso de resultMap permite que MyBatis reconstruya automáticamente la estructura de objetos del sistema.

---

### 4. Definición de ResultMaps

Para mapear correctamente los resultados de la consulta hacia las entidades del sistema, se definieron varios resultMap.

Cada resultMap indica cómo se construye un objeto Java a partir de las columnas de la base de datos.

Los resultMap definidos fueron:

TipoItemResult  
ItemResult  
ItemRentadoResult  
ClienteResult

Ejemplo del resultMap de Item:

<resultMap type="Item" id="ItemResult">
    <id property="id" column="i_id"/>
    <result property="nombre" column="i_nombre"/>
    <association property="tipo" javaType="TipoItem" resultMap="TipoItemResult"/>
</resultMap>

En este caso se utiliza association porque un Item tiene una relación uno a uno con TipoItem.

---

### 5. Uso de colecciones

En el caso de Cliente, se utilizó collection porque un cliente puede tener varios items rentados.

Configuración utilizada:

<resultMap type="Cliente" id="ClienteResult">
    <id property="documento" column="documento"/>
    <result property="nombre" column="nombre"/>
    <collection property="rentados" ofType="ItemRentado" resultMap="ItemRentadoResult"/>
</resultMap>

Esto permite que MyBatis construya automáticamente una lista de ItemRentado dentro del objeto Cliente.

---

### 6. Uso de prefijos de columna

Para evitar conflictos entre columnas con nombres iguales provenientes de diferentes tablas, se utilizaron alias y prefijos de columna.

Por ejemplo:

ir_ → columnas relacionadas con ItemRentado  
i_ → columnas relacionadas con Item  
ti_ → columnas relacionadas con TipoItem

Esto permite que los resultMap identifiquen correctamente qué columnas pertenecen a cada entidad.

---

### 7. Prueba de la configuración

Finalmente se realizó una prueba ejecutando el método consultarClientes desde una clase de prueba.

Código utilizado:

SqlSessionFactory sessionfact = getSqlSessionFactory();
SqlSession sqlss = sessionfact.openSession();

ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);

System.out.println(cm.consultarClientes());

Esto permite verificar que MyBatis recupera correctamente los clientes junto con los items que han rentado.

---

### Resultado

Después de completar esta configuración, MyBatis es capaz de reconstruir automáticamente la estructura de objetos del sistema a partir de una sola consulta SQL.

La estructura obtenida es:

Cliente  
└── ItemRentado  
  └── Item  
    └── TipoItem

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
