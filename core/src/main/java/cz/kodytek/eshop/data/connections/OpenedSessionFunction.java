package cz.kodytek.eshop.data.connections;

import org.hibernate.Session;

import javax.persistence.PersistenceException;

public interface OpenedSessionFunction {
    void invoke(Session session) throws PersistenceException;
}
