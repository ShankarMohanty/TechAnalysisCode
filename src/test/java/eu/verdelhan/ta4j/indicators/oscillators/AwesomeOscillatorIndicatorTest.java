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
package eu.verdelhan.ta4j.indicators.oscillators;

import eu.verdelhan.ta4j.TATestsUtils;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.AverageHighLowIndicator;
import eu.verdelhan.ta4j.mocks.MockTick;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

public class AwesomeOscillatorIndicatorTest {
	private TimeSeries series;

	@Before
	public void setUp() {

		List<Tick> ticks = new ArrayList<Tick>();

		ticks.add(new MockTick(0, 0, 16, 8));//12
		ticks.add(new MockTick(0, 0, 12, 6));//9
		ticks.add(new MockTick(0, 0, 18, 14));//16
		ticks.add(new MockTick(0, 0, 10, 6));//8
		ticks.add(new MockTick(0, 0, 8, 4));//6

		this.series = new MockTimeSeries(ticks);
	}

	@Test
	public void testCalculateWithSma2AndSma3() throws Exception {
		AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new AverageHighLowIndicator(series), 2, 3);

		assertThat(awesome.getValue(0)).isEqualTo(0d);
		assertThat(awesome.getValue(1)).isEqualTo(0d);
		assertThat(awesome.getValue(2)).isEqualTo(0.1666666666d, TATestsUtils.LONG_OFFSET);
		assertThat(awesome.getValue(3)).isEqualTo(1d);
		assertThat(awesome.getValue(4)).isEqualTo(-3d);
	}

	@Test
	public void testWithSma1AndSma2() throws Exception {
		AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new AverageHighLowIndicator(series), 1, 2);

		assertThat(awesome.getValue(0)).isEqualTo(0d);
		assertThat(awesome.getValue(1)).isEqualTo(-1.5d);
		assertThat(awesome.getValue(2)).isEqualTo(3.5d);
		assertThat(awesome.getValue(3)).isEqualTo(-4d);
		assertThat(awesome.getValue(4)).isEqualTo(-1d);
	}

	@Test
	public void testWithSmaDefault() throws Exception {
		AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new AverageHighLowIndicator(series));

		assertThat(awesome.getValue(0)).isEqualTo(0d);
		assertThat(awesome.getValue(1)).isEqualTo(0d);
		assertThat(awesome.getValue(2)).isEqualTo(0d);
		assertThat(awesome.getValue(3)).isEqualTo(0d);
		assertThat(awesome.getValue(4)).isEqualTo(0d);
	}

}
