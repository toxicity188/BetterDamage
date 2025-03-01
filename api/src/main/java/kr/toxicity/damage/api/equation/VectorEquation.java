package kr.toxicity.damage.api.equation;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static java.util.Objects.requireNonNull;

public record VectorEquation(
        @NotNull TEquation x,
        @NotNull TEquation y,
        @NotNull TEquation z
) implements Equation<Vector3f> {

    public static final VectorEquation ZERO = new VectorEquation(
            TEquation.ZERO
    );
    public static final VectorEquation ONE = new VectorEquation(
            TEquation.ONE
    );

    public VectorEquation(@NotNull TEquation scala) {
        this(scala, scala, scala);
    }

    public VectorEquation(@NotNull ConfigurationSection section) {
        this(
                new TEquation(requireNonNull(section.getString("x"), "x must be not null.")),
                new TEquation(requireNonNull(section.getString("y"), "y must be not null.")),
                new TEquation(requireNonNull(section.getString("z"), "z must be not null."))
        );
    }

    @Override
    public @NotNull Reader<Vector3f> reader(int interval) {
        return new VReader(interval);
    }

    private class VReader implements Reader<Vector3f> {

        private final Reader<Float> xe, ye, ze;

        private VReader(int interval) {
            xe = x.reader(interval);
            ye = y.reader(interval);
            ze = z.reader(interval);
        }

        @Override
        public @NotNull Vector3f next() {
            return new Vector3f(xe.next(), ye.next(), ze.next());
        }
    }
}
