package org.example.mibocadillofx.dao;

import org.example.mibocadillofx.model.Alumno;
import org.example.mibocadillofx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class AlumnoDAO {

    public Alumno getAlumnoByUserMail(String mail) {
        Alumno alumno = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Alumno> query = session.createQuery("FROM Alumno WHERE idUsuario = :mail", Alumno.class);
            query.setParameter("mail", mail);
            alumno = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alumno;
    }
}
