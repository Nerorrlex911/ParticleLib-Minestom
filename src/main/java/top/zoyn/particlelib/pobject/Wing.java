package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.utils.VectorUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个翅膀
 *
 * @author Zoyn
 */
public class Wing extends ParticleObject {

    /**
     * 用于缓存所有的点的向量
     */
    private final List<Vec> vecs;
    /**
     * 翅膀的图案
     */
    private List<String> pattern;
    /**
     * 粒子之间的距离
     */
    private double interval;
    /**
     * 最小起始旋转角
     */
    private double minRotAngle;
    /**
     * 最大旋转角
     */
    private double maxRotAngle;
    /**
     * 翅膀是否要进行旋转
     */
    private boolean swing;

    private double currentAngle;
    private boolean increase;

    public Wing(Pos origin, List<String> pattern) {
        this(origin, pattern, 30D, 60D, 0.2D);
    }

    public Wing(Pos origin, List<String> pattern, double minRotAngle, double maxRotAngle, double interval) {
        setOrigin(origin);
        this.pattern = pattern;
        this.interval = interval;
        this.minRotAngle = -minRotAngle;
        this.maxRotAngle = -maxRotAngle;

        currentAngle = -minRotAngle;
        increase = false;
        vecs = Lists.newArrayList();
        swing = true;
        resetWing();
    }

    @Override
    public List<Pos> calculateLocations() {
        resetWing();

        List<Pos> points = Lists.newArrayList();
        for (Vec vec : vecs) {
            if (getEntity() != null) {
                points.add(getOrigin().add(VectorUtils.rotateVector(vec, getOrigin().yaw() - 90 + (float) currentAngle, 0F)));
                points.add(getOrigin().add(VectorUtils.rotateVector(vec.withX(-vec.x()), getOrigin().yaw() - 90 - (float) currentAngle, 0F)));
                continue;
            }
            points.add(getOrigin().add(VectorUtils.rotateVector(vec, (float) currentAngle, 0F)));
            points.add(getOrigin().add(VectorUtils.rotateVector(vec.withX(-vec.x()), -(float) currentAngle, 0F)));
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
        for (Vec vec : vecs) {
            if (getEntity() != null) {
                spawnParticle(getOrigin().add(VectorUtils.rotateVector(vec, getOrigin().yaw() - 90 + (float) currentAngle, 0F)));
                spawnParticle(getOrigin().add(VectorUtils.rotateVector(vec.withX(-vec.x()), getOrigin().yaw() - 90 - (float) currentAngle, 0F)));
                continue;
            }
            spawnParticle(getOrigin().add(VectorUtils.rotateVector(vec, (float) currentAngle, 0F)));
            spawnParticle(getOrigin().add(VectorUtils.rotateVector(vec.withX(-vec.x()), -(float) currentAngle, 0F)));
        }
        if (!swing) {
            return;
        }
        if (!increase) {
            currentAngle--;
        } else {
            currentAngle++;
        }

        if (currentAngle >= minRotAngle) {
            increase = false;
        }
        if (currentAngle <= maxRotAngle) {
            increase = true;
        }
    }

    /**
     * 利用图案来计算出每个粒子的向量
     */
    public void resetWing() {
        vecs.clear();

        for (int i = 0; i < pattern.size(); i++) {
            String line = pattern.get(i);
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (!Character.isWhitespace(c)) {
                    double x = interval * (j + 1);
                    double y = interval * (pattern.size() - i);
                    Vec vec = new Vec(x, y, 0);
                    vecs.add(vec);
                }
            }
        }
    }

    public List<String> getPattern() {
        return pattern;
    }

    public void setPattern(List<String> pattern) {
        this.pattern = pattern;
        resetWing();
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public double getMinRotAngle() {
        return minRotAngle;
    }

    public void setMinRotAngle(double minRotAngle) {
        this.minRotAngle = minRotAngle;
    }

    public double getMaxRotAngle() {
        return maxRotAngle;
    }

    public void setMaxRotAngle(double maxRotAngle) {
        this.maxRotAngle = maxRotAngle;
    }

    public boolean isSwing() {
        return swing;
    }

    public void setSwing(boolean swing) {
        this.swing = swing;
    }
}
