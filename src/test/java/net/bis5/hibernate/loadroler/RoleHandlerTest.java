/* 
 * Hibernate LoadRoler
 * Copyright (C) 2019 Takayuki Maruyama
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.bis5.hibernate.loadroler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;

import org.h2.Driver;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.migration.InitialData;
import net.bis5.hibernate.loadroler.entity.User;
import net.bis5.hibernate.loadroler.entity.UserAttr;
import net.bis5.hibernate.loadroler.entity.Voucher;
import net.bis5.hibernate.loadroler.entity.VoucherType;

public class RoleHandlerTest {
    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void initDatabase() {
        sessionFactory = new Configuration().setProperty("hibernate.hbm2ddl.auto", "update")
                .setProperty("hibernate.dialect", PostgreSQL10Dialect.class.getCanonicalName())
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test;MODE=POSTGRESQL;DB_CLOSE_ON_EXIT=FALSE")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.connection.driver_class", Driver.class.getCanonicalName())
                .addAnnotatedClass(User.class).addAnnotatedClass(UserAttr.class).addAnnotatedClass(Voucher.class)
                .addAnnotatedClass(VoucherType.class).buildSessionFactory();
    }

    @Before
    public void initialize() throws Exception {
        Session session = sessionFactory.openSession();
        new InitialData().migrate(session);
        session.close();
    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.createQuery("DELETE FROM UserAttr").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Voucher").executeUpdate();
            session.createQuery("DELETE FROM VoucherType").executeUpdate();
        } finally {
            if (tx.isActive()) {
                session.flush();
                tx.commit();
            } else {
                tx.rollback();
            }
        }
        session.close();
    }

    @AfterClass
    public static void cleanDatabase() {
        sessionFactory.close();
    }

    @Test
    public void oneToManyHasRole() {
        Session session = sessionFactory.openSession();
        RoleHolders.holderForSession(session).addLoadRole(User.class.getName(), "attrs");

        Criteria criteria = session.createCriteria(User.class);
        @SuppressWarnings("unchecked")
        List<User> resultList = criteria.list();
        assertFalse(resultList.isEmpty());

        session.clear();
        session.close();
        RoleHolders.cleanup(session);

        for (User user : resultList) {
            Set<UserAttr> attrs = user.getAttrs();
            assertNotNull(attrs);
            UserAttr attr = attrs.iterator().next();
            assertNotNull(attr);
        }
    }

    @Test
    public void oneToManyNoRoleSessionActive() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(User.class);
        @SuppressWarnings("unchecked")
        List<User> resultList = criteria.list();
        assertFalse(resultList.isEmpty());

        for (User user : resultList) {
            Set<UserAttr> attrs = user.getAttrs();
            assertNull(attrs);
        }

        session.close();
    }
    @Test
    public void oneToManyNoRolesSessionNotActive() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(User.class);
        @SuppressWarnings("unchecked")
        List<User> resultList = criteria.list();
        assertFalse(resultList.isEmpty());
        session.clear();
        session.close();
        RoleHolders.cleanup(session);

        for (User user : resultList) {
            Set<UserAttr> attrs = user.getAttrs();
            assertNull(attrs);
        }
    }

    @Test
    public void manyToOneHasRole() {
        Session session = sessionFactory.openSession();
        RoleHolders.holderForSession(session).addLoadRole(Voucher.class.getName(), "voucherType");
        Criteria criteria = session.createCriteria(Voucher.class);
        
        @SuppressWarnings("unchecked")
        List<Voucher> vouchers = criteria.list();
        assertFalse(vouchers.isEmpty());
        session.clear();
        session.close();
        RoleHolders.cleanup(session);

        for (Voucher voucher : vouchers) {
            assertNotNull(voucher.getVoucherType());
        }
    }

    @Test
    public void manyToOneNoRole() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Voucher.class);
        
        @SuppressWarnings("unchecked")
        List<Voucher> vouchers = criteria.list();
        assertFalse(vouchers.isEmpty());
        session.clear();
        session.close();
        RoleHolders.cleanup(session);

        for (Voucher voucher : vouchers) {
            assertNull(voucher.getVoucherType());
        }
    }
}
