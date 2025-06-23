package kr.toxicity.damage.api.equation;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static java.util.Optional.ofNullable;

/**
 * Transformation equation
 * @param position position equation
 * @param rotation rotation equation
 * @param scale scale equation
 */
public record TransformationEquation(
        @NotNull VectorEquation position,
        @NotNull QuaternionEquation rotation,
        @NotNull VectorEquation scale
) implements Equation<Transformation> {

    /**
     * Creates from config
     * @param section section
     */
    public TransformationEquation(@NotNull ConfigurationSection section) {
        this(
                ofNullable(section.getConfigurationSection("position")).map(VectorEquation::new).orElse(VectorEquation.ZERO),
                ofNullable(section.getConfigurationSection("rotation")).map(QuaternionEquation::new).orElse(QuaternionEquation.ZERO),
                ofNullable(section.getConfigurationSection("scale")).map(VectorEquation::new).orElse(VectorEquation.ONE)
        );
    }

    @Override
    public @NotNull Reader<Transformation> reader(@NotNull EquationData data) {
        return new TReader(data);
    }

    private class TReader implements Reader<Transformation> {
        private final Reader<Vector3f> pe, se;
        private final Reader<Quaternionf> re;

        private TReader(@NotNull EquationData data) {
            pe = position.reader(data);
            re = rotation.reader(data);
            se = scale.reader(data);
        }

        @Override
        public @NotNull Transformation next() {
            return new Transformation(
                    pe.next(),
                    re.next(),
                    se.next(),
                    new Quaternionf()
            );
        }
    }
}
