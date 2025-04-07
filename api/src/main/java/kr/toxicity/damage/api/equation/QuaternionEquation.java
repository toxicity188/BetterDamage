package kr.toxicity.damage.api.equation;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record QuaternionEquation(
        @NotNull VectorEquation eulerAngle
) implements Equation<Quaternionf> {

    public static final float DEGREE_TO_RADIAN = (float) Math.PI / 180F;

    public static final QuaternionEquation ZERO = new QuaternionEquation(
            TEquation.ZERO
    );

    public QuaternionEquation(@NotNull TEquation scala) {
        this(new VectorEquation(scala));
    }

    public QuaternionEquation(@NotNull ConfigurationSection section) {
        this(new VectorEquation(section));
    }

    @Override
    public @NotNull Reader<Quaternionf> reader(@NotNull EquationData data) {
        return new QReader(data);
    }

    private class QReader implements Reader<Quaternionf> {
        private final Reader<Vector3f> ve;

        private QReader(@NotNull EquationData data) {
            ve = eulerAngle.reader(data);
        }

        @Override
        public @NotNull Quaternionf next() {
            var angle = ve.next();
            return new Quaternionf()
                    .rotateZYX(
                            angle.z * DEGREE_TO_RADIAN,
                            angle.y * DEGREE_TO_RADIAN,
                            angle.x * DEGREE_TO_RADIAN
                    );
        }
    }
}
