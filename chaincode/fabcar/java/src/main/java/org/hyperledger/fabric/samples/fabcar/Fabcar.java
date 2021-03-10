/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Java implementation of the Fabric Car Contract described in the Writing Your
 * First Application tutorial
 */
@Contract(
        name = "FabCar",
        info = @Info(
                title = "FabCar contract",
                description = "The hyperledger ML contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "aleksandrpiliakin@gmail.com",
                        name = "Aleksandr Piliakin",
                        url = "https://hyperledger.example.com")))

@Default
public final class Fabcar implements ContractInterface {

    private final Genson genson = new Genson();

    private enum SummatorErrors {
        SUM_NOT_FOUND,
        SUM_ALREADY_EXISTS
    }

    private String sendRequest(String firstOperand, String secondOperand){
        RestTemplate restTemplate=new RestTemplate();
//        String urlOfMLService=System.getenv("ML_SERVICE_URL");
//        if (urlOfMLService==null){
//            urlOfMLService="localhost";
//        }
        ResponseEntity<String> response = restTemplate.getForEntity("http://104.199.97.44:9010/computateSum?firstOperand=" + firstOperand + "&secondOperand=" + secondOperand, String.class);
        System.out.println("Response is " + response);
        String responseString=response.toString();
        int firstIndexOfResult=responseString.indexOf("result");
        System.out.println(responseString.charAt(firstIndexOfResult+9));
        int indexOfQuote=responseString.indexOf("\"",firstIndexOfResult+9);
        System.out.println(indexOfQuote);
        String resultString=responseString.substring(firstIndexOfResult+9,indexOfQuote);
        System.out.println(resultString);
        Double resultDouble=Double.parseDouble(resultString);
        Double sumTruly=Double.parseDouble(firstOperand)+Double.parseDouble(secondOperand);
        boolean isResultIsClose=false;
        if (Math.abs(sumTruly-resultDouble)<0.3*sumTruly){
            isResultIsClose=true;
        }
        return String.valueOf(isResultIsClose);
    }

    @Transaction()
    public Sum querySum(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String sumState = stub.getStringState(key);

        if (sumState.isEmpty()) {
            String errorMessage = String.format("Sum %s doesn't exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SummatorErrors.SUM_NOT_FOUND.toString());
        }

        Sum sum = genson.deserialize(sumState, Sum.class);

        return sum;
    }

    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        String result1;
        result1=sendRequest(Double.valueOf(0.11111).toString(),Double.valueOf(0.274).toString());
        String[] sumData = {
                "{ \"firstOperand\": \"0.11111\", \"secondOperand\": \"0.274\", \"result\": \""+result1+"\" }",
        };

        for (int i = 0; i < sumData.length; i++) {
            String key = String.format("SUM%d", i);

            Sum sum = genson.deserialize(sumData[i], Sum.class);
            String sumState = genson.serialize(sum);
            stub.putStringState(key, sumState);
        }
    }

    @Transaction()
    public Sum calculateSum(final Context ctx, final String key, final Double firstOperand, final Double secondOperand) {
        ChaincodeStub stub = ctx.getStub();

        String sumState = stub.getStringState(key);
        if (!sumState.isEmpty()) {
            String errorMessage = String.format("Sum %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SummatorErrors.SUM_ALREADY_EXISTS.toString());
        }

        Sum sum = new Sum(String.valueOf(firstOperand), String.valueOf(secondOperand),sendRequest(String.valueOf(firstOperand),String.valueOf(secondOperand)));
        sumState = genson.serialize(sum);
        stub.putStringState(key, sumState);
        return sum;
    }

    @Transaction()
    public SumQueryResult[] queryAllSums(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "SUM0";
        final String endKey = "SUM999";
        List<SumQueryResult> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue singleResult : results) {
            Sum sum = genson.deserialize(singleResult.getStringValue(), Sum.class);
            queryResults.add(new SumQueryResult(singleResult.getKey(), sum));
        }

        SumQueryResult[] response = queryResults.toArray(new SumQueryResult[queryResults.size()]);

        return response;
    }

    @Transaction()
    public Sum changeFirstOperandOfSum(final Context ctx, final String key, final Double newFirstOperand) {
        ChaincodeStub stub = ctx.getStub();

        String sumState = stub.getStringState(key);

        if (sumState.isEmpty()) {
            String errorMessage = String.format("Sum %s doesn't exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SummatorErrors.SUM_NOT_FOUND.toString());
        }

        Sum sum = genson.deserialize(sumState, Sum.class);

        Sum newSum = new Sum(String.valueOf(newFirstOperand), String.valueOf(sum.getSecondOperand()),sendRequest(String.valueOf(newFirstOperand),String.valueOf(sum.getSecondOperand())));
        String newSumState= genson.serialize(newSum);
        stub.putStringState(key, newSumState);

        return newSum;
    }


    @Transaction()
    public Sum changeSecondOperandOfSum(final Context ctx, final String key, final Double newSecondOperand) {
        ChaincodeStub stub = ctx.getStub();

        String sumState = stub.getStringState(key);

        if (sumState.isEmpty()) {
            String errorMessage = String.format("Sum %s doesn't exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SummatorErrors.SUM_NOT_FOUND.toString());
        }

        Sum sum = genson.deserialize(sumState, Sum.class);

        Sum newSum = new Sum(String.valueOf(sum.getFirstOperand()), String.valueOf(newSecondOperand),sendRequest(String.valueOf(sum.getFirstOperand()),String.valueOf(newSecondOperand)));
        String newSumState= genson.serialize(newSum);
        stub.putStringState(key, newSumState);

        return newSum;
    }
}
