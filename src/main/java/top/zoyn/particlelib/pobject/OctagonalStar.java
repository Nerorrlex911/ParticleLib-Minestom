package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个八角星
 *
 * @author Zoyn
 */
public class OctagonalStar extends ParticleObject {

    private double radius;
    private double step;

    public OctagonalStar(Pos origin, double radius, double step) {
        setOrigin(origin);

        this.radius = radius;
        this.step = step;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        double x = radius * Math.cos(Math.toRadians(45));
        double z = radius * Math.sin(Math.toRadians(45));

        double x2 = radius * Math.cos(Math.toRadians(45 * 3));
        double z2 = radius * Math.sin(Math.toRadians(45 * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        double length = START.length();
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= 8; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                points.add(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 135);
        }
        // 做一个对 Matrix 和 Increment 的兼容
        return points.stream().map(location -> {
            Pos showPos = location;
            if (hasMatrix()) {
                Vec v = new Vec(location.x() - getOrigin().x(), location.y() - getOrigin().y(), location.z() - getOrigin().z());
                Vec changed = getMatrix().applyVector(v);

                showPos = getOrigin().add(changed);
            }

            showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            return showPos;
        }).collect(Collectors.toList());
    }

    @Override
    public void show() {
        double x = radius * Math.cos(Math.toRadians(45));
        double z = radius * Math.sin(Math.toRadians(45));

        double x2 = radius * Math.cos(Math.toRadians(45 * 3));
        double z2 = radius * Math.sin(Math.toRadians(45 * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        double length = START.length();
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= 8; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                spawnParticle(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 135);
        }
    }

    public double getRadius() {
        return radius;
    }

    public OctagonalStar setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public double getStep() {
        return step;
    }

    public OctagonalStar setStep(double step) {
        this.step = step;
        return this;
    }
}
