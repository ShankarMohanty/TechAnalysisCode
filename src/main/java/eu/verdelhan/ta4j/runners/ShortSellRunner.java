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
package eu.verdelhan.ta4j.runners;

import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.Runner;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeriesSlicer;
import eu.verdelhan.ta4j.Trade;
import java.util.ArrayList;
import java.util.List;

/**
 * A short sell runner.
 * <p>
 */
public class ShortSellRunner implements Runner {
    private Runner runner;

    public ShortSellRunner(TimeSeriesSlicer slicer, Strategy strategy) {
        runner = new HistoryRunner(slicer, strategy);
    }

    @Override
    public List<Trade> run(int slicePosition) {
        List<Trade> trades = runner.run(slicePosition);
        List<Trade> tradesWithShortSells = new ArrayList<Trade>();

        for (int i = 0; i < (trades.size() - 1); i++) {

            Trade trade = trades.get(i);
            tradesWithShortSells.add(trade);

            Trade nextTrade = trades.get(i + 1);

            Trade shortSell = new Trade(OperationType.SELL);
            shortSell.operate(trade.getExit().getIndex());
            shortSell.operate(nextTrade.getEntry().getIndex());
            tradesWithShortSells.add(shortSell);
        }

        if (!trades.isEmpty()) {
            tradesWithShortSells.add(trades.get(trades.size() - 1));
        }

        return tradesWithShortSells;
    }
}
