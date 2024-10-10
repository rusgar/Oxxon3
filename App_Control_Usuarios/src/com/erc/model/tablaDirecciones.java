package com.erc.model;

/**
 * CON ESTA CLASE RECOGEMOS LA INFORMACIÓN DE UNA DIRECCIÓN, 
 * INCLUYENDO DETALLES COMO DIRECCIÓN, CÓDIGO POSTAL, LOCALIDAD,
 * LATITUD, LONGITUD  Y EL ID DEL CLIENTE ASOCIADO.
 */
public class tablaDirecciones {

	
	// ATRIBUTOS
    private int id;
    private String direccion;
    private String codigoPostal;
    private String localidad;
    private double latitud;
    private double longitud;
    private int idCliente;

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    // CONSTRUCTORES INICIALIZADOS
    public tablaDirecciones() {
        this.id = 0;
        this.direccion = "";
        this.codigoPostal = "";
        this.localidad = "";
        this.latitud = 0.0;
        this.longitud = 0.0;
        this.idCliente = 0;
    }
    
    /**
     * CONSTRUCTOR QUE INICIALIZA LOS ATRIBUTOS CON LOS VALROES ESPECIFICOS
     * @param id
     * @param direccion
     * @param codigoPostal
     * @param localidad
     * @param latitud
     * @param longitud
     * @param idCliente
     */

    public tablaDirecciones(int id, String direccion, String codigoPostal, String localidad, double latitud, double longitud, int idCliente) {
        this.id = id;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "TablaDirecciones [id=" + id + ", direccion=" + direccion + ", codigoPostal=" + codigoPostal
                + ", localidad=" + localidad + ", latitud=" + latitud + ", longitud=" + longitud
                + ", idCliente=" + idCliente + "]";
    }
}
