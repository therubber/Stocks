package stocks.factories;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFactory {

    public BigDecimal createBigDecimal(int value) {
        return NumberFactory.BigDecimalCreator.fromInteger(value);
    }

    public BigDecimal createBigDecimal(double value) {
        return BigDecimalCreator.fromDouble(value);
    }

    private static class BigDecimalCreator {

        private BigDecimalCreator() {}

        public static BigDecimal fromDouble(double value) {
            return new BigDecimal(Double.toString(value)).setScale(2, RoundingMode.HALF_UP);
        }

        public static BigDecimal fromInteger(int value) {
            return new BigDecimal(Integer.toString(value));
        }
    }
}
