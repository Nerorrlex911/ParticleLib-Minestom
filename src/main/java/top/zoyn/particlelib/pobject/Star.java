package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个星星
 *
 * @author Zoyn
 */
public class Star extends ParticleObject implements Playable {

    private final double length;
    private final Vec changeableStart;
    private double radius;
    private double step;
    private int currentSide = 1;
    private double currentStep = 0;
    private Pos changableEnd;

    public Star(Pos origin) {
        this(origin, 1, 0.05);
    }

    public Star(Pos origin, double radius, double step) {
        setOrigin(origin);
        this.radius = radius;
        this.step = step;

        // 每条线的长度
        this.length = Math.sin(Math.toRadians(72)) * radius * 2;

        double x = radius * Math.cos(Math.toRadians(72));
        double z = radius * Math.sin(Math.toRadians(72));
        changeableStart = new Vec(radius * (Math.cos(Math.toRadians(72 * 3)) - x), 0, radius * (Math.sin(Math.toRadians(72 * 3)) - z));
        changeableStart.normalize();
        changableEnd = getOrigin().add(x, 0, z);
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        double x = radius * Math.cos(Math.toRadians(72));
        double z = radius * Math.sin(Math.toRadians(72));

        double x2 = radius * Math.cos(Math.toRadians(72 * 3));
        double z2 = radius * Math.sin(Math.toRadians(72 * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= 5; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                points.add(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 144);
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
        double x = radius * Math.cos(Math.toRadians(72));
        double z = radius * Math.sin(Math.toRadians(72));

        double x2 = radius * Math.cos(Math.toRadians(72 * 3));
        double z2 = radius * Math.sin(Math.toRadians(72 * 3));

        Vec START = new Vec(x2 - x, 0, z2 - z);
        START.normalize();
        Pos end = getOrigin().add(x, 0, z);

        for (int i = 1; i <= 5; i++) {
            for (double j = 0; j < length; j += step) {
                Vec vecTemp = START.mul(j);
                Pos spawnPos = end.add(vecTemp);

                spawnParticle(spawnPos);
            }
            Vec vecTemp = START.mul(length);
            end = end.add(vecTemp);

            VectorUtils.rotateAroundAxisY(START, 144);
        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            // 转弧度制
            final double radians = Math.toRadians(72);
            final double x = radius * Math.cos(radians);
            final double z = radius * Math.sin(radians);
            Pos end = getOrigin().add(x, 0, z);
            final Vec START = new Vec(radius * (Math.cos(Math.toRadians(72 * 3)) - x), 0, radius * (Math.sin(Math.toRadians(72 * 3)) - z));

            @Override
            public void run() {
                // 进行关闭
                if (currentSide >= 6) {
                    cancel();
                    return;
                }
                if (currentStep > length) {
                    // 切换到下一条边开始
                    currentSide += 1;
                    currentStep = 0;

                    Vec vecTemp = START.mul(length);
                    end = end.add(vecTemp);

                    VectorUtils.rotateAroundAxisY(START, 144);
                }
                Vec vecTemp = START.mul(currentStep);
                Pos spawnPos = end.add(vecTemp);

                spawnParticle(spawnPos);
                currentStep += step;
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        Vec vecTemp = changeableStart.mul(currentStep);
        Pos spawnPos = changableEnd.add(vecTemp);

        spawnParticle(spawnPos);
        currentStep += step;

        if (currentStep >= length) {
            // 切换到下一条边开始
            currentSide += 1;
            currentStep = 0;

            vecTemp = changeableStart.mul(length);
            changableEnd = changableEnd.add(vecTemp);

            // 由于 play 的向量是不需要重置的, 因此可以一直旋转 144 然后画线即可
            VectorUtils.rotateAroundAxisY(changeableStart, 144);
        }
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }
}
