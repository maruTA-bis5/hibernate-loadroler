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

import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.property.access.internal.PropertyAccessStrategyBackRefImpl;
import org.hibernate.tuple.NonIdentifierAttribute;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

import net.bis5.hibernate.loadroler.RoleHolders.RoleHolder;

public class RoleHandler implements PostLoadEventListener {

    @Override
    public void onPostLoad(PostLoadEvent event) {
        if (event.getEntity() == null) {
            return;
        }
        Object entity = event.getEntity();

        PersistenceContext context = event.getSession().getPersistenceContext();
        EntityEntry entry = context.getEntry(entity);

        EntityPersister persister = entry.getPersister();
        EntityMetamodel metamodel = persister.getEntityMetamodel();
        NonIdentifierAttribute[] properties = metamodel.getProperties();
        Object[] loadedState = entry.getLoadedState();

        RoleHolder holder = RoleHolders.holderForSession(event.getSession());

        boolean needApply = false;
        for (int i = 0; i < loadedState.length; i++) {
            NonIdentifierAttribute property = properties[i];
            String role = entity.getClass().getCanonicalName() + "." + property.getName();
            Object value = loadedState[i];
            if (value != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                Type type = persister.getPropertyType(property.getName());
                boolean needExclude = type.isCollectionType() || type instanceof ManyToOneType;

                if (needExclude) {
                    if (holder.contains(role)) {
                        if (type.isCollectionType()) {
                            AbstractPersistentCollection collection = (AbstractPersistentCollection) value;
                            collection.forceInitialization();
                            loadedState[i] = collection;
                        }
                    } else {
                        loadedState[i] = null;
                    }
                    needApply = true;
                }
            }
        }
        if (needApply) {
            persister.setPropertyValues(entity, loadedState);
        }
    }
}