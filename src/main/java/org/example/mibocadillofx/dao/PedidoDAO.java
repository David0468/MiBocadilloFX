package org.example.mibocadillofx.dao;

import org.example.mibocadillofx.model.Pedido;
import org.example.mibocadillofx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PedidoDAO {

    // Método auxiliar para obtener el pedido del día para un alumno
    public Pedido getPedidoDelDia(String idAlumno) {
        Pedido pedido = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            LocalDate today = LocalDate.now();
            Query<Pedido> query = session.createQuery(
                    "FROM Pedido WHERE idAlumno = :idAlumno AND fechaPedido = :today", Pedido.class);
            query.setParameter("idAlumno", idAlumno);
            query.setParameter("today", today);
            pedido = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return pedido;
    }

    // Método modificado: si ya existe un pedido, lo elimina y crea uno nuevo
    public boolean createOrReplacePedido(String idAlumno, String idBocadillo, double precio) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LocalDate today = LocalDate.now();

            // Buscar si existe un pedido para el alumno en el día actual
            Pedido existingPedido = getPedidoDelDia(idAlumno);
            if (existingPedido != null) {
                session.delete(existingPedido);
                // Forzar flush para asegurar que se borre antes de insertar el nuevo
                session.flush();
            }

            // Crear el nuevo pedido
            Pedido newPedido = new Pedido();
            newPedido.setFechaPedido(today);
            newPedido.setIdAlumno(idAlumno);
            newPedido.setIdBocadillo(idBocadillo);
            newPedido.setPrecio(precio);

            session.save(newPedido);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // Método para cancelar el pedido (elimina el pedido existente)
    public boolean cancelPedido(String idAlumno) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Pedido pedido = getPedidoDelDia(idAlumno);
            if (pedido != null) {
                session.delete(pedido);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public void marcarComoEntregado(Pedido pedido) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            pedido.setFechaRecogida(LocalDate.now());
            session.update(pedido);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desmarcarComoEntregado(Pedido pedido) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            pedido.setFechaRecogida(null);
            session.update(pedido);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarPedido(Pedido pedido) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(pedido);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> obtenerPedidosPorFecha(LocalDate fecha) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pedido WHERE fechaPedido = :fecha", Pedido.class)
                    .setParameter("fecha", fecha)
                    .list();
        }
    }
    public List<String> obtenerCursosDisponibles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery("SELECT nombre FROM cursos WHERE fecha_baja IS NULL").list();
        }
    }
}
