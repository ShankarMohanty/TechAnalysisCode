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
import eu.verdelhan.ta4j.TimeSeriesSlicer;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.analysis.evaluators.Decision;
import eu.verdelhan.ta4j.mocks.MockDecision;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import eu.verdelhan.ta4j.series.RegularSlicer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.joda.time.Period;
import org.junit.Test;

public class VersusBuyAndHoldCriterionTest {

	@Test
	public void testCalculateOnlyWithGainTrades() {
		MockTimeSeries series = new MockTimeSeries(100, 105, 110, 100, 95, 105);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		trades.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
		assertThat(buyAndHold.calculate(series, trades)).isEqualTo(1.10 * 1.05 / 1.05);
	}

	@Test
	public void testSummarize() {
		MockTimeSeries series = new MockTimeSeries(100, 105, 110, 100, 95, 105);
		TimeSeriesSlicer slicer = new RegularSlicer(series, new Period().withYears(2000));
		List<Decision> decisions = new LinkedList<Decision>();

		List<Trade> tradesToDummy1 = new LinkedList<Trade>();
		tradesToDummy1.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		Decision dummy1 = new MockDecision(tradesToDummy1, slicer);
		decisions.add(dummy1);

		List<Trade> tradesToDummy2 = new LinkedList<Trade>();
		tradesToDummy2.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));
		Decision dummy2 = new MockDecision(tradesToDummy2, slicer);
		decisions.add(dummy2);
		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
		assertThat(buyAndHold.summarize(series, decisions)).isEqualTo(1.10 * 1.05 / 1.05);
	}

	@Test
	public void testCalculateOnlyWithLossTrades() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
		assertThat(buyAndHold.calculate(series, trades)).isEqualTo(0.95 * 0.7 / 0.7);
	}
	
	@Test
	public void testCalculateWithOnlyOneTrade() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		Trade trade = new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL));

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
		assertThat(buyAndHold.calculate(series, trade)).isEqualTo((100d / 70) / (100d / 95));
	}

	@Test
	public void testCalculateWithNoTrades() {
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
		List<Trade> trades = new ArrayList<Trade>();

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
		assertThat(buyAndHold.calculate(series, trades)).isEqualTo(1 / 0.7);
	}
	@Test
	public void testCalculateWithAverageProfit()
	{
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 130);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new AverageProfitCriterion());
		
		assertThat(buyAndHold.calculate(series, trades)).isEqualTo(Math.pow(95d/100 * 130d/100, 1d/6) / Math.pow(130d / 100, 1d/6));
	}
	@Test
	public void testCalculateWithNumberOfTicks()
	{
		MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 130);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new VersusBuyAndHoldCriterion(new NumberOfTicksCriterion());
		
		assertThat(buyAndHold.calculate(series, trades)).isEqualTo(6d/6d);
	}
}
