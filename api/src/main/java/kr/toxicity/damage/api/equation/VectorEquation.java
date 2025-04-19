package kr.toxicity.damage.api.equation;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static java.util.Objects.requireNonNull;

/**
 * Vector equation
 * @param x x
 * @param y y
 * @param z z
 */
public record VectorEquation(
        @NotNull TEquation x,
        @NotNull TEquation y,
        @NotNull TEquation z
) implements Equation<Vector3f> {

    /**
     * Zero
     */
    public static final VectorEquation ZERO = new VectorEquation(
            TEquation.ZERO
    );

    /**
     * One
     */
    public static final VectorEquation ONE = new VectorEquation(
            TEquation.ONE
    );

    /**
     * Creates scala equation
     * @param scala scala
     */
    public VectorEquation(@NotNull TEquation scala) {
        this(scala, scala, scala);
    }

    /**
     * Creates vector equation
     * @param section config
     */
    public VectorEquation(@NotNull ConfigurationSection section) {
        this(
                new TEquation(requireNonNull(section.getString("x"), "x must be not null.")),
                new TEquation(requireNonNull(section.getString("y"), "y must be not null.")),
                new TEquation(requireNonNull(section.getString("z"), "z must be not null."))
        );
    }

    @Override
    public @NotNull Reader<Vector3f> reader(@NotNull EquationData data) {
        return new VReader(data);
    }

    private class VReader implements Reader<Vector3f> {

        private final Reader<Float> xe, ye, ze;

        private VReader(@NotNull EquationData data) {
            xe = x.reader(data);
            ye = y.reader(data);
            ze = z.reader(data);
        }

        @Override
        public @NotNull Vector3f next() {
            return new Vector3f(xe.next(), ye.next(), ze.next());
        }
    }
}
