package com.github.programmerr47.ganalytics.core

import org.junit.Test

class AnalyticsSingleWrapperTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkDefaultBehavior() {
        run(SampleInterface::class) {
            assertEquals(Event("sampleinterface", "method1")) { method1() }
            assertEquals(Event("sampleinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkCuttingOffAnalyticsPrefix() {
        run(AnalyticsInterface::class) {
            assertEquals(Event("interface", "method1")) { method1() }
            assertEquals(Event("interface", "method2")) { method2() }
        }
    }
}