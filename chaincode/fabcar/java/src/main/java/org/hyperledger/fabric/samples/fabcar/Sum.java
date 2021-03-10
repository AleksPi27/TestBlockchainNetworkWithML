/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;


import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Sum {

    @Property()
    private final String firstOperand;

    @Property()
    private final String secondOperand;

    @Property()
    private String result;

    public String getFirstOperand() {
        return firstOperand;
    }

    public String getSecondOperand() {
        return secondOperand;
    }

    public String getResult() {
        return result;
    }

    public Sum(@JsonProperty("firstOperand") final String firstOperand,
               @JsonProperty("secondOperand") final String secondOperand,
               @JsonProperty("result") final String result){
        this.firstOperand=firstOperand;
        this.secondOperand=secondOperand;
        this.result=result;
    }

    public Sum(final String firstOperand,
               final String secondOperand){
        this.firstOperand=firstOperand;
        this.secondOperand=secondOperand;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [firstOperand=" + firstOperand + ", secondOperand="
                + secondOperand + ", result=" + result + "]";
    }
}
