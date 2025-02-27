package org.example.mibocadillofx.dao;

import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BocadilloDAO {

    public Bocadillo getBocadilloDelDia(String tipo) {
        Bocadillo bocadillo = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            LocalDate today = LocalDate.now();
            // Obtener el día actual en español (por ejemplo, "martes", "miércoles", etc.)
            String dayInSpanish = today.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase();
            System.out.println("Día actual (en español): " + dayInSpanish);
            System.out.println("Buscando bocadillo para tipo: " + tipo);

            Query<Bocadillo> query = session.createQuery(
                    "FROM Bocadillo WHERE tipo_bocadillo = :tipo AND dia = :dia", Bocadillo.class);
            query.setParameter("tipo", tipo);
            query.setParameter("dia", dayInSpanish);
            bocadillo = query.uniqueResult();

            if (bocadillo != null) {
                // Consultar la tabla bocadillo_alergeno para obtener los nombres de los alérgenos asociados
                @SuppressWarnings("unchecked")
                List<String> allergens = session.createNativeQuery(
                                "SELECT id_alergeno FROM bocadillo_alergeno WHERE id_bocadillo = :bocadilloName")
                        .setParameter("bocadilloName", bocadillo.getNombre())
                        .getResultList();
                if (allergens != null && !allergens.isEmpty()) {
                    String allergenStr = allergens.stream().collect(Collectors.joining(", "));
                    bocadillo.setAlergenos(allergenStr);
                } else {
                    bocadillo.setAlergenos("Sin alérgenos");
                }
                System.out.println("Bocadillo encontrado: " + bocadillo.getNombre());
                System.out.println("Alérgenos: " + bocadillo.getAlergenos());
            } else {
                System.out.println("No se encontró bocadillo para tipo " + tipo + " y día " + dayInSpanish);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return bocadillo;
    }

    public List<Bocadillo> obtenerTodosLosBocadillos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Bocadillo WHERE fecha_baja IS NULL", Bocadillo.class).list(); // Ahora incluye inactivos
        }
    }

    public List<Bocadillo> obtenerTodosLosBocadillosAdmin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Bocadillo", Bocadillo.class).list(); // Ahora incluye inactivos
        }
    }


    public boolean agregarBocadillo(Bocadillo bocadillo) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(bocadillo);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public void eliminarBocadillo(String nombre) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Bocadillo bocadillo = session.get(Bocadillo.class, nombre);
            if (bocadillo != null) {
                session.delete(bocadillo);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    public boolean actualizarBocadillo(Bocadillo bocadillo) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(bocadillo);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    public void cambiarEstadoBocadillo(Bocadillo bocadillo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            if (bocadillo.getFecha_baja() == null) {
                bocadillo.setFecha_baja(String.valueOf(LocalDate.now())); // Marcar como inactivo
            } else {
                bocadillo.setFecha_baja(null); // Reactivar
            }

            session.update(bocadillo);
            tx.commit();
        }
    }


}
