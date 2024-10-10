package com.erc.model;

import java.time.LocalDate;

/**
 * CON ESTA CLASE RECOGEMOS LA INFORMAICON DE LA TABLA SALIDAS, ICLUYENDO LOS ATRIBUTOS DE
 * TAREA, INSTALACIONES,INCIDENCIAS,SOLUCION,COSTECLIENTES, DESCRIPCION, FECHATAREA
 */
public class tablaSalidas {

	// ATRIBUTOS
    private int id, idCliente, idDireccion, idTrabajador;
    private String tarea;
    private boolean instalaciones;
    private boolean incidencias;
    private boolean solucion;
    private double costeCliente;  
    private String descripcion;
    private LocalDate fechaTarea;

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public boolean isInstalaciones() {
        return instalaciones;
    }

    public void setInstalaciones(boolean instalaciones) {
        this.instalaciones = instalaciones;
    }

    public boolean isIncidencias() {
        return incidencias;
    }

    public void setIncidencias(boolean incidencias) {
        this.incidencias = incidencias;
    }

    public boolean isSolucion() {
        return solucion;
    }

    public void setSolucion(boolean solucion) {
        this.solucion = solucion;
    }

    public double getCosteCliente() {
        return costeCliente;
    }

    public void setCosteCliente(double costeCliente) {
        this.costeCliente = costeCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaTarea() {
        return fechaTarea;
    }

    public void setFechaTarea(LocalDate fechaTarea) {
        this.fechaTarea = fechaTarea;
    }

    // CONSTRUCTORES INICIALIZADOS
    public tablaSalidas() {
        super();
        this.id = 0;
        this.idCliente = 0;
        this.idDireccion = 0;
        this.idTrabajador = 0;
        this.tarea = "";
        this.instalaciones = false;
        this.incidencias = false;
        this.solucion = false;
        this.costeCliente = 0.0;  
        this.descripcion = "";
        this.fechaTarea = LocalDate.now();
    }

    /**
     * CONSTRUCTOR QUE INICIALIZA LOS ATRIBUTOS CON LOS VALROES ESPECIFICOS
     * @param id
     * @param idCliente
     * @param idDireccion
     * @param idTrabajador
     * @param tarea
     * @param instalaciones
     * @param incidencias
     * @param solucion
     * @param costeCliente
     * @param descripcion
     * @param fechaTarea
     */
    public tablaSalidas(int id, int idCliente, int idDireccion, int idTrabajador, String tarea, boolean instalaciones,
                        boolean incidencias, boolean solucion, double costeCliente, String descripcion, LocalDate fechaTarea) {
        super();
        this.id = id;
        this.idCliente = idCliente;
        this.idDireccion = idDireccion;
        this.idTrabajador = idTrabajador;
        this.tarea = tarea;
        this.instalaciones = instalaciones;
        this.incidencias = incidencias;
        this.solucion = solucion;
        this.costeCliente = costeCliente;
        this.descripcion = descripcion;
        this.fechaTarea = fechaTarea;
    }

    @Override
    public String toString() {
        return "tablaSalidas [id=" + id + ", idCliente=" + idCliente + ", idDireccion=" + idDireccion
                + ", idTrabajador=" + idTrabajador + ", tarea=" + tarea + ", instalaciones=" + instalaciones
                + ", incidencias=" + incidencias + ", solucion=" + solucion + ", costeCliente=" + costeCliente
                + ", descripcion=" + descripcion + ", fechaTarea=" + fechaTarea + "]";
    }
}
