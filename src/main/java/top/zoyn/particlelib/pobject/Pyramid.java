package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个N棱锥特效
 *
 * @author Zoyn
 */
public class Pyramid extends ParticleObject {

    private final List<Pos> pos;
    private int side;
    private double height;
    private double step;
    private double radius;
    private Pos upLoc;

    public Pyramid(Pos origin, int side) {
        this(origin, side, 1, 1);
    }

    public Pyramid(Pos origin, int side, double radius, double height) {
        this(origin, side, radius, height, 0.02);
    }

    /**
     * 表示一个棱锥特效
     *
     * @param origin 棱锥底面中心点
     * @param side   棱的个数
     * @param radius 底面半径, 中心点到任意一个角的长度
     * @param height 底面中心点到最上方顶点的长度
     * @param step   粒子的间距
     */
    public Pyramid(Pos origin, int side, double radius, double height, double step) {
        if (side <= 2) {
            throw new IllegalArgumentException("边数不可为小于或等于2的数!");
        }
        this.side = side;
        this.height = height;
        this.step = step;
        this.radius = radius;

        this.pos = new ArrayList<>();
        setOrigin(origin);
    }

    public int getSide() {
        return side;
    }

    public Pyramid setSide(int side) {
        this.side = side;
        resetLocations();
        return this;

    }

    public double getHeight() {
        return height;
    }

    public Pyramid setHeight(double height) {
        this.height = height;
        upLoc = getOrigin().add(0, height, 0);
        resetLocations();
        return this;
    }

    public double getStep() {
        return step;
    }

    public Pyramid setStep(double step) {
        this.step = step;
        resetLocations();
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Pyramid setRadius(double radius) {
        this.radius = radius;
        resetLocations();
        return this;
    }

    @Override
    public ParticleObject setOrigin(Pos origin) {
        super.setOrigin(origin);
        // 重置最上方的 Loc
        upLoc = origin.add(0, height, 0);

        resetLocations();
        return this;
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

//        buildLine(upLoc, pos.get(i), step);
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

            // 棱长部分
            vecAB = temp.get(i).sub(upLoc).asVec();
            vectorLength = vecAB.length();
            vecAB.normalize();
            for (double j = 0; j < vectorLength; j += step) {
                points.add(upLoc.add(vecAB.mul(j)));
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
            // 棱
            buildLine(upLoc, pos.get(i), step);
            // 底面
            if (i + 1 == pos.size()) {
                buildLine(pos.get(i), pos.get(0), step);
                break;
            }
            buildLine(pos.get(i), pos.get(i + 1), step);
        }
    }

    public void resetLocations() {
        pos.clear();

        for (double angle = 0; angle <= 360; angle += 360D / side) {
            double radians = Math.toRadians(angle);
            double x = radius * Math.cos(radians);
            double z = radius * Math.sin(radians);

            pos.add(getOrigin().add(x, 0, z));
        }
    }

    /**
     * 此方法只用于 Pyramid
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
