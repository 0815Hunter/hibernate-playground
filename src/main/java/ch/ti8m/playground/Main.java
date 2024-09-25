package ch.ti8m.playground;

import ch.ti8m.playground.model.Bericht;
import ch.ti8m.playground.model.Entwurf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.function.Consumer;

public class Main {

    static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {

        initManager();

        Entwurf entwurf = new Entwurf();
        Bericht bericht1 = new Bericht();
        Bericht bericht2 = new Bericht();

        inTransaction(entityManager -> {
            List<Bericht> berichte = entityManager.createQuery("SELECT a FROM Bericht a", Bericht.class).getResultList();
            List<Entwurf> entwuerfe = entityManager.createQuery("SELECT a FROM Entwurf a", Entwurf.class).getResultList();

            berichte.forEach(entityManager::remove);
            entwuerfe.forEach(entityManager::remove);
        });

        inTransaction(entityManager -> {
            entityManager.persist(entwurf);
            entwurf.setBericht(bericht1);
        });

        Bericht oldBericht = entwurf.getBericht();
        oldBericht.setArchived(true);

        entwurf.setBericht(bericht2);

        inTransaction(entityManager -> {
            entityManager.merge(entwurf);
            entityManager.merge(oldBericht);
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