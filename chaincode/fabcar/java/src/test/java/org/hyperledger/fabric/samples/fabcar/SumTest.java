///*
// * SPDX-License-Identifier: Apache-2.0
// */
//
//package org.hyperledger.fabric.samples.fabcar;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//public final class SumTest {
//
//    @Nested
//    class Equality {
//
//        @Test
//        public void isReflexive() {
//            Sum sum = SumModel.computations(0.11,0.23);
//
//            assertThat(sum).isEqualTo(sum);
//        }
//
//        @Test
//        public void isSymmetric() {
//            Sum sumA = SumModel.computations(0.11,0.23);
//            Sum sumB = SumModel.computations(0.11,0.23);
//
//            assertThat(sumA).isEqualTo(sumB);
//            assertThat(sumB).isEqualTo(sumA);
//        }
//
//        @Test
//        public void isTransitive() {
//            Sum sumA = SumModel.computations(0.31,0.589);
//            Sum sumB = SumModel.computations(0.51,0.23);
//            Sum sumC = SumModel.computations(0.1,0.23);
//
//            assertThat(sumA).isEqualTo(sumB);
//            assertThat(sumB).isEqualTo(sumC);
//            assertThat(sumA).isEqualTo(sumC);
//        }
//
//        @Test
//        public void handlesInequality() {
//            Sum sumA =SumModel.computations(0.11,0.23);
//            Sum sumB = SumModel.computations(0.56,1.43);
//
//            assertThat(sumA).isNotEqualTo(sumB);
//        }
//
//        @Test
//        public void handlesOtherObjects() {
//            Sum sumA = SumModel.computations(0.11,0.23);
//            String carB = "not a car";
//
//            assertThat(sumA).isNotEqualTo(carB);
//        }
//
//        @Test
//        public void handlesNull() {
//            Sum sum = SumModel.computations(0.11,0.23);
//
//            assertThat(sum).isNotEqualTo(null);
//        }
//    }
//
//}
