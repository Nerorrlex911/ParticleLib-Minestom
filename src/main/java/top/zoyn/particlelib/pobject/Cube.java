package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个立方体特效
 *
 * @author Zoyn
 */
public class Cube extends ParticleObject {

    /**
     * 向上的向量
     */
    private static final Vec UP = new Vec(0, 1, 0).normalize();
    /**
     * 向 X正半轴 的向量
     */
    private static final Vec RIGHT = new Vec(1, 0, 0).normalize();
    private Pos minLoc;
    private Pos maxLoc;
    private double step;

    public Cube(Pos minLoc, Pos maxLoc) {
        this(minLoc, maxLoc, 0.2D);
    }

    /**
     * 构造一个立方体
     *
     * @param minLoc 一个点
     * @param maxLoc 另外一个点
     * @param step   绘制边框时的步进长度
     */
    public Cube(Pos minLoc, Pos maxLoc, double step) {
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.step = step;

        setOrigin(minLoc.add(VectorUtils.createVector(minLoc, maxLoc).mul(0.5)));
    }

    public Pos getMinLocation() {
        return minLoc;
    }

    public void setMinLocation(Pos minLoc) {
        this.minLoc = minLoc;
    }

    public Pos getMaxLocation() {
        return maxLoc;
    }

    public void setMaxLocation(Pos maxLoc) {
        this.maxLoc = maxLoc;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        // 获得最大最小的两个点
        double minX = Math.min(minLoc.x(), maxLoc.x());
        double minY = Math.min(minLoc.y(), maxLoc.y());
        double minZ = Math.min(minLoc.z(), maxLoc.z());

        double maxX = Math.max(minLoc.x(), maxLoc.x());
        double maxY = Math.max(minLoc.y(), maxLoc.y());
        double maxZ = Math.max(minLoc.z(), maxLoc.z());

        Pos minLoc = new Pos(minX, minY, minZ);

        // 获得立方体的 长 宽 高
        double width = maxX - minX;
        double height = maxY - minY;
        double depth = maxZ - minZ;

        // 此处的 newOrigin是底部的四个点
        Pos newOrigin = minLoc;
        double length;
        // 这里直接得到向X正半轴方向的向量
        Vec vec = RIGHT;
        for (int i = 1; i <= 4; i++) {
            if (i % 2 == 0) {
                length = depth;
            } else {
                length = width;
            }

            // 4条高
            for (double j = 0; j < height; j += step) {
                points.add(newOrigin.add(UP.mul(j)));
            }

            // 第n条边
            for (double j = 0; j < length; j += step) {
                Pos spawnLoc = newOrigin.add(vec.mul(j));
                points.add(spawnLoc);
                points.add(spawnLoc.add(0, height, 0));
            }
            // 获取结束时的坐标
            newOrigin = newOrigin.add(vec.mul(length));
            vec = VectorUtils.rotateAroundAxisY(vec, 90D);
        }

        // 做一个对 Matrix 和 Increment 的兼容
        return points.stream().map(location -> {
            Pos showPos = location;
            if (hasMatrix()) {
                Vec v = new Vec(location.x() - getOrigin().x(), location.y() - getOrigin().y(), location.z() - getOrigin().z());
                Vec changed = getMatrix().applyVector(v);

                showPos = getOrigin().add(changed);
            }

            showPos = showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            return showPos;
        }).collect(Collectors.toList());
    }

    @Override
    public void show() {
        // 获得最大最小的两个点
        double minX = Math.min(minLoc.x(), maxLoc.x());
        double minY = Math.min(minLoc.y(), maxLoc.y());
        double minZ = Math.min(minLoc.z(), maxLoc.z());

        double maxX = Math.max(minLoc.x(), maxLoc.x());
        double maxY = Math.max(minLoc.y(), maxLoc.y());
        double maxZ = Math.max(minLoc.z(), maxLoc.z());

        Pos minLoc = new Pos(minX, minY, minZ);

        // 获得立方体的 长 宽 高
        double width = maxX - minX;
        double height = maxY - minY;
        double depth = maxZ - minZ;

        // 此处的 newOrigin是底部的四个点
        Pos newOrigin = minLoc;
        double length;
        // 这里直接得到向X正半轴方向的向量
        Vec vec = RIGHT;
        for (int i = 1; i <= 4; i++) {
            if (i % 2 == 0) {
                length = depth;
            } else {
                length = width;
            }

            // 4条高
            for (double j = 0; j < height; j += step) {
                spawnParticle(newOrigin.add(UP.mul(j)));
            }

            // 第n条边
            for (double j = 0; j < length; j += step) {
                Pos spawnLoc = newOrigin.add(vec.mul(j));
                spawnParticle(spawnLoc);
                spawnParticle(spawnLoc.add(0, height, 0));
            }
            // 获取结束时的坐标
            newOrigin = newOrigin.add(vec.mul(length));
            vec = VectorUtils.rotateAroundAxisY(vec, 90D);
        }
    }
}
