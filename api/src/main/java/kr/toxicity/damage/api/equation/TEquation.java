package kr.toxicity.damage.api.equation;

import lombok.RequiredArgsConstructor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.*;

public final class TEquation implements Equation<Float> {

    public static final Function MIN = new Function("min", 2) {
        @Override
        public double apply(double... doubles) {
            return min(doubles[0], doubles[1]);
        }
    };
    public static final Function MAX = new Function("max", 2) {
        @Override
        public double apply(double... doubles) {
            return max(doubles[0], doubles[1]);
        }
    };
    public static final Function CLAMP = new Function("clamp", 3) {
        @Override
        public double apply(double... doubles) {
            return clamp(doubles[0], doubles[1], doubles[2]);
        }
    };

    public static final TEquation ZERO = new TEquation("0");
    public static final TEquation ONE = new TEquation("1");
    public static final TEquation T = new TEquation("t");
    public static final TEquation FULL_LIGHT = new TEquation("15");

    private final Expression expression;

    public TEquation(@NotNull String equation) {
        expression = new ExpressionBuilder(equation)
                .variables("t", "r", "pi", "e")
                .functions(
                        MIN,
                        MAX,
                        CLAMP
                )
                .build();
    }

    public double evaluate(double t) {
        return new Expression(expression)
                .setVariables(Map.ofEntries(
                        Map.entry("t", t),
                        Map.entry("pi", PI),
                        Map.entry("e", E),
                        Map.entry("r", ThreadLocalRandom.current().nextDouble())
                ))
                .evaluate();
    }


    @Override
    public Equation.@NotNull Reader<Float> reader(int interval) {
        return new TReader(interval);
    }

    @RequiredArgsConstructor
    private class TReader implements Equation.Reader<Float>  {
        private final int interval;

        private double t;
        private final Expression copied = new Expression(expression)
                .setVariables(Map.ofEntries(
                        Map.entry("pi", PI),
                        Map.entry("e", E),
                        Map.entry("r", ThreadLocalRandom.current().nextDouble())
                ));


        @Override
        public @NotNull Float next() {
            var next = copied.setVariable("t", t).evaluate();
            t += interval;
            return (float) next;
        }
    }
}
