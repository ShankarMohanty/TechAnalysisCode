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

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.mocks.MockTick;
import eu.verdelhan.ta4j.series.DefaultTimeSeries;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

public class StochasticOscillatorDIndicatorTest {
	private TimeSeries data;

	@Before
	public void setUp() {

		List<Tick> ticks = new ArrayList<Tick>();
		ticks.add(new MockTick(44.98, 119.13, 119.50, 116.00));
		ticks.add(new MockTick(45.05, 116.75, 119.94, 116.00));
		ticks.add(new MockTick(45.11, 113.50, 118.44, 111.63));
		ticks.add(new MockTick(45.19, 111.56, 114.19, 110.06));
		ticks.add(new MockTick(45.12, 112.25, 112.81, 109.63));
		ticks.add(new MockTick(45.15, 110.00, 113.44, 109.13));
		ticks.add(new MockTick(45.13, 113.50, 115.81, 110.38));
		ticks.add(new MockTick(45.12, 117.13, 117.50, 114.06));
		ticks.add(new MockTick(45.15, 115.63, 118.44, 114.81));
		ticks.add(new MockTick(45.24, 114.13, 116.88, 113.13));
		ticks.add(new MockTick(45.43, 118.81, 119.00, 116.19));
		ticks.add(new MockTick(45.43, 117.38, 119.75, 117.00));
		ticks.add(new MockTick(45.58, 119.13, 119.13, 116.88));
		ticks.add(new MockTick(45.58, 115.38, 119.44, 114.56));

		data = new DefaultTimeSeries(ticks);
	}

	@Test
	public void testStochasticOscilatorDParam14UsingSMA3AndGenericConstructer() {

		StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(data, 14);
		SMAIndicator sma = new SMAIndicator(sof, 3);
		StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sma);

		assertThat(sos.getValue(0)).isEqualTo(sma.getValue(0));
		assertThat(sos.getValue(1)).isEqualTo(sma.getValue(1));
		assertThat(sos.getValue(2)).isEqualTo(sma.getValue(2));
	}

	@Test
	public void testStochasticOscilatorDParam14UsingSMA3() {

		StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(data, 14);
		StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sof);
		SMAIndicator sma = new SMAIndicator(sof, 3);

		assertThat(sos.getValue(0)).isEqualTo(sma.getValue(0));
		assertThat(sos.getValue(1)).isEqualTo(sma.getValue(1));
		assertThat(sos.getValue(2)).isEqualTo(sma.getValue(2));
	}

	@Test
	public void testStochasticOscilatorDShouldWorkJumpingIndexes() {

		StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(data, 14);
		StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sof);
		SMAIndicator sma = new SMAIndicator(sof, 3);

		assertThat(sos.getValue(13)).isEqualTo(sma.getValue(13));
	}
}
