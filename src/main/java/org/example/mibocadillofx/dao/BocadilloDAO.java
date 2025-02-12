package org.example.mibocadillofx.dao;

import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class BocadilloDAO {

    public Bocadillo getBocadilloDelDia(String tipo) {
        Bocadillo bocadillo = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            LocalDate today = LocalDate.now();
            String dayInSpanish = today.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase();
            System.out.println("Día actual (en español): " + dayInSpanish);
            System.out.println("Buscando bocadillo para tipo: " + tipo);

            Query<Bocadillo> query = session.createQuery(
                    "FROM Bocadillo WHERE tipo_bocadillo = :tipo AND dia = :dia", Bocadillo.class);
            query.setParameter("tipo", tipo);
            query.setParameter("dia", dayInSpanish);
            bocadillo = query.uniqueResult();

            if (bocadillo != null) {
                // Aquí asignamos los alérgenos de ejemplo; en producción, obtén la información real.
                bocadillo.setAlergenos("Gluten, Lactosa");
                System.out.println("Bocadillo encontrado: " + bocadillo.getNombre());
            } else {
                System.out.println("No se encontró bocadillo para " + tipo + " y día " + dayInSpanish);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return bocadillo;
    }
}
