package com.erc.main;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import com.erc.main.OpcionesCRUD;

import com.erc.bdhelpers.ConexionBd;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Font;


/**
 *  ESTA ES LA CLASE PRINCIPAL QUE REPRESENTA LA VENTANA PRINCIPAL DE LA APLICACIÓN DE CONTROL DE USUARIOS. 
 *  PROPORCIONA UNA INTERFAZ GRÁFICA PARA QUE LOS USUARIOS INGRESEN SUS CREDENCIALES Y SE CONECTEN A UNA BASE DE DATOS.
 */
public class MainInicio {

    private JFrame frmControlUsuarios;
    private JPasswordField passwordField;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainInicio window = new MainInicio();
                    window.frmControlUsuarios.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainInicio() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmControlUsuarios = new JFrame();
        frmControlUsuarios.setIconImage(Toolkit.getDefaultToolkit().getImage(MainInicio.class.getResource("/images/Oxon3.png")));
        frmControlUsuarios.setTitle("CONTROL USUARIOS");
        frmControlUsuarios.setBounds(100, 100, 1050, 600);
        frmControlUsuarios.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmControlUsuarios.getContentPane().setLayout(null);
        
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblUsuario.setBounds(145, 46, 150, 30);
        frmControlUsuarios.getContentPane().add(lblUsuario);
        
        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblContraseña.setBounds(145, 155, 150, 30);
        frmControlUsuarios.getContentPane().add(lblContraseña);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(145, 200, 150, 20); 
        frmControlUsuarios.getContentPane().add(passwordField);
        
        textField = new JTextField();
        textField.setBounds(145, 93, 150, 20); 
        frmControlUsuarios.getContentPane().add(textField);
        textField.setColumns(10);
        
        JButton btnConectar = new JButton("Conectar");
        btnConectar.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        btnConectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String usuario = textField.getText();
                String password = new String(passwordField.getPassword());

                ConexionBd conexionBd = new ConexionBd("oxon3", "localhost", "3306", usuario, password);
                Connection conexion = null;
                try {
                    
                    conexion = conexionBd.generarConexion();
                    JOptionPane.showMessageDialog(frmControlUsuarios, "Conexión exitosa a la base de datos.");
                    
                   
                    // CERRAMOS LA VENTANA ACTUAL Y ABRIMOS LA NUEVA VENTANA DE OPCIONES CRUD
                    frmControlUsuarios.dispose();
                    OpcionesCRUD ventanaCrud = new OpcionesCRUD(conexion);  // PASAMOS LA CONEXION A OPCIONES CRUD
                    ventanaCrud.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frmControlUsuarios, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnConectar.setBounds(145, 264, 150, 30); 
        frmControlUsuarios.getContentPane().add(btnConectar);
        
        JLabel lblFondo = new JLabel("");
        lblFondo.setHorizontalAlignment(SwingConstants.TRAILING);
        lblFondo.setIcon(new ImageIcon(MainInicio.class.getResource("/images/Oxon3.jpg"))); 
        lblFondo.setBounds(0, -20,1034, 581);
        frmControlUsuarios.getContentPane().add(lblFondo);
    }


}
    