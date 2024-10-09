package com.erc.main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.erc.bdhelpers.BDDAO;
import com.erc.dao.SalidaInfoDAO;
import com.erc.dao.URLShortener;
import com.toedter.calendar.JCalendar;
import com.erc.model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;




/**
 * CON ESTA CLASE  REPRESENTAMOS UNA INTERFAZ GRÁFICA QUE PERMITE A LOS USUARIOS  REALIZAR OPERACIONES CRUD (CREAR, LEER, ACTUALIZAR, ELIMINAR) 
 * EN DIFERENTES TABLAS  DE UNA BASE DE DATOS. PROPORCIONA FUNCIONALIDAD PARA BUSCAR REGISTROS, GENERAR REPORTES, Y GESTIONAR ENLACES A GOOGLE MAPS.
 * @author EDU RUS
 */
@SuppressWarnings("serial")
public class OpcionesCRUD extends JFrame {

	// ATRIBUTOS  O VARIBALES PARA MANEJAR
	private Connection conexion;
	private BDDAO bdDao;
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JTextField textFieldId; 
	private JButton btnBuscar; 
	private JComboBox<String> comboBoxTablas; 
	private JPanel panelTablaResultados; 
	private JTextField textFieldUrl;
	private JLabel lblEnlaceGoogle;
	private JButton btnVerCalendario;
	private URLShortener urlShortener = new URLShortener();
	private JButton btnGenerarReporteDiario;
	private JButton btnGenerarReporteCliente;

	/**
	 * 	COSNTRUCTOR QUE INICIALIZA LA INTERFAZ DE USUARIO Y ESTABLECE LA CONEXION A LA BASE DE DATOS
	 * @param Connection
	 */
	public OpcionesCRUD(Connection conexion) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(OpcionesCRUD.class.getResource("/images/Oxon3.png")));
		this.conexion = conexion;
		this.bdDao = new BDDAO();
		inicializar();
	}

	
	/**
	 * INCILIZAMOS LOS COMPONENTES DE LA VENTANA Y CONFIGURAMOS LA DISPOSION DE TODOS LOS COMPONENTES
	 */
	 private void inicializar() {
	        setTitle("TABLAS CLIENTES");
	        setBounds(100, 100, 950, 610);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        // CONFIGURACION DEL COMBOX PARA SELECCIONAR LAS TABLAS QUE DESEAMOS
	        String[] tablas = {" ", "CLIENTES", "DIRECCIONES", "TRABAJADORES", "SALIDAS", "TRABAJDORES_SALIDAS"};
	        comboBoxTablas = new JComboBox<>(tablas);
	        comboBoxTablas.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
	        comboBoxTablas.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                cambiarTabla();
	            }
	        });
	        

	        // CONFIGURACION DEL PANEL DE SELECCION
	        JPanel panelSeleccionTabla = new JPanel();
	        panelSeleccionTabla.setBounds(0, 0, 935, 83);
	        panelSeleccionTabla.setLayout(new BorderLayout());
	        JLabel label = new JLabel("Seleccionar Tabla:");
	        label.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
	        panelSeleccionTabla.add(label, BorderLayout.NORTH);
	        panelSeleccionTabla.add(comboBoxTablas, BorderLayout.CENTER);

	        // CONFIGURACION DEL JPANEL DEL ID***************************************************************************
	        JPanel panelId = new JPanel();
	        panelId.setBackground(new Color(115, 91, 162));
	        panelId.setLayout(new FlowLayout());
	        JLabel lblId = new JLabel("ID");
	        lblId.setForeground(new Color(255, 255, 255));
	        lblId.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
	        panelId.add(lblId);

	        textFieldId = new JTextField();
	        textFieldId.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
	        textFieldId.setColumns(10);
	        panelId.add(textFieldId);

	        // CREAMO EL BTN PARA BUSCAR
	        btnBuscar = new JButton("Buscar");
	        btnBuscar.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        btnBuscar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                buscarPorId();
	            }
	        });
	        panelId.add(btnBuscar);   
	       
	        // CREAMOS EL BTN PARA VER CALENDARIO
	        btnVerCalendario = new JButton("Ver Calendario");
	        btnVerCalendario.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        btnVerCalendario.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	               
	            	// NO BUSCA EL CALENDARIO NI BUSCA POR ID
	                mostrarCalendario(null);
	            }
	        });
	        panelId.add(btnVerCalendario); 

	        // CONFIGURACION DEL ENLACE DE GOOGLE
	        lblEnlaceGoogle = new JLabel("ENLACE\r\n");
	        lblEnlaceGoogle.setForeground(new Color(255, 255, 255));
	        lblEnlaceGoogle.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
	        lblEnlaceGoogle.setVisible(false); // OCULTAMOS INICIALMENTE PUES NO LO NECESITAMOS DE INICIO
	        panelId.add(lblEnlaceGoogle);

	        textFieldUrl = new JTextField();
	        textFieldUrl.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
	        textFieldUrl.setColumns(20);
	        textFieldUrl.setVisible(false); // OCULTAMOS INICIALMENTE PUES NO LO NECESITAMOS DE INICIO
	        getContentPane().setLayout(null);
	        panelId.add(textFieldUrl);

	        panelSeleccionTabla.add(panelId, BorderLayout.SOUTH);
	        btnGenerarReporteDiario = new JButton(" Reporte Diario");
			btnGenerarReporteDiario.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
			btnGenerarReporteDiario.setVisible(false);
			panelId.add(btnGenerarReporteDiario);
			
            // CREAMOS EL BTN DE REPORTECLIENTES
		    btnGenerarReporteCliente = new JButton(" Reporte por Cliente");
			btnGenerarReporteCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
			btnGenerarReporteCliente.setVisible(false);
			panelId.add(btnGenerarReporteCliente);			
			btnGenerarReporteCliente.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {			        
			        String idText = textFieldId.getText().trim();			      
			        if (!idText.isEmpty()) {
			            try {
			                int idCliente = Integer.parseInt(idText);
			                generarReporteCliente(idCliente); 
			            } catch (NumberFormatException ex) {
			                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
			            }
			        } else {
			            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID.", "Error", JOptionPane.ERROR_MESSAGE);
			        }
			    }
			});
            // GENERAMOS EL INFORMEDIARIO ASOCIADO A UN CLIENTE
			btnGenerarReporteDiario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					generarReporteDiario();
				}
			});
	        getContentPane().add(panelSeleccionTabla);
	        
	        

	        // CONFIGURAMOS EL PANEL DE LA TABLA PAR MOSTRAR LOS RESULTADOD****************************************************************************
	        panelTablaResultados = new JPanel(new BorderLayout());
	        panelTablaResultados.setBounds(0, 83, 940, 445);
	        modeloTabla = new tablaModeloFechas(); 
	        tabla = new JTable(modeloTabla);
	        tabla.setRowSelectionAllowed(false);
	        tabla.setEnabled(false);
	        JScrollPane scrollPane = new JScrollPane(tabla);
	        panelTablaResultados.add(scrollPane, BorderLayout.CENTER);
	        getContentPane().add(panelTablaResultados);

	        // CREACION DE LOS PANELES CON LOS BOTONES DEL CRUD 
	        JPanel panelBotones = new JPanel();
	        panelBotones.setBackground(new Color(0, 150, 64));
	        panelBotones.setBounds(0, 528, 940, 40);
	        getContentPane().add(panelBotones);
	        
	         // CREACION DE UNA LINEA DE SEPARACION MANTENUENDO DISTANCIA CON EL BTN DE DESCONECTAR
	        Component horizontalStrut_1 = Box.createHorizontalStrut(130);
	        panelBotones.add(horizontalStrut_1);
            // CREACION DEL BTN LISTAR
	        JButton btnListar = new JButton("Listar");
	        btnListar.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        panelBotones.add(btnListar);
	        btnListar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                listar();
	            }
	        });
	        // CREACION DEL BTN INSERTAR
	        JButton btnInsertar = new JButton("Insertar");
	        btnInsertar.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        panelBotones.add(btnInsertar);
	        btnInsertar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                insertar();
	            }
	        });
	        // CREACION DEL BTN ACTUALIZAR
	        JButton btnActualizar = new JButton("Actualizar");
	        btnActualizar.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        panelBotones.add(btnActualizar);
	        btnActualizar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                actualizar();
	            }
	        });
	        // CREACION DEL BTN ELIMINAR
	        JButton btnEliminar = new JButton("Eliminar");
	        btnEliminar.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
	        panelBotones.add(btnEliminar);
	        btnEliminar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                eliminar();
	            }
	        });
	         // CREACION DE UNA LINEA DE SEPARACION MANTENUENDO DISTANCIA CON EL BTN DE DESCONECTAR
	        Component horizontalStrut = Box.createHorizontalStrut(260);
	        panelBotones.add(horizontalStrut);
	        // CREACION DEL BTN DESCONECTAR
	        JButton btnDesconectar = new JButton("Desconectar");
	        btnDesconectar.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
	        panelBotones.add(btnDesconectar);
	        btnDesconectar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                desconectar();  // LLAMAMOS AL METODO DESCONETAR CREADO MAS ABAJO
	            }
	        });

	        panelBotones.add(btnDesconectar);	
	        // INICIALIZAMOS LA TABLA COMO VACIA
	        panelTablaResultados.setVisible(true);
	    }



	 /**
	  * MUESTRA UN DIÁLOGO QUE CONTIENE UN CALENDARIO Y UNA TABLA DE DATOS  RELACIONADOS CON LAS FECHAS SELECCIONADAS.
	  *  PERMITE AL USUARIO SELECCIONAR UNA FECHA DEL CALENDARIO Y VER LA INFORMACIÓN CORRESPONDIENTE EN LA TABLA.
	  * @param id
	  */
	private void mostrarCalendario(Integer id) {
		JDialog calendarioDialog = new JDialog(this, "Calendario", true);
		calendarioDialog.getContentPane().setLayout(new BorderLayout());
		calendarioDialog.setSize(900, 700);

		// CREACION DEL CALENDARIO
		JCalendar calendario = new JCalendar();
		calendarioDialog.getContentPane().add(calendario, BorderLayout.NORTH);

		// CREAMOS EL MODELO DE LA TABLA  Y SU MODELO PARA MOSTRAR LA INFORMACION
		tablaModeloFechas tableModel = new tablaModeloFechas();
		JTable table = new JTable(tableModel);

		// CONFIGURACION DE LA TABLA Y FLEXIBILIDAD DE LA MISMA
		table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);

		// CONFIGURACION DE LAS CELDAS PARA CADA COLUMNA DEL ENLACE
		table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 5 && value instanceof String) {
					String url = (String) value;
					JLabel label = new JLabel("<html><a href='" + url + "'>" + url + "</a></html>");
					label.setForeground(Color.BLUE); // APLICAMOS UN COLOR AZUL AL ENLACE URL PARA QUE SEA MAS VISIBLE
					label.setCursor(new Cursor(Cursor.HAND_CURSOR));
					return label;
				}
				return componente;
			}
		});

		table.addMouseListener(new java.awt.event.MouseAdapter() { // NOS PERMITE HACER CLICK Y MUESTRA EN OTR PANTALLA EL URL
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int column = table.columnAtPoint(evt.getPoint());
				if (column == 5) { // INDICE CONINCIDE CON EL STRING DE LA COLUMNA DE LA TABLA MODEELO DE FECHAS
					String url = (String) table.getValueAt(row, column);
					try {
						Desktop.getDesktop().browse(new java.net.URI(url));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al abrir el enlace: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		// AÑADIMOS UN SCROLL POR SI SALEN TODAS LAS SALIDAS EN LA PANTALLA
		JScrollPane scrollPane = new JScrollPane(table);
		calendarioDialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// DETECTAMOS LOS CAMBIOS DE FECHA
		calendario.getDayChooser().addPropertyChangeListener(evt -> {
			if ("day".equals(evt.getPropertyName())) {
				java.util.Date selectedDate = calendario.getDate();
				java.sql.Date sqlSelectedDate = new java.sql.Date(selectedDate.getTime());
				System.out.println("Fecha seleccionada: " + sqlSelectedDate); // MANERA DE DEPURACION PARA MOSTRAR POR CONSOLA
				marcarFechasEnTabla(tableModel, sqlSelectedDate, id); // ACTUALIZA LA TABLA CON LA NUEVA FECHA Y FILTRA POR ID

				// FORZAR ACTUALIZACIÓN DEL MODELO Y VISTA
				table.revalidate();
				table.repaint();
			}
		});

		calendarioDialog.setVisible(true);
	}



/**
 *  CON ESTE MÉTODO RECUPERAMOS LAS SALIDAS DE LA BASE DE DATOS Y FILTRA LOS RESULTADOS BASÁNDOSE EN LA FECHA SELECCIONADA
 *  Y EL ID PROPORCIONADO. LOS RESULTADOS SE MUESTRAN EN EL MODELO DE TABLA CREADA Y SE GENERAN INFORMES EN PDF SI SE 
 *  ENCUENTRAN  SALIDAS CORRESPONDIENTES
 * @param modelFechas
 * @param selectedDate
 * @param id
 */
	private void marcarFechasEnTabla(tablaModeloFechas modelFechas, Date selectedDate, Integer id) {
		try {
			ArrayList<tablaSalidas> salidas = bdDao.listarSalidas(conexion);
			LocalDate fechaSeleccionada = selectedDate.toLocalDate();
			System.out.println("Número de salidas recuperadas: " + salidas.size());

			modelFechas.setRowCount(0); //  LIMPIAMOS EL MODELO ANTES DE AGREGAR NUEVAS FILAS A LA TABLA

			List<SalidaInfo> salidaInfos = new ArrayList<>();

			for (tablaSalidas salida : salidas) {
				LocalDate fechaTarea = salida.getFechaTarea();
				if (fechaTarea.isEqual(fechaSeleccionada) && (id == null || salida.getId() == id)) {
					int idTrabajador = salida.getIdTrabajador();
					int idDireccion = salida.getIdDireccion();

					tablaTrabajadores trabajador = bdDao.obtenerTrabajadorPorId(conexion, idTrabajador);
					tablaDirecciones direccion = bdDao.obtenerDireccionPorId(conexion, idDireccion);
					tablaClientes cliente = null;

					if (direccion != null) {
						int idCliente = direccion.getIdCliente();
						cliente = bdDao.obtenerClientePorId(conexion, idCliente);
					}

					if (trabajador != null && direccion != null && cliente != null) {
						SalidaInfo salidaInfo = new SalidaInfo(
								salida,
								salida.getTarea(),
								trabajador.getPuesto(),
								direccion.getLocalidad(),
								cliente.getNombre(),
								"https://www.google.com/maps/?q=" + direccion.getLatitud() + "," + direccion.getLongitud()
								);

						modelFechas.addFecha(
								salida.getFechaTarea(),
								salida.getId(),
								trabajador.getPuesto(),
								direccion.getLocalidad(),
								cliente.getNombre(),
								"https://www.google.com/maps/?q=" + direccion.getLatitud() + "," + direccion.getLongitud(),
								salida.getDescripcion(),
								salida.isInstalaciones(),
								salida.isIncidencias()
								);

						salidaInfos.add(salidaInfo); // AÑADIMOS A LA LISTA DE SALIDASINFO PARA CREAR EL PDF
					} else {
						System.out.println("Datos incompletos para el trabajador con ID: " + idTrabajador);
					}
				}
			}
			//  LLAMAMOS AL METODO PARA GENERAR EL PDF CON LAS SALIDDAS FILTRADAS
			modelPDFDiario.generarPDF(salidaInfos, fechaSeleccionada);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al obtener las fechas de las salidas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 *  CON ESTE MÉTODO  EL USUARIO SELECCIONA UNA TABLA DIFERENTE  EN EL COMBOBOXTABLAS. 
	 *  ACTUALIZA LAS COLUMNAS DEL MODELO DE TABLA SEGÚN  LA SELECCIÓN, ASÍ COMO LA VISIBILIDAD DE CIERTOS COMPONENTES DE LA INTERFAZ,
	 *  COMO BOTONES Y CAMPOS DE TEXTO, QUE SON NECESARIOS PARA LA TABLA SELECCIONADA.
	 */
	private void cambiarTabla() {
		String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
		// CONFIGURAMOS LAS COLUNNAS DE LA TABLA
		if ("CLIENTES".equals(tablaSeleccionada)) {
			modeloTabla.setColumnIdentifiers(new String[]{"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Correo"});
			lblEnlaceGoogle.setVisible(false);
			textFieldUrl.setVisible(false);
			btnVerCalendario.setVisible(false);
			btnGenerarReporteDiario.setVisible(true);
			btnGenerarReporteCliente.setVisible(true);
		} else if ("DIRECCIONES".equals(tablaSeleccionada)) {
			modeloTabla.setColumnIdentifiers(new String[]{"ID", "Dirección", "Código Postal", "Localidad", "Latitud", "Longitud", "ID Cliente"});
			lblEnlaceGoogle.setVisible(true);
			textFieldUrl.setVisible(true);
			btnVerCalendario.setVisible(false);
			btnGenerarReporteDiario.setVisible(false);
			btnGenerarReporteCliente.setVisible(false);
		} else if ("TRABAJADORES".equals(tablaSeleccionada)) {
			modeloTabla.setColumnIdentifiers(new String[]{"ID","Nombre", "Apellidos", "Teléfono","SS", "Puesto", "ID Dirección"});
			lblEnlaceGoogle.setVisible(false);
			textFieldUrl.setVisible(false);
			btnVerCalendario.setVisible(false);
			btnGenerarReporteDiario.setVisible(false);
			btnGenerarReporteCliente.setVisible(false);
		} else if ("SALIDAS".equals(tablaSeleccionada)) {
			modeloTabla.setColumnIdentifiers(new String[]{"ID", "Tarea", "Instalaciones", "Incidencias", "Solución", "Descripción", "Coste Cliente", "Fecha Tarea", "ID CLIENTE", "ID DIRECCION", "ID TRABAJADOR"});
			lblEnlaceGoogle.setVisible(false);
			textFieldUrl.setVisible(false);
			btnVerCalendario.setVisible(true);
			btnGenerarReporteDiario.setVisible(false);
			btnGenerarReporteCliente.setVisible(false);
		} else if ("TRABAJADORES SALIDAS".equals(tablaSeleccionada)) {
			modeloTabla.setColumnIdentifiers(new String[]{"ID", "ID Trabajador", "ID Salida"});
			lblEnlaceGoogle.setVisible(false);
			textFieldUrl.setVisible(false);
			btnVerCalendario.setVisible(false);
			btnGenerarReporteDiario.setVisible(false);
			btnGenerarReporteCliente.setVisible(false);
		}
		// LIMPIAMOS Y COULTAMOS LAS COLUNMAS DE LA TABLA AL PASAR DE UNA A OTRA
		panelTablaResultados.setVisible(false);
	}


	/**
	 *   CON ESTE MÉTODO SE ENCARGA DE LIMPIAR EL MODELO DE TABLA Y LUEGO LLENAR LAS FILAS  CON LOS DATOS CORRESPONDIENTES
	 *  A LA TABLA SELECCIONADA (CLIENTES, DIRECCIONES,  TRABAJADORES, SALIDAS O TRABAJADORES SALIDAS) 
	 *  RECUPERANDO LA INFORMACIÓN DE LA BASE DE DATOS A TRAVÉS DE UN OBJETO DE ACCESO A DATOS (BDDAO).
	 */
	private void listar() {

		try {
			String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
			modeloTabla.setRowCount(0); //AL LISTAR LIMPIAMOS LAS TABLAS

			//  LISTADO DE DATOS SEGUN LA TABLA SELECCIONADA
			if ("CLIENTES".equals(tablaSeleccionada)) {
				ArrayList<tablaClientes> clientes = bdDao.listarClientes(conexion);
				for (tablaClientes cliente : clientes) {
					modeloTabla.addRow(new Object[]{
							cliente.getId(),
							cliente.getNombre(),
							cliente.getApellidos(),
							cliente.getDni(),
							cliente.getTelefono(),
							cliente.getCorreo()
					});
				}
			} else if ("DIRECCIONES".equals(tablaSeleccionada)) {
				ArrayList<tablaDirecciones> direcciones = bdDao.listarDirecciones(conexion);
				for (tablaDirecciones direccion : direcciones) {
					modeloTabla.addRow(new Object[]{
							direccion.getId(),
							direccion.getDireccion(),
							direccion.getCodigoPostal(),
							direccion.getLocalidad(),
							direccion.getLatitud(),
							direccion.getLongitud(),
							direccion.getIdCliente()
					});
				}
			} else if ("TRABAJADORES".equals(tablaSeleccionada)) {
				ArrayList<tablaTrabajadores> trabajadores = bdDao.listarTrabajadores(conexion);
				for (tablaTrabajadores trabajador : trabajadores) {
					modeloTabla.addRow(new Object[]{
							trabajador.getId(),
							trabajador.getNombre(),
							trabajador.getApellidos(),
							trabajador.getTelefono(),
							trabajador.getSs(),
							trabajador.getPuesto(),
							trabajador.getIdDireccion()
					});
				}
			} else if ("SALIDAS".equals(tablaSeleccionada)) {
				ArrayList<tablaSalidas> salidas = bdDao.listarSalidas(conexion);
				for (tablaSalidas salida : salidas) {
					modeloTabla.addRow(new Object[]{
							salida.getId(),
							salida.getTarea(),
							salida.isInstalaciones(),
							salida.isIncidencias(),
							salida.isSolucion(),
							salida.getDescripcion(),
							salida.getCosteCliente(),
							salida.getFechaTarea(),
							salida.getIdCliente(),
							salida.getIdDireccion(),
							salida.getIdTrabajador()
					});
				}
				btnVerCalendario.setVisible(!salidas.isEmpty());
			} else if ("TRABAJADORES SALIDAS".equals(tablaSeleccionada)) {
				ArrayList<tablaTrabajadoresSalidas> trabajadoresSalidas = bdDao.listarTrabajadoresSalidas(conexion);
				for (tablaTrabajadoresSalidas trabajadorSalida : trabajadoresSalidas) {
					modeloTabla.addRow(new Object[]{
							trabajadorSalida.getId(),
							trabajadorSalida.getIdTrabajador(),
							trabajadorSalida.getIdSalida()

					});
				}
			}
			panelTablaResultados.setVisible(true); // MUESTRA OS RESULTADOS DE LA TABLA
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al listar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}


	
	/**
	 *  CON ESTE MÉTODO RECUPERA EL ID DESDE EL  CAMPO DE TEXTO Y VERIFICA QUE NO ESTÉ VACÍO.  SI EL ID ES VÁLIDO, 
	 *  SE INTENTA BUSCAR EL REGISTRO CORRESPONDIENTE EN LA BBDD SEGÚN LA TABLA SELECCIONADA EN EL COMBOBOXTABLAS. 
	 *  SI SE ENCUENTRA EL REGISTRO, SE AÑADE A LA TABLA VISIBLE; DE LO CONTRARIO, SE MUESTRA UN MENSAJE INFORMANDO 
	 *  QUE NO SE ENCONTRÓ EL REGISTRO.
	 */
	private void buscarPorId() {
		String idStr = textFieldId.getText();
		if (idStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			int id = Integer.parseInt(idStr);
			String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
			modeloTabla.setRowCount(0); // LIMPIAMOS LA TABLA ANTES DE LISTARLA
			if ("CLIENTES".equals(tablaSeleccionada)) {
				tablaClientes cliente = bdDao.obtenerClientePorId(conexion, id);
				if (cliente != null) {
					modeloTabla.addRow(new Object[]{
							cliente.getId(),
							cliente.getNombre(),
							cliente.getApellidos(),                           
							cliente.getDni(),
							cliente.getTelefono(),
							cliente.getCorreo()
					});
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró el CLIENTE con ID: " + id, "Resultado", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if ("DIRECCIONES".equals(tablaSeleccionada)) {
				tablaDirecciones direccion = bdDao.obtenerDireccionPorId(conexion, id);
				if (direccion != null) {
					modeloTabla.addRow(new Object[]{
							direccion.getId(),
							direccion.getDireccion(),
							direccion.getCodigoPostal(),
							direccion.getLocalidad(),
							direccion.getLatitud(),
							direccion.getLongitud(),
							direccion.getIdCliente()
					});
					// GENERAMOS EL ENLACE DE GOOGLE, CON LOS ATRINUTOS NECESARIOS
					generarEnlaceGoogleMaps(direccion.getLatitud(), direccion.getLongitud());
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró la DIRECCION con ID: " + id, "Resultado", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else if ("TRABAJADORES".equals(tablaSeleccionada)) {
				tablaTrabajadores trabajador = bdDao.obtenerTrabajadorPorId(conexion, id);
				if (trabajador != null) {
					modeloTabla.addRow(new Object[]{
							trabajador.getId(),
							trabajador.getNombre(),
							trabajador.getApellidos(),
							trabajador.getTelefono(),
							trabajador.getSs(),
							trabajador.getPuesto(),
							trabajador.getIdDireccion()
					});
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró el TRABAJADOR con ID: " + id, "Resultado", JOptionPane.INFORMATION_MESSAGE);
				}
			}else if ("SALIDAS".equals(tablaSeleccionada)) {
				tablaSalidas salida = bdDao.obtenerSalidaPorId(conexion, id);
				if (salida != null) {
					modeloTabla.addRow(new Object[]{
							salida.getId(),
							salida.getTarea(),
							salida.isInstalaciones(),
							salida.isIncidencias(),
							salida.isSolucion(),
							salida.getDescripcion(),
							salida.getCosteCliente(),                           
							salida.getFechaTarea(),                            
							salida.getIdCliente(),
							salida.getIdDireccion(),
							salida.getIdTrabajador(),
					});
					btnVerCalendario.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró la SALIDA con ID: " + id, "Resultado", JOptionPane.INFORMATION_MESSAGE);
					btnVerCalendario.setVisible(false);
				}
			}else if ("TRABAJADORES SALIDAS".equals(tablaSeleccionada)) {
				tablaTrabajadoresSalidas trabajadorSalida = bdDao.obtenerTrabajadorSalidaPorID(conexion, id);
				if (trabajadorSalida != null) {
					modeloTabla.addRow(new Object[]{
							trabajadorSalida.getId(),
							trabajadorSalida.getIdTrabajador(),
							trabajadorSalida.getIdSalida()
					});
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró el trabajador salida con ID: " + id, "Resultado", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			panelTablaResultados.setVisible(true); // MOSTRAMOS LA TABLA DESEPUES DE LISTARLA
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	
	/**
	 *  CON ESTE MÉTODO PERMITE AL USUARIO INGRESAR INFORMACIÓN PARA CREAR UN NUEVO REGISTRO  EN LA BBDD
	 *   BASADO EN LA TABLA SELECCIONADA EN EL COMBOBOXTABLAS. 
	 *   ADEMAS UTILIZAMOS JOPTIONPANE PARA SOLICITAR LOS DATOS NECESARIOS. LOS REGISTROS QUE SE  PUEDEN INSERTAR INCLUYEN
	 *   LAS TABLAS CREADAS ANTERIORMENTE
	 */
	private void insertar() {
		String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
		try {
			if ("CLIENTES".equals(tablaSeleccionada)) {
				String nombre = JOptionPane.showInputDialog("Nombre:");
				String apellidos = JOptionPane.showInputDialog("Apellidos:");
				String dni = JOptionPane.showInputDialog("DNI:");
				String telefono = JOptionPane.showInputDialog("Teléfono:");
				String correo = JOptionPane.showInputDialog("Correo Electronico:");
				bdDao.insertarCliente(conexion, nombre, apellidos, dni, telefono, correo);

			} else if ("DIRECCIONES".equals(tablaSeleccionada)) {
				String direccion = JOptionPane.showInputDialog("Dirección:");
				String codigoPostal = JOptionPane.showInputDialog("Código Postal:");
				String localidad = JOptionPane.showInputDialog("Localidad:");
				String latitudStr = JOptionPane.showInputDialog("Latitud:");
				String longitudStr = JOptionPane.showInputDialog("Longitud:");
				String idClienteStr = JOptionPane.showInputDialog("ID Usuario:");
				double latitud = Double.parseDouble(latitudStr);
				double longitud = Double.parseDouble(longitudStr);
				int idcliente = Integer.parseInt(idClienteStr);
				bdDao.insertarDireccion(conexion, direccion, codigoPostal, localidad, latitud, longitud, idcliente);

			}else if ("TRABAJADORES".equals(tablaSeleccionada)) {
				String nombre = JOptionPane.showInputDialog("Nombre del Trabajador:");
				String apellidos = JOptionPane.showInputDialog("Apellidos del Trabajador:");
				String telefono = JOptionPane.showInputDialog("Teléfono del Trabajador:");
				String ss = JOptionPane.showInputDialog("Número de Seguridad Social:");
				String puesto = JOptionPane.showInputDialog("Puesto:");
				String idDireccionStr = JOptionPane.showInputDialog("ID Dirección:");
				int idDireccion = Integer.parseInt(idDireccionStr);              
				bdDao.insertarTrabajador(conexion, nombre, apellidos, telefono, ss, puesto, idDireccion);                


			}else if ("SALIDAS".equals(tablaSeleccionada)) {
				String tarea = JOptionPane.showInputDialog("Tarea:");
				String instalacionesStr = JOptionPane.showInputDialog("Instalaciones (true/false):");
				String incidenciasStr = JOptionPane.showInputDialog("Incidencias (true/false):");
				String solucionStr = JOptionPane.showInputDialog("Solución (true/false):");
				String descripcion = JOptionPane.showInputDialog("Descripción:");
				String costeClienteStr = JOptionPane.showInputDialog("Coste Cliente:");
				String fechaTareaStr = JOptionPane.showInputDialog("Fecha Tarea (yyyy-MM-dd):");
				String idClienteStr = JOptionPane.showInputDialog("ID Cliente:");
				String idDireccionStr = JOptionPane.showInputDialog("ID Dirección:");
				String idTrabajadorStr = JOptionPane.showInputDialog("ID Trabajador:");                
				boolean instalaciones = Boolean.parseBoolean(instalacionesStr);
				boolean incidencias = Boolean.parseBoolean(incidenciasStr);
				boolean solucion = Boolean.parseBoolean(solucionStr);
				double costeCliente = Double.parseDouble(costeClienteStr);              
				Date fechaTarea = Date.valueOf(fechaTareaStr);
				int idCliente = Integer.parseInt(idClienteStr);
				int idDireccion = Integer.parseInt(idDireccionStr);
				int idTrabajador = Integer.parseInt(idTrabajadorStr);

				bdDao.insertarSalida(conexion, tarea, instalaciones, incidencias, solucion, descripcion, costeCliente, fechaTarea, idCliente, idDireccion, idTrabajador);

			}else if ("TRABAJADOR SALIDAS".equals(tablaSeleccionada)) {
				String idTrabajadorStr = JOptionPane.showInputDialog("ID Trabajador:");
				String idSalidaStr = JOptionPane.showInputDialog("ID Salida:");
				int idTrabajador = Integer.parseInt(idTrabajadorStr);
				int idSalida = Integer.parseInt(idSalidaStr);
				bdDao.insertarTrabajadorSalida(conexion, idTrabajador, idSalida);
			}

			listar(); // ACTUALIZA LA TABLA DESPUES DE INSERTAR LOS DATOS
		} catch (SQLException | NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Error al insertar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	/**
	 *  CON ESTE MÉTODO PERMITE AL USUARIO INGRESAR INFORMACIÓN PARA ACTUALIZAR UN REGISTRO  ESPECÍFICO EN LA BBDD,
	 *  BASADO EN LA TABLA SELECCIONADA EN EL COMBOBOXTABLAS.ADEMAS UTILIZAMOS JOPTIONPANE PARA SOLICITAR LOS DATOS NECESARIOS.
	 *  LOS REGISTROS QUE SE PUEDEN ACTUALIZAR SON LAS TABLAS CREADAS ANTERIORMENTE
	 */
	private void actualizar() {
		String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
		try {
			if ("CLIENTES".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID del Cliente a Actualizar:");
				int id = Integer.parseInt(idStr);
				String nombre = JOptionPane.showInputDialog("Nombre:");
				String apellidos = JOptionPane.showInputDialog("Apellidos:");
				String dni = JOptionPane.showInputDialog("DNI:");
				String telefono = JOptionPane.showInputDialog("Teléfono:");
				String correo = JOptionPane.showInputDialog("Correo:");
				bdDao.actualizarCliente(conexion, id, nombre, apellidos, dni, telefono, correo);

			} else if ("DIRECCIONES".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID de la Dirección a Actualizar:");
				int id = Integer.parseInt(idStr);
				String direccion = JOptionPane.showInputDialog("Dirección:");
				String codigoPostal = JOptionPane.showInputDialog("Código Postal:");
				String localidad = JOptionPane.showInputDialog("Localidad:");
				String latitudStr = JOptionPane.showInputDialog("Latitud:");
				String longitudStr = JOptionPane.showInputDialog("Longitud:");
				String idClienteStr = JOptionPane.showInputDialog("ID Cliente:");
				String latitud = latitudStr.trim();
				String longitud = longitudStr.trim();
				int idCliente = Integer.parseInt(idClienteStr);
				bdDao.actualizarDireccion(conexion, id, direccion, codigoPostal, localidad, latitud, longitud, idCliente);

			} else if ("TRABAJADORES".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID del Trabajador a Actualizar:");
				int id = Integer.parseInt(idStr);
				String nombre = JOptionPane.showInputDialog("Nuevo Nombre del Trabajador (dejar vacío para no cambiar):");
				String apellidos = JOptionPane.showInputDialog("Nuevos Apellidos del Trabajador (dejar vacío para no cambiar):");
				String telefono = JOptionPane.showInputDialog("Nuevo Teléfono del Trabajador (dejar vacío para no cambiar):");
				String ss = JOptionPane.showInputDialog("Nuevo Número de Seguridad Social (dejar vacío para no cambiar):");
				String puesto = JOptionPane.showInputDialog("Nuevo Puesto del Trabajador (dejar vacío para no cambiar):");
				String idDireccionStr = JOptionPane.showInputDialog("Nuevo ID Dirección (0 para no cambiar):");
				int idDireccion = Integer.parseInt(idDireccionStr);
				bdDao.actualizarTrabajador(conexion, id, nombre, apellidos, telefono, ss, puesto, idDireccion);

			}else if ("SALIDAS".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID de la Salida a Actualizar:");
				int id = Integer.parseInt(idStr);

				String tarea = JOptionPane.showInputDialog("Tarea:");
				String instalacionesStr = JOptionPane.showInputDialog("Instalaciones (true/false):");
				String incidenciasStr = JOptionPane.showInputDialog("Incidencias (true/false):");
				String solucionStr = JOptionPane.showInputDialog("Solución (true/false):");
				String descripcion = JOptionPane.showInputDialog("Descripción:");
				String costeClienteStr = JOptionPane.showInputDialog("Coste Cliente:");
				String fechaTareaStr = JOptionPane.showInputDialog("Fecha Tarea (yyyy-MM-dd):");
				String idClienteStr = JOptionPane.showInputDialog("ID Cliente:");
				String idDireccionStr = JOptionPane.showInputDialog("ID Dirección:");
				String idTrabajadorStr = JOptionPane.showInputDialog("ID Trabajador:");
				boolean instalaciones = Boolean.parseBoolean(instalacionesStr);
				boolean incidencias = Boolean.parseBoolean(incidenciasStr);
				boolean solucion = Boolean.parseBoolean(solucionStr);
				double costeCliente = 0.0;
				try {
					costeCliente = Double.parseDouble(costeClienteStr);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Coste Cliente debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Date fechaTarea = null;

				try {
					if (fechaTareaStr != null && !fechaTareaStr.trim().isEmpty()) {
						fechaTarea = Date.valueOf(fechaTareaStr);
					}
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				int idCliente = Integer.parseInt(idClienteStr);
				int idDireccion = Integer.parseInt(idDireccionStr);
				int idTrabajador = Integer.parseInt(idTrabajadorStr);

				bdDao.actualizarSalida(conexion, id, tarea, instalacionesStr, incidenciasStr, solucionStr, descripcion, costeClienteStr, fechaTareaStr, idCliente, idDireccion, idTrabajador);


			} else if ("TRABAJADORES_SALIDAS".equals(tablaSeleccionada)) {
				String idTrabajadorStr = JOptionPane.showInputDialog("ID del Trabajador a Actualizar:");
				int idTrabajador = Integer.parseInt(idTrabajadorStr);
				String idSalidaStr = JOptionPane.showInputDialog("ID de la Salida:");
				int idSalida = Integer.parseInt(idSalidaStr);
				bdDao.actualizarTrabajadorSalida(conexion, idTrabajador, idSalida);
			}

			listar(); // ACTUALIZA LA TABLA DESPUES DE ACTUALIZAR LOS DATOS
		} catch (SQLException | NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Error al actualizar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * CON ESTE MÉTODO PERMITE AL USUARIO INGRESAR EL ID DE UN REGISTRO QUE DESEA ELIMINAR DE LA BBDD, BASADO EN LA TABLA SELECCIONADA
	 * EN EL COMBOBOXTABLAS.ADEMAS UTILIZAMOS CUADROS DE DIÁLOGO JOPTIONPANE PARA SOLICITAR EL ID CORRESPONDIENTE. 
	 * LOS REGISTROS QUE SE PUEDEN ELIMINAR EN LAS TABLAS CREADAS ANTERIORMENTE
	 */
	private void eliminar() {
		String tablaSeleccionada = (String) comboBoxTablas.getSelectedItem();
		try {
			if ("Clientes".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID del Clientes a Eliminar:");
				int id = Integer.parseInt(idStr);
				bdDao.borrarClientePorId(conexion, id);
			} else if ("Direcciones".equals(tablaSeleccionada)) {
				String idStr = JOptionPane.showInputDialog("ID de la Dirección a Eliminar:");
				int id = Integer.parseInt(idStr);
				bdDao.borrarDireccionPorId(conexion, id);
			}  else if ("Trabajadores".equals(tablaSeleccionada)) {                
				String idStr = JOptionPane.showInputDialog("ID del Trabajador a Eliminar:");
				int id = Integer.parseInt(idStr);
				bdDao.borrarTrabajadorPorId(conexion, id);
			} else if ("Salidas".equals(tablaSeleccionada)) {               
				String idStr = JOptionPane.showInputDialog("ID de la Salida a Eliminar:");
				int id = Integer.parseInt(idStr);
				bdDao.borrarSalidaPorId(conexion, id);
			}else if ("Trabajadores Salidas".equals(tablaSeleccionada)) {               
				String idStr = JOptionPane.showInputDialog("ID de la Trabajador Salida a Eliminar:");
				int id = Integer.parseInt(idStr);
				bdDao.borrarSalidaPorId(conexion, id);
			}
			listar(); // ACTUALIZA LA TABLA DESPUES DE ELIMINAR LOS DATOS
		} catch (SQLException | NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Error al eliminar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/**
	 * CON ESTE MÉTODO CREAMOS UN ENLACE A GOOGLE MAPS QUE MUESTRA LA UBICACIÓN ESPECIFICADA POR LAS  COORDENADAS. 
	 * EL ENLACE SE ACORTA UTILIZANDO UN SERVICIO DE ACORTAMIENTO DE URL Y SE MUESTRA EN UN CAMPO DE TEXTO. ADEMÁS, 
	 * SE AGREGA UN LISTENER PARA QUE EL ENLACE SEA CLICABLE; AL HACER  CLIC, SE ABRE LA UBICACIÓN EN EL NAVEGADOR WEB DEL USUARIO.
	 * @param  double LATITUD
	 * @param  double LONGITUD
	 */
	private void generarEnlaceGoogleMaps(double latitud, double longitud) {
		String originalEnlace = "https://www.google.com/maps?q=" + latitud + "," + longitud;
		String enlaceCorto = urlShortener.shortenURL(originalEnlace);
		textFieldUrl.setText("COORDENADAS"); //  MUESTRA EL ENLACE EN EL CAMPO DE TEXTO

		//  HACER QUE EL ENLACE SEA CLICABLE
		textFieldUrl.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					Desktop.getDesktop().browse(new java.net.URI(originalEnlace)); // ABRIMOS EL ENLACE EN EL NAVEGADOR
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al abrir el enlace: " + e.getMessage());
				}
			}
		});
	}

	/**
	 * CON ESTE MÉTODO VERIFICAMOS SI HAY UNA CONEXIÓN ACTIVA A LA BASE DE DATOS. SI LA CONEXIÓN ESTÁ ABIERTA, 
	 * LA CIERRA PARA LIBERAR RECURSOS. EN CASO DE QUE OCURRA UN ERROR DURANTE EL CIERRE DE LA CONEXIÓN,
	 * SE MUESTRA UN MENSAJE DE ERROR AL USUARIO. LUEGO, SE CIERRA LA VENTANA ACTUAL Y SE REINICIA 
	 * LA VENTANA PRINCIPAL DE LA APLICACIÓN.
	 */
	private void desconectar() {
		try {
			// CERRAMOS LA CONEXION A LA BBDD
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		//  CERRAMOS LA VENTANA ACTUAL
		dispose();

		// ABRIMOS OTRA VEZ LA VENTANA PRINCIPAL DE INICIO
		MainInicio.main(new String[0]);
	}

	
	/**
	 *  CON ESTE MÉTODO OBTIENEMOS LA INFORMACIÓN DE SALIDAS DEL DÍA ACTUAL DESDE LA BASE DE DATOS 
	 *  Y GENERA UN REPORTE EN FORMATO PDF. SI NO SE ENCUENTRAN DATOS PARA EL REPORTE, 
	 *  SE  MUESTRA UN MENSAJE DE ADVERTENCIA. EN CASO DE ERRORES DURANTE LA OBTENCIÓN DE DATOS  O LA GENERACIÓN DEL PDF, 
	 *  SE MUESTRA UN MENSAJE DE ERROR CORRESPONDIENTE.
	 */
	private void generarReporteDiario() {
		try {
			SalidaInfoDAO salidaInfoDAO = new SalidaInfoDAO(conexion, bdDao);
			List<SalidaInfo> salidaInfos = salidaInfoDAO.obtenerSalidaInfo(LocalDate.now(), null);
			// VERIFICACION SI SE OBTUVIERON LOS DATOS
			if (salidaInfos.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No hay datos para generar el reporte diario.");
				return;
			}

			//  GENERAMOS EL PDF USUANDO LOS DATOS OBTENIDOS
			modelPDFDiario.generarPDF(salidaInfos, LocalDate.now());
			JOptionPane.showMessageDialog(this, "Reporte diario generado exitosamente.");

		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(this, "Error al obtener datos para el reporte diario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, "Error al generar el reporte diario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}




	/**
	 * CON ESTE MÉTODO UTILIZAMOS EL IDENTIFICADOR DEL CLIENTE PROPORCIONADO PARA GENERAR UN  REPORTE EN FORMATO PDF
	 *  MEDIANTE EL USO DE LA CLASE  MODELPDFCLIENTE.  UNA VEZ QUE EL REPORTE HA SIDO GENERADO, SE MUESTRA UN MENSAJE DE CONFIRMACIÓN.
	 * @param idCliente
	 */
	private void generarReporteCliente(int idCliente) {

		ModelPDFCliente pdfGenerator = new ModelPDFCliente();
		pdfGenerator.generateClientPDF(idCliente, conexion, bdDao);
		JOptionPane.showMessageDialog(this, "Reporte del cliente generado exitosamente.");
	}
}
