///*
// * SPDX-License-Identifier: Apache-2.0
// */
//
//package org.hyperledger.fabric.samples.fabcar;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.ThrowableAssert.catchThrowable;
//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verifyZeroInteractions;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.hyperledger.fabric.contract.Context;
//import org.hyperledger.fabric.shim.ChaincodeException;
//import org.hyperledger.fabric.shim.ChaincodeStub;
//import org.hyperledger.fabric.shim.ledger.KeyValue;
//import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InOrder;
//
//public final class FabcarTest {
//
//    private final class MockKeyValue implements KeyValue {
//
//        private final String key;
//        private final String value;
//
//        MockKeyValue(final String key, final String value) {
//            super();
//            this.key = key;
//            this.value = value;
//        }
//
//        @Override
//        public String getKey() {
//            return this.key;
//        }
//
//        @Override
//        public String getStringValue() {
//            return this.value;
//        }
//
//        @Override
//        public byte[] getValue() {
//            return this.value.getBytes();
//        }
//
//    }
//
//    private final class MockCarResultsIterator implements QueryResultsIterator<KeyValue> {
//
//        private final List<KeyValue> sumList;
//
//        MockCarResultsIterator() {
//            super();
//
//            sumList = new ArrayList<KeyValue>();
//
//            sumList.add(new MockKeyValue("SUM0",
//                    "{\"firstOperand\":\"0.11\",\"secondOperand\":\"11.98\",\"result\":\""+SumModel.computations(0.11,11.98).getResult()+"\"}"));
//            sumList.add(new MockKeyValue("SUM1",
//                    "{\"firstOperand\":\"0.34\",\"secondOperand\":\"1.36\",\"result\":\""+SumModel.computations(0.34,1.36).getResult()+"\"}"));
//            sumList.add(new MockKeyValue("SUM2",
//                    "{\"firstOperand\":\"9.99\",\"secondOperand\":\"1.0\",\"result\":\""+SumModel.computations(9.99,1.0).getResult()+"\"}"));
//            sumList.add(new MockKeyValue("SUM7",
//                    "{\"firstOperand\":\"0.78\",\"secondOperand\":\"5.13\",\"result\":\""+SumModel.computations(0.78,5.13).getResult()+"\"}"));
//            sumList.add(new MockKeyValue("SUM9",
//                    "{\"firstOperand\":\"0.52\",\"secondOperand\":\"1.22\",\"result\":\""+SumModel.computations(0.52,1.22).getResult()+"\"}"));
//        }
//
//        @Override
//        public Iterator<KeyValue> iterator() {
//            return sumList.iterator();
//        }
//
//        @Override
//        public void close() throws Exception {
//            // do nothing
//        }
//
//    }
//
//    @Test
//    public void invokeUnknownTransaction() {
//        Fabcar contract = new Fabcar();
//        Context ctx = mock(Context.class);
//
//        Throwable thrown = catchThrowable(() -> {
//            contract.unknownTransaction(ctx);
//        });
//
//        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                .hasMessage("Undefined contract method called");
//        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);
//
//        verifyZeroInteractions(ctx);
//    }
//
//    @Nested
//    class InvokeQueryCarTransaction {
//
//        @Test
//        public void whenSumExists() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            Sum sumTemp=SumModel.computations(0.11,11.98);
//            when(stub.getStringState("SUM0"))
//                    .thenReturn("{\"firstOperand\":\"0.11\",\"secondOperand\":\"11.98\",\"result\":\""+sumTemp.getResult()+"\"}");
//
//            Sum sum = contract.querySum(ctx, "SUM0");
//
//            assertThat(sum).isEqualTo(new Sum(0.11,11.98,SumModel.computations(0.11,11.98).getResult()));
//        }
//
//        @Test
//        public void whenSumDoesNotExist() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("SUM0")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.querySum(ctx, "SUM0");
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Car SUM0 does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("SUM_NOT_FOUND".getBytes());
//        }
//    }
//
//    @Test
//    void invokeInitLedgerTransaction() {
//        Fabcar contract = new Fabcar();
//        Context ctx = mock(Context.class);
//        ChaincodeStub stub = mock(ChaincodeStub.class);
//        when(ctx.getStub()).thenReturn(stub);
//
//        contract.initLedger(ctx);
//
//        InOrder inOrder = inOrder(stub);
//        inOrder.verify(stub).putStringState("SUM0",
//                "{\"firstOperand\":\"0.11\",\"secondOperand\":\"11.98\",\"result\":\""+SumModel.computations(0.11,11.98).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM1",
//                "{\"firstOperand\":\"0.34\",\"secondOperand\":\"1.36\",\"result\":\""+SumModel.computations(0.34,1.36).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM2",
//                "{\"firstOperand\":\"9.99\",\"secondOperand\":\"1.0\",\"result\":\""+SumModel.computations(9.99,1.0).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM3",
//                "{\"firstOperand\":\"0.78\",\"secondOperand\":\"5.13\",\"result\":\""+SumModel.computations(0.78,5.13).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM4",
//                "{\"firstOperand\":\"0.52\",\"secondOperand\":\"1.22\",\"result\":\""+SumModel.computations(0.52,1.22).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM5",
//                "{\"firstOperand\":\"0.12\",\"secondOperand\":\"34.96\",\"result\":\""+SumModel.computations(0.12,34.96).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM6",
//                "{\"firstOperand\":\"0.71\",\"secondOperand\":\"1.22\",\"result\":\""+SumModel.computations(0.71,1.22).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM7",
//                "{\"firstOperand\":\"0.2\",\"secondOperand\":\"1.2\",\"result\":\""+SumModel.computations(0.2,1.2).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM8",
//                "{\"firstOperand\":\"0.93\",\"secondOperand\":\"21.22\",\"result\":\""+SumModel.computations(0.93,21.22).getResult()+"\"}");
//        inOrder.verify(stub).putStringState("SUM9",
//                "{\"firstOperand\":\"0.76\",\"secondOperand\":\"6.92\",\"result\":\""+SumModel.computations(0.76,6.92).getResult()+"\"}");
//    }
//
//    @Nested
//    class InvokeCreateSumTransaction {
//
//        @Test
//        public void whenSumExists() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("SUM0"))
//                    .thenReturn("{\"firstOperand\":\"0.11\",\"secondOperand\":\"11.98\",\"result\":\""+SumModel.computations(0.11,11.98)+"\"}");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.calculateSum(ctx, "SUM0", 0.11, 11.98);
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Sum SUM0 already exists");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("SUM_ALREADY_EXISTS".getBytes());
//        }
//
//        @Test
//        public void whenCarDoesNotExist() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("SUM0")).thenReturn("");
//
//            Sum car = contract.calculateSum(ctx, "SUM0", 0.11, 11.98);
//
//            assertThat(car).isEqualTo(SumModel.computations(0.11,11.98));
//        }
//    }
//
//    @Test
//    void invokeQueryAllSumsTransaction() {
//        Fabcar contract = new Fabcar();
//        Context ctx = mock(Context.class);
//        ChaincodeStub stub = mock(ChaincodeStub.class);
//        when(ctx.getStub()).thenReturn(stub);
//        when(stub.getStateByRange("SUM0", "SUM999")).thenReturn(new MockCarResultsIterator());
//
//        SumQueryResult[] cars = contract.queryAllSums(ctx);
//
//        final List<SumQueryResult> expectedCars = new ArrayList<SumQueryResult>();
//        expectedCars.add(new SumQueryResult("SUM0", SumModel.computations(0.11,11.98)));
//        expectedCars.add(new SumQueryResult("SUM1", SumModel.computations(0.34,1.36)));
//        expectedCars.add(new SumQueryResult("SUM2", SumModel.computations(9.99,1.0)));
//        expectedCars.add(new SumQueryResult("SUM7", SumModel.computations(0.78,5.13)));
//        expectedCars.add(new SumQueryResult("SUM9", SumModel.computations(0.52,1.22)));
//
//        assertThat(cars).containsExactlyElementsOf(expectedCars);
//    }
//
//    @Nested
//    class ChangeSumOwnerTransaction {
//
//        @Test
//        public void whenSumExists() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("SUM0"))
//                    .thenReturn("{\"firstOperand\":\"0.11\",\"secondOperand\":\"11.98\",\"result\":\"2.09\"}");
//
//            Sum sum = contract.changeFirstOperandOfSum(ctx, "SUM0", 4.56);
//
//            assertThat(sum).isEqualTo(new SumQueryResult("SUM0", SumModel.computations(4.56, 11.98)));
//        }
//
//        @Test
//        public void whenSumDoesNotExist() {
//            Fabcar contract = new Fabcar();
//            Context ctx = mock(Context.class);
//            ChaincodeStub stub = mock(ChaincodeStub.class);
//            when(ctx.getStub()).thenReturn(stub);
//            when(stub.getStringState("SUM0")).thenReturn("");
//
//            Throwable thrown = catchThrowable(() -> {
//                contract.changeFirstOperandOfSum(ctx, "SUM0", 4.56);
//            });
//
//            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
//                    .hasMessage("Sum SUM0 does not exist");
//            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("SUM_NOT_FOUND".getBytes());
//        }
//    }
//}
