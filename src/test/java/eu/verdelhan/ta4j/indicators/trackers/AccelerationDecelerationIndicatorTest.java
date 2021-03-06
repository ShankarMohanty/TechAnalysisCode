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
package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.TATestsUtils;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.mocks.MockTick;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

public class AccelerationDecelerationIndicatorTest {

	private TimeSeries series;

	@Before
	public void setUp() {

		List<Tick> ticks = new ArrayList<Tick>();

		ticks.add(new MockTick(0, 0, 16, 8));
		ticks.add(new MockTick(0, 0, 12, 6));
		ticks.add(new MockTick(0, 0, 18, 14));
		ticks.add(new MockTick(0, 0, 10, 6));
		ticks.add(new MockTick(0, 0, 8, 4));

		series = new MockTimeSeries(ticks);
	}

	@Test
	public void testCalculateWithSma2AndSma3() throws Exception {
		AccelerationDecelerationIndicator acceleration = new AccelerationDecelerationIndicator(series, 2, 3);

		assertThat(acceleration.getValue(0)).isZero();
		assertThat(acceleration.getValue(1)).isZero();
		assertThat(acceleration.getValue(2)).isEqualTo(0.1666666666d - 0.0833333333d, TATestsUtils.LONG_OFFSET);
		assertThat(acceleration.getValue(3)).isEqualTo(1d - 0.5833333333, TATestsUtils.LONG_OFFSET);
		assertThat(acceleration.getValue(4)).isEqualTo(-3d + 1d);
	}

	@Test
	public void testWithSma1AndSma2() throws Exception {
		AccelerationDecelerationIndicator acceleration = new AccelerationDecelerationIndicator(series, 1, 2);

		assertThat(acceleration.getValue(0)).isZero();
		assertThat(acceleration.getValue(1)).isZero();
		assertThat(acceleration.getValue(2)).isZero();
		assertThat(acceleration.getValue(3)).isZero();
		assertThat(acceleration.getValue(4)).isZero();
	}

	@Test
	public void testWithSmaDefault() throws Exception {
		AccelerationDecelerationIndicator acceleration = new AccelerationDecelerationIndicator(series);

		assertThat(acceleration.getValue(0)).isZero();
		assertThat(acceleration.getValue(1)).isZero();
		assertThat(acceleration.getValue(2)).isZero();
		assertThat(acceleration.getValue(3)).isZero();
		assertThat(acceleration.getValue(4)).isZero();
	}
}
