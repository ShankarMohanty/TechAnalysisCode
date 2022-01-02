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

import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.analysis.evaluators.Decision;
import java.util.ArrayList;
import java.util.List;

/**
 * Buy and hold criterion.
 * <p>
 * @see <a href="http://en.wikipedia.org/wiki/Buy_and_hold">http://en.wikipedia.org/wiki/Buy_and_hold</a>
 */
public class BuyAndHoldCriterion extends AbstractAnalysisCriterion {

    @Override
    public double calculate(TimeSeries series, List<Trade> trades) {
        return series.getTick(series.getEnd()).getClosePrice() / series.getTick(series.getBegin()).getClosePrice();
    }

    @Override
    public double summarize(TimeSeries series, List<Decision> decisions) {
        return calculate(series, new ArrayList<Trade>());
    }

    @Override
    public double calculate(TimeSeries series, Trade trade) {
		int entryIndex = trade.getEntry().getIndex();
		int exitIndex = trade.getExit().getIndex();

        if (trade.getEntry().getType() == OperationType.BUY) {
            return series.getTick(exitIndex).getClosePrice() / series.getTick(entryIndex).getClosePrice();
        } else {
            return series.getTick(entryIndex).getClosePrice() / series.getTick(exitIndex).getClosePrice();
        }
    }
}
