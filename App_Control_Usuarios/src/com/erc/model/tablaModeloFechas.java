package com.erc.model;


import java.time.LocalDate;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class tablaModeloFechas extends DefaultTableModel {
    private static final String[] COLUMN_NAMES = {
        "Fecha Tarea", "ID", "Puesto", "Localidad", "Nombre Usuario", 
        "Enlace Google Maps", "Descripción", "Es instalación", "Es incidencia"
    };

    public static String[] getColumnNames() {
		return COLUMN_NAMES;
	}


	public tablaModeloFechas(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		// TODO Auto-generated constructor stub
	}


	public tablaModeloFechas(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}


	public tablaModeloFechas(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}


	public tablaModeloFechas(Vector<?> columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}


	public tablaModeloFechas(Vector<? extends Vector> data, Vector<?> columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}


	// INCIALIZAMOS A CERO LAS STATIC FINALES
	public tablaModeloFechas() {
        super(COLUMN_NAMES, 0); 
    }
    
 
    public void addFecha(
    	LocalDate fechaTarea,       
        int id,
        String puesto,
        String localidad,
        String nombreUsuario,
        String enlaceGoogleMaps,
        String descripcion,
        boolean esInstalacion,
        boolean esIncidencia )
    {     
    	// CONVERSION DEL TRUE FALSE, PARA QUE INCLUYA SI O NO
        String instalacion = esInstalacion ? "Sí" : "No";
        String incidencia = esIncidencia ? "Sí" : "No";
        
        addRow(new Object[]{fechaTarea, id, puesto, localidad, nombreUsuario, 
                            enlaceGoogleMaps, descripcion, instalacion, incidencia});
    }
}

