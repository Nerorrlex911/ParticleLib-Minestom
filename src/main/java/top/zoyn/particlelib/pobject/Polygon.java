package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个多边形
 */
public class Polygon extends ParticleObject implements Playable {

    private final List<Pos> pos;
    /**
     * 边数
     */
    private int side;
    private double step;
    private Vec currentVec;
    private int currentLoc = 0;
    private double length;
    private double currentStep = 0;

    /**
     * 构造一个多边形
     *
     * @param side   边数
     * @param origin 原点
     */
    public Polygon(int side, Pos origin) {
        this(side, origin, 0.02);
    }

    public Polygon(int side, Pos origin, double step) {
        if (side <= 2) {
            throw new IllegalArgumentException("边数不可为小于或等于2的数!");
        }
        this.side = side;
        setOrigin(origin);
        this.step = step;

        this.pos = new ArrayList<>();
        resetLocations();
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
        resetLocations();
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
        resetLocations();
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        List<Pos> temp = Lists.newArrayList();

        for (double angle = 0; angle <= 360; angle += 360D / side) {
            double radians = Math.toRadians(angle);
            double x = Math.cos(radians);
            double z = Math.sin(radians);

            temp.add(getOrigin().add(x, 0, z));
        }
        for (int i = 0; i < temp.size(); i++) {
            if (i + 1 == temp.size()) {
                Vec vecAB = temp.get(i).sub(temp.get(0)).asVec();
                double vectorLength = vecAB.length();
                vecAB.normalize();
                for (double j = 0; j < vectorLength; j += step) {
                    points.add(temp.get(0).add(vecAB.mul(j)));
                }
                break;
            }

            Vec vecAB = temp.get(i + 1).sub(temp.get(i)).asVec();
            double vectorLength = vecAB.length();
            vecAB.normalize();
            for (double j = 0; j < vectorLength; j += step) {
                points.add(temp.get(i).add(vecAB.mul(j)));
            }
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
        if (pos.isEmpty()) {
            return;
        }

        for (int i = 0; i < pos.size(); i++) {
            if (i + 1 == pos.size()) {
                buildLine(pos.get(i), pos.get(0), step);
                break;
            }
            buildLine(pos.get(i), pos.get(i + 1), step);
        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                Vec vecTemp = currentVec.normalize().mul(currentStep);
                spawnParticle(pos.get(currentLoc).add(vecTemp));

                // 重置
                if (currentStep > length) {
                    currentStep = 0D;
                    currentVec = VectorUtils.rotateAroundAxisY(currentVec, 360D / side);
                    currentLoc++;
                }
                // 在此处进行退出
                if (currentLoc == side) {
                    cancel();
                    return;
                }
                currentStep += step;
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        Vec vecTemp = currentVec.normalize().mul(currentStep);
        spawnParticle(pos.get(currentLoc).add(vecTemp));

        // 重置
        if (currentStep > length) {
            currentStep = 0D;
            currentVec = VectorUtils.rotateAroundAxisY(currentVec, 360D / side);
            currentLoc++;
        }
        if (currentLoc == side) {
            currentLoc = 0;
        }
        currentStep += step;
    }

    public void resetLocations() {
        pos.clear();

        for (double angle = 0; angle <= 360; angle += 360D / side) {
            double radians = Math.toRadians(angle);
            double x = Math.cos(radians);
            double z = Math.sin(radians);

            pos.add(getOrigin().add(x, 0, z));
        }

        currentVec = pos.get(1).sub(pos.get(0)).asVec();
        length = currentVec.length();
    }

    /**
     * 此方法只用于本 Polygon
     *
     * @param locA 点A
     * @param locB 点B
     * @param step 步长
     */
    private void buildLine(Pos locA, Pos locB, double step) {
        Vec vecAB = locB.sub(locA).asVec();
        double vectorLength = vecAB.length();
        vecAB.normalize();
        for (double i = 0; i < vectorLength; i += step) {
            spawnParticle(locA.add(vecAB.mul(i)));
        }
    }

}
