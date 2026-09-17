package ar.edu.utn.frba.dds.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerHelper {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();
    
    static {
        emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    }
    
    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
        }
        return em;
    }

    public static void beginTransaction() { //Abre la transaccion a la abse de datos, empieza a escuhchar
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
    }

    public static void commit() { //Confirma los cambios, toma las ehechas en memoria y hace las ordenes sql necesarias. 
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.commit();
        }
    }

    public static void rollback() { //Si hay un error ahce que no se hagan lso cambniso para que noq uede abierrta. 
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    public static void closeEntityManager() { //Permite que todas las clases y repositorios, compartan la misma conexión dentro del mismo hilo, evitando consultas concurrentes 
        EntityManager em = threadLocal.get();
        if (em != null && em.isOpen()) {
            em.close();
            threadLocal.remove();
        }
    }
}

