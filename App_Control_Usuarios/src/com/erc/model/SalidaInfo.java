package com.erc.model;


/**
 *  CON ESTA CLASE REPRESENTAMOS LA INFORMACIÓN DE UNA SALIDA, INCLUYENDO LOS ATRIBUTOS
 *   COMO EL NONBRE, LOCALIDAD, NOMBRE DEL USUARIO Y UN ENLACE A GOOGLE MAPS.
 */
public class SalidaInfo {

	// ATRIBUTOS
    private tablaSalidas salida;
    private String tarea;
    private String nombre;
    private String localidad;
    private String nombreUsuario;
    private String enlaceGoogleMaps;

    
    
 // GETTERS Y SETTERS


	 public String getEnlaceGoogleMaps() {
	        return enlaceGoogleMaps;
	    }


	 public void setEnlaceGoogleMaps(String enlaceGoogleMaps) {
		 this.enlaceGoogleMaps = enlaceGoogleMaps;
		}

    public tablaSalidas getSalida() {
        return salida;
    }

    public void setSalida(tablaSalidas salida) {
        this.salida = salida;
        this.tarea = salida.getTarea();
    }
    public String getTarea() {
        return tarea;
    }
    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

   
  /**
   * CONSTRUCTOR PARA CREAR UN INSTANCIA DE LA CLASE
   * @param salida
   * @param nombre
   * @param localidad
   * @param nombreUsuario
   * @param enlaceGoogleMaps
   */
    public SalidaInfo(tablaSalidas salida, String tarea, String nombre, String localidad, String nombreUsuario, String enlaceGoogleMaps) {
        this.salida = salida;
        this.tarea = tarea;
        this.nombre = nombre;
        this.localidad = localidad;
        this.nombreUsuario = nombreUsuario;
        this.enlaceGoogleMaps = enlaceGoogleMaps;
    }


	
}
