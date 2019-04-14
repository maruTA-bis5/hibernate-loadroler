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
package net.bis5.hibernate.loadroler.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAttr {
    @Id
    private Long userAttrId;
    private Long userId;
    private String extensionInfo;

    public void setUserAttrId(Long userAttrId) {
        this.userAttrId = userAttrId;
    }

    public Long getUserAttrId() {
        return userAttrId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setExtensionInfo(String extensionInfo) {
        this.extensionInfo = extensionInfo;
    }

    public String getExtensionInfo() {
        return extensionInfo;
    }
}