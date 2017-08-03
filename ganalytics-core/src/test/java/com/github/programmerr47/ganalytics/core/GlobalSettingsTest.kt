package com.github.programmerr47.ganalytics.core

import org.junit.Test
import kotlin.reflect.KClass

class GlobalSettingsTest : WrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()

    @Test
    fun checkCuttingOffAnalyticsPrefix() {
        arrayOf(
                GanalyticsSettings { cutOffAnalyticsClassPrefix = false } to "analyticsinterface",
                GanalyticsSettings { cutOffAnalyticsClassPrefix = true } to "interface",
                GanalyticsSettings() to "interface")
                .forEach {
                    run(it.first, AnalyticsInterface::class) {
                        assertEquals(Event(it.second, "method1")) { method1() }
                        assertEquals(Event(it.second, "method2")) { method2() }
                    }
                }
    }

    @Test
    fun checkGlobalSplitter() {
        val settings = GanalyticsSettings { prefixSplitter = "^_^" }
        run(settings, AnalyticsInterface::class) {
            assertEquals(Event("interface", "method1")) { method1() }
            assertEquals(Event("interface", "method2")) { method2() }
        }
        run(settings, AnalyticsHasPrefixInterface::class) {
            assertEquals(Event("hasprefixinterface", "hasprefixinterface^_^method1")) { method1() }
            assertEquals(Event("hasprefixinterface", "hasprefixinterface^_^method1")) { method1() }
        }
        run(settings, SplitterInterface::class) {
            assertEquals(Event("splitterinterface", "splitterinterface^_^method1")) { method1() }
            assertEquals(Event("splitterinterface", "splitterinterface_-_method2")) { method2() }
            assertEquals(Event("splitterinterface", "splitterinterface::method3")) { method3() }
        }
    }

    @Test
    fun checkGlobalConvention() {
        val settings = GanalyticsSettings { namingConvention = testConvention() }
        run(settings, SampleInterface::class) {
            assertEquals(Event("s_a_m_p_l_e_I_n_t_e_r_f_a_c_e", "m_e_t_h_o_d_1")) { method1() }
            assertEquals(Event("s_a_m_p_l_e_I_n_t_e_r_f_a_c_e", "m_e_t_h_o_d_2")) { method2() }
        }
        run(settings, AnalyticsLibConventionInterface::class) {
            assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
        }
    }

    private inline fun <T : Any> run(settings: GanalyticsSettings, clazz: KClass<T>, block: T.() -> Unit) =
            run(testSingleWrapper(settings), clazz, block)

    private fun testSingleWrapper(settings: GanalyticsSettings = GanalyticsSettings()) =
            AnalyticsSingleWrapper(testProvider, settings)

    private fun testConvention() = object : NamingConvention {
        override fun convert(name: String) = name.toCharArray().joinToString(separator = "_")
    }
}