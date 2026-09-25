package ar.edu.utn.frba.dds.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerHelper {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();
    
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    public static void beginTransaction() { //Abre la transaccion a la abse de datos, empieza a escuhchar
        EntityTransaction tx = getEntityManager().getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
    }

    public static void commit() { //Confirma los cambios, toma las ehechas en memoria y hace las ordenes sql necesarias. 
        EntityTransaction tx = getEntityManager().getTransaction();
        if (tx.isActive()) {
            tx.commit();
        }
    }

    public static void rollback() { //Si hay un error ahce que no se hagan lso cambniso para que noq uede abierrta. 
        EntityTransaction tx = getEntityManager().getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    public static void closeEntityManager() { //Permite que todas las clases y repositorios, compartan la misma conexión dentro del mismo hilo, evitando consultas concurrentes 
        EntityManager em = threadLocal.get();
        if (em != null && em.isOpen()) {
            em.close();
        }
        threadLocal.remove();
    }
    public static void cerrarFactory() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}

