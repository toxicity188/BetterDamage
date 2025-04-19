package kr.toxicity.damage.api.equation;

import org.jetbrains.annotations.NotNull;

/**
 * Equation
 * @param <T> type
 */
public interface Equation<T> {

    /**
     * Creates equation reader
     * @param data data
     * @return reader
     */
    @NotNull Reader<T> reader(@NotNull EquationData data);

    /**
     * Equation reader
     * @param <T> type
     */
    interface Reader<T> {
        /**
         * Gets next element
         * @return next element
         */
        @NotNull T next();
    }
}
