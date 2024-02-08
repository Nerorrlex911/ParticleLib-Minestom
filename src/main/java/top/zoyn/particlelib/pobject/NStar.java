package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个N角星, N必须是一个奇数
 *
 * @author Zoyn
 */
public class NStar extends ParticleObject {

    private double angle;
    private int corner;
    private double radius;
    private double step;

    public NStar(Pos origin, int corner, double radius, double step) {
        if (corner % 2 == 0) {
            throw new IllegalArgumentException("N角星的 corner 参数必须为一个奇数整数!");
        }
        setOrigin(origin);
        this.corner = corner;
        this.radius = radius;
        this.step = step;

        this.angle = 360D / corner;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        double x = radius * Math.cos(Math.toRadians(angle));
        double z = radius * Math.sin(Math.toRadians(angle));

        double x2 = radius * Math.cos(Math.toRadians(angle * 3));
        double z2 = radius * Math.sin(Math.toRadians(angle * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        double length = START.length();
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= corner; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                points.add(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 180 - angle / 2);
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
        double x = radius * Math.cos(Math.toRadians(angle));
        double z = radius * Math.sin(Math.toRadians(angle));

        double x2 = radius * Math.cos(Math.toRadians(angle * 3));
        double z2 = radius * Math.sin(Math.toRadians(angle * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        double length = START.length();
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= corner; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                spawnParticle(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 180 - angle / 2);
        }
    }

    public double getAngle() {
        return angle;
    }

    public NStar setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public int getCorner() {
        return corner;
    }

    public NStar setCorner(int corner) {
        this.corner = corner;
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public NStar setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public double getStep() {
        return step;
    }

    public NStar setStep(double step) {
        this.step = step;
        return this;
    }
}
