/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

/**
 * CarQueryResult structure used for handling result of query
 *
 */
@DataType()
public class SumQueryResult {
    @Property()
    private final String key;

    @Property()
    private final Sum record;

    public SumQueryResult(@JsonProperty("Key") final String key, @JsonProperty("Record") final Sum record) {
        this.key = key;
        this.record = record;
    }

    public String getKey() {
        return key;
    }

    public Sum getRecord() {
        return record;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey(), this.getRecord());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        SumQueryResult second = (SumQueryResult) obj;

        Boolean keysAreEquals = this.getKey().equals(second.getKey());
        Boolean recordsAreEquals = this.getRecord().equals(second.getRecord());
        return keysAreEquals && recordsAreEquals;

    }

    @Override
    public String toString() {
        return "{\"Key\":\"" + key + "\"" + "\"Record\":{\"" + record + "}\"}";
    }
}
