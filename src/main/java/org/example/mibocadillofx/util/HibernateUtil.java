package org.example.mibocadillofx.util;

import org.example.mibocadillofx.model.Usuario;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Agrega esta línea para depurar la ruta de carga del archivo hibernate.cfg.xml
            System.out.println("Intentando cargar hibernate.cfg.xml desde: " + HibernateUtil.class.getResource("/hibernate.cfg.xml"));

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");  // Se asume que el archivo está en src/main/resources

            // Registra las clases mapeadas (en este ejemplo, Usuario)
            configuration.addAnnotatedClass(Usuario.class);

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
