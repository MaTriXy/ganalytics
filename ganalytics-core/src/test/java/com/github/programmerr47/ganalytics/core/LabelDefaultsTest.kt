package com.github.programmerr47.ganalytics.core

import org.junit.Test
import java.math.BigDecimal

class LabelDefaultsTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkLabelInSingleParameterMethods() {
        val dummyClass = DummyClass(101, "Holy world!")
        run(SingleParameterMethodInterface::class) {
            assertEquals(Event("singleparametermethodinterface", "intMethod", "101")) { intMethod(101) }
            assertEquals(Event("singleparametermethodinterface", "strMethod", "Holy world!")) { strMethod("Holy world!") }
            assertEquals(Event("singleparametermethodinterface", "dummyClassMethod", dummyClass.toString())) { dummyClassMethod(dummyClass) }
            assertEquals(Event("singleparametermethodinterface", "dummyDataClassMethod", "DummyDataClass(id=101, name=Holy world!)")) { dummyDataClassMethod(DummyDataClass(101, "Holy world!")) }
            assertEquals(Event("singleparametermethodinterface", "dummyEnumClassMethod", "TWO")) { dummyEnumClassMethod(DummyEnum.TWO) }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun throwErrorInMoreThanTwoParameterMethod() {
        run(MoreTwoParameterMethodInterface::class) {
            assertEquals(Event("moretwoparametermethodinterface", "method1")) { method1() }
            method2("1", 2, 3L)
        }
    }

    @Test
    fun checkLabelAndValueInTwoParamMethod() {
        run(TwoParameterMethodStringAndNumberInterface::class) {
            assertEquals(Event("interface", "intStrMethod", "2", 1)) { intStrMethod(1, "2") }
            assertEquals(Event("interface", "strIntMethod", "1", 2)) { strIntMethod("1", 2) }
            assertEquals(Event("interface", "strLongMethod", "1", 2)) { strLongMethod("1", 2) }
            assertEquals(Event("interface", "strNumberMethod", "1", 32)) { strNumberMethod("1", BigDecimal(32.3)) }
            assertEquals(Event("interface", "strNumberMethod", "1", 33)) { strNumberMethod("1", 33.9f) }
        }
    }
}