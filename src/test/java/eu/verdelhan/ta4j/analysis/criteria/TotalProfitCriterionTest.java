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
package eu.verdelhan.ta4j.analysis.criteria;

import eu.verdelhan.ta4j.AnalysisCriterion;
import eu.verdelhan.ta4j.Operation;
import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class TotalProfitCriterionTest {

	@Test
	public void testCalculateOnlyWithGainTrades() {
		MockTimeSeries series = new MockTimeSeries(100, 105, 110, 100, 95, 105);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		trades.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion profit = new TotalProfitCriterion();
		assertThat(profit.calculate(series, trades)).isEqualTo(1.10 * 1.05);
	}

	@Test
	public void testCalculateOnlyWithLossTrades() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion profit = new TotalProfitCriterion();
		assertThat(profit.calculate(series, trades)).isEqualTo(0.95 * 0.7);
	}

	@Test
	public void testCalculateProfitWithTradesThatStartSelling() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.SELL), new Operation(1, OperationType.BUY)));
		trades.add(new Trade(new Operation(2, OperationType.SELL), new Operation(5, OperationType.BUY)));

		AnalysisCriterion profit = new TotalProfitCriterion();
		assertThat(profit.calculate(series, trades)).isEqualTo((1 / 0.95) * (1 / 0.7));
	}

	@Test
	public void testCalculateWithNoTradesShouldReturn1() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		List<Trade> trades = new ArrayList<Trade>();

		AnalysisCriterion profit = new TotalProfitCriterion();
		assertThat(profit.calculate(series, trades)).isEqualTo(1d);
	}
}
