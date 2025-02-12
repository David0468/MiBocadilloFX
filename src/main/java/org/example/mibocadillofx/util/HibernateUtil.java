package org.example.mibocadillofx.util;

import org.example.mibocadillofx.model.Usuario;
import org.example.mibocadillofx.model.Alumno;
import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.model.Pedido;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // Registra las entidades
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Alumno.class);
            configuration.addAnnotatedClass(Bocadillo.class);
            configuration.addAnnotatedClass(Pedido.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al inicializar Hibernate: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
