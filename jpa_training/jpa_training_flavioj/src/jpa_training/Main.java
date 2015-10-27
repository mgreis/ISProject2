/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa_training;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * @author Flávio J. Saraiva
 */
public class Main {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        try {
            emf = Persistence.createEntityManagerFactory("JpaTraining");
            em = emf.createEntityManager();
            Main app = new Main();

            // create
            final Team t1 = new Team("Alunos", "DEI", "Prof");
            final Player p1 = new Player("Flávio", new Date(), 170.0, "Ataque", t1);
            final Player p2 = new Player("Pedro", new Date(), 160.0, "Defesa", t1);
            final Player p3 = new Player("Liana", new Date(), 200.0, "Guarda-redes", t1);
            final Player p4 = new Player("Mário", new Date(), 150.0, "Ataque", null);

            System.out.println("\n[ Add everything ]");
            final Object[] objs = new Object[]{t1, p1, p2, p3, p4};
            app.persistAll(objs);
            for (Object obj : objs) {
                System.out.println(obj);
            }
            app.detachAll(objs); // get the rest from database

            System.out.println("\n[ List players in position Ataque ]");
            final String position = "Ataque";
            for (Player player : app.getPlayersInPosition(position)) {
                System.out.println(player);
            }

            System.out.println("\n[ List players players taller than 160.0 ]");
            final double height = 160.0;
            for (Player player : app.getPlayersTallerThan(height)) {
                System.out.println(player);
            }

            System.out.println("\n[ List players in team 1 ]");
            for (Player player : app.getTeam(t1.getId()).getPlayers()) {
                System.out.println(player);
            }

            System.out.println("\n[ Update height to 999.0 ]");
            app.updatePlayerHeight(p1.getId(), 999.0);
            for (Player player : app.getPlayers()) {
                System.out.println(player);
            }

            System.out.println("\n[ Delete player ]");
            app.deletePlayer(p2.getId());
            for (Player player : app.getPlayers()) {
                System.out.println(player);
            }
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    /* Method to UPDATE height for a player */
    public void updatePlayerHeight(Long id, double height) {
        Player player = getPlayer(id);
        player.setHeight(height);
        merge(player);
    }

    /* Method to DELETE a player from the records */
    public void deletePlayer(Long id) {
        Player player = getPlayer(id);
        remove(player);
    }

    // get a player
    public Player getPlayer(Long id) {
        assert (id != null);
        final TypedQuery<Player> query = em.createQuery(
                "SELECT e FROM Player e WHERE id = :id",
                Player.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    // get a team
    public Team getTeam(Long id) {
        assert (id != null);
        final TypedQuery<Team> query = em.createQuery(
                "SELECT e FROM Team e WHERE id = :id",
                Team.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    // get players
    public List<Player> getPlayersInPosition(String position) {
        assert (position != null);
        final TypedQuery<Player> query = em.createQuery(
                "SELECT e FROM Player e WHERE position = :position",
                Player.class);
        query.setParameter("position", position);
        return query.getResultList();
    }

    // get players taller than
    public List<Player> getPlayersTallerThan(double height) {
        final TypedQuery<Player> query = em.createQuery(
                "SELECT e FROM Player e WHERE height > :height",
                Player.class);
        query.setParameter("height", height);
        return query.getResultList();
    }

    // get all players
    public List<Player> getPlayers() {
        final TypedQuery<Player> query = em.createQuery(
                "SELECT e FROM Player e",
                Player.class);
        return query.getResultList();
    }

    // update  a managed object
    public <T> T merge(T obj) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            obj = em.merge(obj);
        } finally {
            if (!tx.getRollbackOnly()) {
                tx.commit();
            }
        }
        return obj;
    }

    // persist all objects in a single transaction
    public void persistAll(Object[] objs) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Object obj : objs) {
                em.persist(obj);
                em.refresh(obj);
            }
        } finally {
            if (!tx.getRollbackOnly()) {
                tx.commit();
            }
        }
    }

    // detach all objects
    public void detachAll(Object[] objs) {
        for (Object obj : objs) {
            em.detach(obj);
        }
    }

// remove object
    public <T> T remove(T obj) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(obj);
        } finally {
            if (!tx.getRollbackOnly()) {
                tx.commit();
            }
        }
        return obj;
    }
}
