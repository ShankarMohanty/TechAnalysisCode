/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Marc de Verdelhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.helpers;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

public class HighestValueIndicatorTest {

	private TimeSeries data;

	@Before
	public void setUp() {
		data = new MockTimeSeries(1, 2, 3, 4, 3, 4, 5, 6, 4, 3, 3, 4, 3, 2);
	}

	@Test
	public void testHighestValueUsingTimeFrame5UsingClosePrice() throws Exception {
		HighestValueIndicator highestValue = new HighestValueIndicator(new ClosePriceIndicator(data), 5);

		assertThat(highestValue.getValue(4)).isEqualTo(4d);
		assertThat(highestValue.getValue(5)).isEqualTo(4d);
		assertThat(highestValue.getValue(6)).isEqualTo(5d);
		assertThat(highestValue.getValue(7)).isEqualTo(6d);
		assertThat(highestValue.getValue(8)).isEqualTo(6d);
		assertThat(highestValue.getValue(9)).isEqualTo(6d);
		assertThat(highestValue.getValue(10)).isEqualTo(6d);
		assertThat(highestValue.getValue(11)).isEqualTo(6d);
		assertThat(highestValue.getValue(12)).isEqualTo(4d);

	}

	@Test
	public void testFirstHighestValueIndicatorValueShouldBeEqualsToFirstDataValue() {
		HighestValueIndicator highestValue = new HighestValueIndicator(new ClosePriceIndicator(data), 5);
		assertThat(highestValue.getValue(0)).isEqualTo(1);
	}

	@Test
	public void testHighestValueIndicatorWhenTimeFrameIsGreaterThanIndex() {
		HighestValueIndicator highestValue = new HighestValueIndicator(new ClosePriceIndicator(data), 500);
		assertThat(highestValue.getValue(12)).isEqualTo(6d);
	}

	@Test
	public void testHighestValueShouldWorkJumpingIndexes() {
		HighestValueIndicator highestValue = new HighestValueIndicator(new ClosePriceIndicator(data), 5);
		assertThat(highestValue.getValue(6)).isEqualTo(5d);
		assertThat(highestValue.getValue(12)).isEqualTo(4d);
	}
}
