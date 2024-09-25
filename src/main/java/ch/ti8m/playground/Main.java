package ch.ti8m.playground;

import ch.ti8m.playground.model.Bericht;
import ch.ti8m.playground.model.Entwurf;
import jakarta.persistence.*;

import java.util.List;
import java.util.function.Consumer;

public class Main {

    static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {

        initManager();

        if (false) {
            inTransaction(entityManager -> {
                List<Bericht> berichte = entityManager.createQuery("SELECT a FROM Bericht a", Bericht.class).getResultList();
                List<Entwurf> entwuerfe = entityManager.createQuery("SELECT a FROM Entwurf a", Entwurf.class).getResultList();

                berichte.forEach(entityManager::remove);
                entwuerfe.forEach(entityManager::remove);
            });
            return;
        }

        Entwurf entwurf = new Entwurf();
        // save Entwurf and Bericht
        inTransaction(entityManager -> {
            entityManager.persist(entwurf);
            entwurf.setBericht(new Bericht());
        });

        Bericht oldBericht = entwurf.getBericht();
        entwurf.setBericht(new Bericht());

        // add new Bericht, delete(archive) old bericht
        inTransaction(entityManager -> {
            Entwurf mergedEntwurf = entityManager.merge(entwurf);
            Bericht oldBerichtToDelete = entityManager.find(Bericht.class, oldBericht.getBerichtId());

            if (mergedEntwurf == oldBerichtToDelete.getEntwurf()) {
                System.out.println("Equal!");
            }

            entityManager.remove(oldBerichtToDelete);
        });

    }

    private static void initManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    static void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try (entityManager) {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}