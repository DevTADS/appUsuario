package com.tcc.appusuario.Modelos;

import java.util.List;

public class Reporte {
    private int id_reporte;
    private String descripcion;
    private List<String> imagenes;
    private String hora;
    private String fecha;
    private String geolocalizacion;
    private int id_usuario;
    private String status;
    private String prioridad;

    private String division;

    public Reporte(int id_reporte, String descripcion, List<String> imagenes, String hora, String fecha, String geolocalizacion, int id_usuario, String status, String prioridad, String division) {
        this.id_reporte = id_reporte;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.hora = hora;
        this.fecha = fecha;
        this.geolocalizacion = geolocalizacion;
        this.id_usuario = id_usuario;
        this.status = status;
        this.prioridad = prioridad;
        this.division = division;
    }

    public int getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(int id_reporte) {
        this.id_reporte = id_reporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGeolocalizacion() {
        return geolocalizacion;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }


    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
