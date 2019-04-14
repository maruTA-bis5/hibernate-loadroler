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
import javax.persistence.ManyToOne;

@Entity
public class Voucher {
    @Id
    private Long voucherId;
    @ManyToOne
    private VoucherType voucherType;
    private String voucherCode;

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherCode() {
        return voucherCode;
    }
}