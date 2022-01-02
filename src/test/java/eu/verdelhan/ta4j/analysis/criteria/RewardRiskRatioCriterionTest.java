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
import org.junit.Before;
import org.junit.Test;

public class RewardRiskRatioCriterionTest {

	private RewardRiskRatioCriterion rrc;

	@Before
	public void setUp() {
		this.rrc = new RewardRiskRatioCriterion();
	}

	@Test
	public void testRewardRiskRatioCriterion() {
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(4, OperationType.SELL)));
		trades.add(new Trade(new Operation(5, OperationType.BUY), new Operation(7, OperationType.SELL)));

		MockTimeSeries series = new MockTimeSeries(100, 105, 95, 100, 90, 95, 80, 120);

		double totalProfit = (105d / 100) * (90d / 95d) * (120d / 95);
		double peak = (105d / 100) * (100d / 95);
		double low = (105d / 100) * (90d / 95) * (80d / 95);

		assertThat(rrc.calculate(series, trades)).isEqualTo(totalProfit / ((peak - low) / peak));
	}

	@Test
	public void testSummarize() {
		MockTimeSeries series = new MockTimeSeries(100, 105, 95, 100, 90, 95, 80, 120);
		List<Decision> decisions = new LinkedList<Decision>();
		TimeSeriesSlicer slicer = new RegularSlicer(series, new Period().withYears(2000));

		List<Trade> tradesToDummy1 = new LinkedList<Trade>();
		tradesToDummy1.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		Decision dummy1 = new MockDecision(tradesToDummy1, slicer);
		decisions.add(dummy1);

		List<Trade> tradesToDummy2 = new LinkedList<Trade>();
		tradesToDummy2.add(new Trade(new Operation(2, OperationType.BUY), new Operation(4, OperationType.SELL)));
		Decision dummy2 = new MockDecision(tradesToDummy2, slicer);
		decisions.add(dummy2);

		List<Trade> tradesToDummy3 = new LinkedList<Trade>();
		tradesToDummy3.add(new Trade(new Operation(5, OperationType.BUY), new Operation(7, OperationType.SELL)));
		Decision dummy3 = new MockDecision(tradesToDummy3, slicer);
		decisions.add(dummy3);

		double totalProfit = (105d / 100) * (90d / 95d) * (120d / 95);
		double peak = (105d / 100) * (100d / 95);
		double low = (105d / 100) * (90d / 95) * (80d / 95);
		
		assertThat(rrc.summarize(series, decisions)).isEqualTo(totalProfit / ((peak - low) / peak));
	}

	@Test
	public void testRewardRiskRatioCriterionOnlyWithGain() {
		MockTimeSeries series = new MockTimeSeries(1, 2, 3, 6, 8, 20, 3);
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		assertThat(Double.isInfinite(rrc.calculate(series, trades))).isTrue();

	}

	@Test
	public void testRewardRiskRatioCriterionWithNoTrades() {
		MockTimeSeries series = new MockTimeSeries(1, 2, 3, 6, 8, 20, 3);
		List<Trade> trades = new ArrayList<Trade>();

		assertThat(Double.isInfinite(rrc.calculate(series, trades))).isTrue();

	}
	
	@Test
	public void testWithOneTrade() {
		Trade trade = new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL));

		MockTimeSeries series = new MockTimeSeries(100, 95, 95, 100, 90, 95, 80, 120);

			
		RewardRiskRatioCriterion ratioCriterion = new RewardRiskRatioCriterion();
		assertThat(ratioCriterion.calculate(series, trade)).isEqualTo((95d/100) / ((1d - 0.95d)));
	}
}
