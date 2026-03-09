package edu.unisabana.dyas.samples.services.client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.samples.entities.Cliente;

public class ClienteGUI extends JFrame {

    private JTextField campoId;
    private JTextArea areaResultados;

    public ClienteGUI() {

        setTitle("Consulta de Clientes");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();

        JButton btnTodos = new JButton("Consultar todos");
        JButton btnBuscar = new JButton("Consultar por ID");

        campoId = new JTextField(10);

        panelSuperior.add(new JLabel("ID Cliente:"));
        panelSuperior.add(campoId);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnTodos);

        add(panelSuperior, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultados);
        add(scroll, BorderLayout.CENTER);

        btnTodos.addActionListener(e -> consultarTodos());
        btnBuscar.addActionListener(e -> consultarPorId());
    }

    private void consultarTodos() {

        try {

            SqlSessionFactory factory = MyBatisExample.getSqlSessionFactory();
            SqlSession session = factory.openSession();

            ClienteMapper mapper = session.getMapper(ClienteMapper.class);

            List<Cliente> clientes = mapper.consultarClientes();

            areaResultados.setText("");

            for (Cliente c : clientes) {

                areaResultados.append(c.toString() + "\n");

                if (c.getRentados() != null) {
                    c.getRentados().forEach(ir ->
                        areaResultados.append("   " + ir + "\n")
                    );
                }

                areaResultados.append("\n");
            }

            session.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void consultarPorId() {

        try {

            int id = Integer.parseInt(campoId.getText());

            SqlSessionFactory factory = MyBatisExample.getSqlSessionFactory();
            SqlSession session = factory.openSession();

            ClienteMapper mapper = session.getMapper(ClienteMapper.class);

            Cliente c = mapper.consultarCliente(id);

            areaResultados.setText("");

            if (c != null) {

                areaResultados.append(c.toString() + "\n");

                if (c.getRentados() != null) {
                    c.getRentados().forEach(ir ->
                        areaResultados.append("   " + ir + "\n")
                    );
                }

            } else {

                areaResultados.append("Cliente no encontrado");

            }

            session.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ClienteGUI().setVisible(true);
        });

    }
}