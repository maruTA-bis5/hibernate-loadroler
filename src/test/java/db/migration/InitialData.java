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
package db.migration;

import java.util.HashSet;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.bis5.hibernate.loadroler.entity.User;
import net.bis5.hibernate.loadroler.entity.UserAttr;
import net.bis5.hibernate.loadroler.entity.Voucher;
import net.bis5.hibernate.loadroler.entity.VoucherType;

public class InitialData {

    public void migrate(Session session) throws Exception {
        Transaction tx = session.beginTransaction();
        try {
            long userIdSeq = 1L;
            long userAttrIdSeq = 1L;

            User user1 = createUser(session, userIdSeq++, "user1");
            user1.getAttrs().add(createAttr(userAttrIdSeq++, userIdSeq - 1, "user1ext1"));
            user1.getAttrs().add(createAttr(userAttrIdSeq++, userIdSeq - 1, "user1ext2"));

            User user2 = createUser(session, userIdSeq++, "user2");
            user2.getAttrs().add(createAttr(userAttrIdSeq++, userIdSeq - 1, "user2ext1"));
            user2.getAttrs().add(createAttr(userAttrIdSeq++, userIdSeq - 1, "user2ext2"));

            long voucherTypeIdSeq = 1L;
            long voucherIdSeq = 1L;

            VoucherType voucherType = createVoucherType(session, voucherTypeIdSeq++, "typeA");
            createVoucher(session, voucherType, voucherIdSeq++, "Voucher0001");
            createVoucher(session, voucherType, voucherIdSeq++, "Voucher0002");

        } catch (Exception ex) {
            tx.rollback();
        } finally {
            if (tx.isActive()) {
                session.flush();
                tx.commit();
            }
        }
    }

    private Voucher createVoucher(Session session, VoucherType voucherType, long id, String voucherCode) {
        Voucher voucher = new Voucher();
        voucher.setVoucherId(id);
        voucher.setVoucherType(voucherType);
        voucher.setVoucherCode(voucherCode);
        session.persist(voucher);
        return voucher;
    }

    private VoucherType createVoucherType(Session session, long id, String typeCode) {
        VoucherType voucherType = new VoucherType();
        voucherType.setVoucherTypeId(id);
        voucherType.setVoucherTypeCode(typeCode);
        session.persist(voucherType);
        return voucherType;
    }

    private UserAttr createAttr(long userAttrId, long userId, String info) {
        UserAttr attr = new UserAttr();
        attr.setUserAttrId(userAttrId);
        attr.setUserId(userId);
        attr.setExtensionInfo(info);
        return attr;
    }

    private User createUser(Session session, long id, String name) {
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setAttrs(new HashSet<UserAttr>());
        session.persist(user);
        return user;
    }

}