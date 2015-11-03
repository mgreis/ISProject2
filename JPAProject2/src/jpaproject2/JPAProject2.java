/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2;

import entities.MusicEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author Mário
 */
public class JPAProject2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList<MusicEntity> musicList = new ArrayList();

        musicList.add(new MusicEntity("Smells Like Teen Spirits", "Nevermind01.mp3"));
        musicList.add(new MusicEntity("Jeremy", "Ten06.mp3"));

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("JPAPROJECT2PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (MusicEntity music : musicList) {
            em.persist(music);
        }
        tx.commit();
        getMusics(em);
    }

    public static void getMusics(EntityManager em) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<MusicEntity> query = builder.createQuery(MusicEntity.class);

        // build query
        final Root<MusicEntity> musics = query.from(MusicEntity.class);

        //executes the query
        query.select(musics);
        TypedQuery<MusicEntity> q = em.createQuery(query);
        List<MusicEntity> allMusics = q.getResultList();

        //prints results
        for (MusicEntity m : allMusics) {
            System.out.println(m.getName() + " : " + m.getFilename());
        }
    }
}

        /**
         * Query database and returns players by position
         *
         * @param em
         * @param position
         */
        /*
         public static void getPlayersByPosition(EntityManager em, String position) {
         final CriteriaBuilder builder = em.getCriteriaBuilder();
         final CriteriaQuery<FootballPlayerEntity> query = builder.createQuery(FootballPlayerEntity.class);

         // build query
         final Root<FootballPlayerEntity> players = query.from(FootballPlayerEntity.class);
         query.where(builder.like(players.get("position"), position));

         //executes the query
         final List<FootballPlayerEntity> allPlayers = em.createQuery(query).getResultList();

         //prints results
         for (FootballPlayerEntity f : allPlayers) {
         System.out.println(f.getName());
         }
         }
         /**
         * Queries database and returns players taller than
         * @param em
         * @param height 
     
         public static void getPlayersTallerThan(EntityManager em, Double height) {
         final CriteriaBuilder builder = em.getCriteriaBuilder();
         final CriteriaQuery<FootballPlayerEntity> query = builder.createQuery(FootballPlayerEntity.class);

         // build query
         final Root<FootballPlayerEntity> players = query.from(FootballPlayerEntity.class);
         query.where(builder.greaterThan(players.get("height"), height));

         //executes the query
         final List<FootballPlayerEntity> allPlayers = em.createQuery(query).getResultList();

         //prints results
         for (FootballPlayerEntity f : allPlayers) {
         System.out.println(f.getName());
         }
         }
        
                
    }
                */
