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
package eu.verdelhan.ta4j.strategies;

import eu.verdelhan.ta4j.strategies.CombinedBuyAndSellStrategy;
import eu.verdelhan.ta4j.strategies.IndicatorCrossedIndicatorStrategy;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Operation;
import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.mocks.MockIndicator;
import eu.verdelhan.ta4j.mocks.MockStrategy;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class CombinedBuyAndSellStrategyTest {

	private Operation[] enter;

	private Operation[] exit;

	private MockStrategy buyStrategy;

	private MockStrategy sellStrategy;

	private CombinedBuyAndSellStrategy combined;

	@Test
	public void testeShoudEnter() {

		enter = new Operation[] { new Operation(0, OperationType.BUY), null, new Operation(2, OperationType.BUY), null,
				new Operation(4, OperationType.BUY) };
		exit = new Operation[] { null, null, null, null, null };

		buyStrategy = new MockStrategy(enter, null);
		sellStrategy = new MockStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertThat(combined.shouldEnter(0)).isTrue();
		assertThat(combined.shouldEnter(1)).isFalse();
		assertThat(combined.shouldEnter(2)).isTrue();
		assertThat(combined.shouldEnter(3)).isFalse();
		assertThat(combined.shouldEnter(4)).isTrue();

		assertThat(combined.shouldExit(0)).isFalse();
		assertThat(combined.shouldExit(1)).isFalse();
		assertThat(combined.shouldExit(2)).isFalse();
		assertThat(combined.shouldExit(3)).isFalse();
		assertThat(combined.shouldExit(4)).isFalse();

	}

	@Test
	public void testeShoudExit() {

		exit = new Operation[] { new Operation(0, OperationType.SELL), null, new Operation(2, OperationType.SELL),
				null, new Operation(4, OperationType.SELL) };

		enter = new Operation[] { null, null, null, null, null };

		buyStrategy = new MockStrategy(enter, null);
		sellStrategy = new MockStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertThat(combined.shouldExit(0)).isTrue();
		assertThat(combined.shouldExit(1)).isFalse();
		assertThat(combined.shouldExit(2)).isTrue();
		assertThat(combined.shouldExit(3)).isFalse();
		assertThat(combined.shouldExit(4)).isTrue();

		assertThat(combined.shouldEnter(0)).isFalse();
		assertThat(combined.shouldEnter(1)).isFalse();
		assertThat(combined.shouldEnter(2)).isFalse();
		assertThat(combined.shouldEnter(3)).isFalse();
		assertThat(combined.shouldEnter(4)).isFalse();
	}

	@Test
	public void testWhenBuyStrategyAndSellStrategyAreEquals() {
		Indicator<Double> first = new MockIndicator<Double>(new Double[] { 4d, 7d, 9d, 6d, 3d, 2d });
		Indicator<Double> second = new MockIndicator<Double>(new Double[] { 3d, 6d, 10d, 8d, 2d, 1d });

		Strategy crossed = new IndicatorCrossedIndicatorStrategy(first, second);

		combined = new CombinedBuyAndSellStrategy(crossed, crossed);

		for (int index = 0; index < 6; index++) {
			assertThat(combined.shouldEnter(index)).isEqualTo(crossed.shouldEnter(index));
			assertThat(combined.shouldExit(index)).isEqualTo(crossed.shouldExit(index));
		}
	}
}
