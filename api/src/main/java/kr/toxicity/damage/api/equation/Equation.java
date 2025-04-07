package kr.toxicity.damage.api.equation;

import org.jetbrains.annotations.NotNull;

public interface Equation<T> {

    @NotNull Reader<T> reader(@NotNull EquationData data);

    interface Reader<T> {
        @NotNull T next();
    }
}
