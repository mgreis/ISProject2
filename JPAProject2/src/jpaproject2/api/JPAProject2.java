/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.api;

import jpaproject2.entities.MusicFileEntity;
import jpaproject2.entities.UserEntity;
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

        ArrayList<UserEntity> userList = new ArrayList();

        
        userList.add(new UserEntity("jeremy@pearljam.com", "ten"));

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("JPAPROJECT2PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (UserEntity user : userList) {
            em.persist(user);
        }
        tx.commit();
        //getMusics(em);
    }

    public static void getMusics(EntityManager em) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<MusicFileEntity> query = builder.createQuery(MusicFileEntity.class);

        // build query
        final Root<MusicFileEntity> musics = query.from(MusicFileEntity.class);

        //executes the query
        query.select(musics);
        TypedQuery<MusicFileEntity> q = em.createQuery(query);
        List<MusicFileEntity> allMusics = q.getResultList();

        //prints results
        for (MusicFileEntity m : allMusics) {
            //System.out.println(m.getName() + " : " + m.getFilename());
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
