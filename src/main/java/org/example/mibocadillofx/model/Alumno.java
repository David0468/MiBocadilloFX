package org.example.mibocadillofx.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    private String nombre; // Clave primaria

    // Otros campos según la tabla
    @Column(name = "tipo_alumno")
    private String tipoAlumno;

    @Column(name = "id_usuario")
    private String idUsuario; // Este es el correo del alumno

    @Column(name = "id_curso") // Asegura que este campo esté bien mapeado
    private String idCurso;

    // Puedes incluir más campos según sea necesario (fecha_baja, id_curso, etc.)

    // Constructor sin argumentos (requerido por Hibernate)
    public Alumno() {
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoAlumno() {
        return tipoAlumno;
    }
    public void setTipoAlumno(String tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }
}
