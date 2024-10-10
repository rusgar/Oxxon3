package com.erc.model;


/**
 * CON ESTA CLASE RECOGEMOS Y ENLEZAMOS LAS TABLAS DE TRABAJADORES CON LAS SALIDAS
 */
public class tablaTrabajadoresSalidas {
    private int id, idTrabajador, idSalida;
    
 // GETTERES Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(int idSalida) {
        this.idSalida = idSalida;
    }

    
 // CONSTRUCTORES INICIALIZADOS
    public tablaTrabajadoresSalidas() {
        super();
        this.id = 0;
        this.idTrabajador = 0;
        this.idSalida = 0;
    }

  /**
   * CONSTRUCTOR PARAMETRIZADO QUE INICIALIZA LOS ATRIBUTOS CON LOS VALROES ESPECIFICOS 
   * @param id
   * @param idTrabajador
   * @param idSalida
   */
    public tablaTrabajadoresSalidas(int id, int idTrabajador, int idSalida) {
        super();
        this.id = id;
        this.idTrabajador = idTrabajador;
        this.idSalida = idSalida;
    }

    
    @Override
    public String toString() {
        return "tablaTrabajadoresSalidas [id=" + id + ", idTrabajador=" + idTrabajador + ", idSalida=" + idSalida + "]";
    }

	
}
