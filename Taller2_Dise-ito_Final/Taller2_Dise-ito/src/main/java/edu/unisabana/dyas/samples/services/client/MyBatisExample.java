package edu.unisabana.dyas.samples.services.client;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.samples.entities.Cliente;

public class MyBatisExample {

    /**
     * Construye la fábrica de sesiones de MyBatis
     */
    public static SqlSessionFactory getSqlSessionFactory() {

        SqlSessionFactory sqlSessionFactory = null;

        try {

            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        return sqlSessionFactory;
    }

    /**
     * Programa principal
     */
    public static void main(String args[]) throws SQLException {

        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();

        try {

            ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);

            System.out.println("====== CONSULTAR TODOS LOS CLIENTES ======");

            List<Cliente> clientes = cm.consultarClientes();

            clientes.forEach(c -> {

                System.out.println(c);

                if (c.getRentados() != null) {
                    c.getRentados().forEach(ir -> System.out.println("\t" + ir));
                }

            });

            System.out.println("\n====== CONSULTAR CLIENTE POR DOCUMENTO ======");

            Cliente cli = cm.consultarCliente(123456789);

            System.out.println(cli);

            sqlss.commit();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            sqlss.close();

        }
    }
}