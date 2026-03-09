package edu.unisabana.dyas.sampleprj.dao.mybatis.mappers;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import edu.unisabana.dyas.samples.entities.Cliente;

public interface ClienteMapper {

    /**
     * Consultar un cliente por documento
     */
    public Cliente consultarCliente(@Param("idcli") int id);

    /**
     * Registrar un nuevo item rentado asociado al cliente
     */
    public void agregarItemRentadoACliente(
            @Param("idcli") int id,
            @Param("iditem") int idit,
            @Param("fechaini") Date fechainicio,
            @Param("fechafin") Date fechafin);

    /**
     * Consultar todos los clientes
     */
    public List<Cliente> consultarClientes();

}