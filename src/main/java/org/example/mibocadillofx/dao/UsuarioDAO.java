package org.example.mibocadillofx.dao;

import org.example.mibocadillofx.model.Usuario;
import org.example.mibocadillofx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioDAO {

    public Usuario getUsuarioByMail(String mail) {
        Transaction transaction = null;
        Usuario usuario = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            usuario = session.get(Usuario.class, mail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return usuario;
    }
}
