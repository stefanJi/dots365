package site.jiyang.dots365

import org.junit.Assert
import org.junit.Test

/**
 * Create by jy on 2019-10-27
 */
class DateCalculatorTest {

    @Test
    fun testCalculate() {
        Assert.assertEquals("82.2%", DateCalculator.calculate(2019, 10, 27, 300).percent)
        Assert.assertEquals("82.5%", DateCalculator.calculate(2019, 10, 28, 301).percent)
        Assert.assertEquals("82.7%", DateCalculator.calculate(2019, 10, 29, 302).percent)
    }
}