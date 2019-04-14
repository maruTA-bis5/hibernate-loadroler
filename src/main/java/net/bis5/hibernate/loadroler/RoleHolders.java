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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.hibernate.Session;

public class RoleHolders {

    private static final Map<Session, RoleHolder> instances = Collections.synchronizedMap(new WeakHashMap<Session, RoleHolder>());

    public static RoleHolder holderForSession(Session session) {
        // Java 8以降をターゲットにするならsynchronizedをやめてcomputeIfAbsentにする
        synchronized (instances) {
            if (instances.containsKey(session)) {
                return instances.get(session);
            }
            RoleHolder roleHolder = new RoleHolder();
            instances.put(session, roleHolder);
            return roleHolder;
        }
    }

    public static void cleanup(Session session) {
        instances.remove(session);
    }

    public static class RoleHolder {

        private Set<String> roles = new HashSet<String>();

        public void addLoadRole(String entityName, String associatePath) {
            roles.add(entityName + "." + associatePath);
        }

        public void clearLoadRoles() {
            roles.clear();
        }

        public boolean contains(String entityName, String associatePath) {
            return contains(entityName + "." + associatePath);
        }

        public boolean contains(String role) {
            return roles.contains(role);
        }
    }

}