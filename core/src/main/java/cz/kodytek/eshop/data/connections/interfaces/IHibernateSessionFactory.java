package cz.kodytek.eshop.data.connections.interfaces;

import cz.kodytek.eshop.data.connections.OpenedSessionFunction;

import javax.persistence.PersistenceException;

public interface IHibernateSessionFactory {
    void createSession(OpenedSessionFunction fn) throws PersistenceException;
}
