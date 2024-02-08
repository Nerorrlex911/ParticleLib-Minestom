package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.util.List;
import java.util.stream.Collectors;

public class Grid extends ParticleObject {

    private final double gridLength;
    private Pos minimumPos;
    private Pos maximumPos;
    private boolean isXDimension = false;
    private boolean isYDimension = false;

    public Grid(Pos minimumPos, Pos maximumPos) {
        this(minimumPos, maximumPos, 1.2D);
    }

    public Grid(Pos minimumPos, Pos maximumPos, double gridLength) {
        this.minimumPos = minimumPos;
        this.maximumPos = maximumPos;
        // 平面检查
        if (minimumPos.getBlockX() != maximumPos.getBlockX()) {
            if (minimumPos.getBlockZ() != maximumPos.getBlockZ()) {
                if (minimumPos.getBlockY() != maximumPos.getBlockY()) {
                    throw new IllegalArgumentException("请将两点设定在X平面, Y平面或Z平面上(即一个方块的面上)");
                }
            }
        }
        if (minimumPos.getBlockX() == maximumPos.getBlockX()) {
            isXDimension = false;
        }
        if (minimumPos.getBlockY() == maximumPos.getBlockY()) {
            isYDimension = true;
        }
        if (minimumPos.getBlockZ() == maximumPos.getBlockZ()) {
            isXDimension = true;
        }

        this.gridLength = gridLength;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        // 为防止给定的最小和最高点出现反向的情况, 这里做了个查找操作
        Pos minPos = findMinimumLocation();
        Pos maxPos = findMaximumLocation();

        double height;
        double width;

        // 在Y平面下有点不一样
        if (isYDimension) {
            height = Math.abs(minPos.x() - maxPos.x());
            width = Math.abs(minPos.z() - maxPos.z());
        } else {
            height = Math.abs(maximumPos.y() - minimumPos.y());
            if (isXDimension) {
                width = Math.abs(maximumPos.x() - minimumPos.x());
            } else {
                width = Math.abs(maximumPos.z() - minimumPos.z());
            }
        }
        int heightSideLine = (int) (height / gridLength);
        int widthSideLine = (int) (width / gridLength);

        if (isYDimension) {
            for (int i = 1; i <= heightSideLine; i++) {
                Vec vec = maxPos.sub(minPos).asVec();
                vec.withZ(0).normalize();

                Pos start = minPos.add(0, 0, i * gridLength);
                for (double j = 0; j < width; j += 0.2) {
                    points.add(start.add(vec.mul(j)));
                }
            }

            for (int i = 1; i <= widthSideLine; i++) {
                Vec vec = maxPos.sub(minPos).asVec();
                vec.withX(0).normalize();
                Pos start = minPos.add(i * gridLength, 0, 0);

                for (double j = 0; j < height; j += 0.2) {
                    points.add(start.add(vec.mul(j)));
                }
            }
            return points;
        }

        for (int i = 1; i <= heightSideLine; i++) {
            Vec vec = maxPos.sub(minPos).asVec();
            vec.withY(0).normalize();

            Pos start = minPos.add(0, i * gridLength, 0);
            for (double j = 0; j < width; j += 0.2) {
                points.add(start.add(vec.mul(j)));
            }
        }

        for (int i = 1; i <= widthSideLine; i++) {
            Vec vec = maxPos.sub(minPos).asVec();
            Pos start;
            if (isXDimension) {
                vec.withX(0).normalize();
                start = minPos.add(i * gridLength, 0, 0);
            } else {
                vec.withZ(0).normalize();
                start = minPos.add(0, 0, i * gridLength);
            }

            for (double j = 0; j < height; j += 0.2) {
                points.add(start.add(vec.mul(j)));
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
        // 为防止给定的最小和最高点出现反向的情况, 这里做了个查找操作
        Pos minPos = findMinimumLocation();
        Pos maxPos = findMaximumLocation();

        double height;
        double width;

        // 在Y平面下有点不一样
        if (isYDimension) {
            height = Math.abs(minPos.x() - maxPos.x());
            width = Math.abs(minPos.z() - maxPos.z());
        } else {
            height = Math.abs(maximumPos.y() - minimumPos.y());
            if (isXDimension) {
                width = Math.abs(maximumPos.x() - minimumPos.x());
            } else {
                width = Math.abs(maximumPos.z() - minimumPos.z());
            }
        }
        int heightSideLine = (int) (height / gridLength);
        int widthSideLine = (int) (width / gridLength);

        if (isYDimension) {
            for (int i = 1; i <= heightSideLine; i++) {
                Vec vec = maxPos.sub(minPos).asVec();
                vec.withZ(0).normalize();

                Pos start = minPos.add(0, 0, i * gridLength);
                for (double j = 0; j < width; j += 0.2) {
                    spawnParticle(start.add(vec.mul(j)));

                }
            }

            for (int i = 1; i <= widthSideLine; i++) {
                Vec vec = maxPos.sub(minPos).asVec();
                vec.withX(0).normalize();
                Pos start = minPos.add(i * gridLength, 0, 0);

                for (double j = 0; j < height; j += 0.2) {
                    spawnParticle(start.add(vec.mul(j)));
                }
            }
            return;
        }

        for (int i = 1; i <= heightSideLine; i++) {
            Vec vec = maxPos.sub(minPos).asVec();
            vec.withY(0).normalize();

            Pos start = minPos.add(0, i * gridLength, 0);
            for (double j = 0; j < width; j += 0.2) {
                spawnParticle(start.add(vec.mul(j)));
            }
        }

        for (int i = 1; i <= widthSideLine; i++) {
            Vec vec = maxPos.sub(minPos).asVec();
            Pos start;
            if (isXDimension) {
                vec.withX(0).normalize();
                start = minPos.add(i * gridLength, 0, 0);
            } else {
                vec.withZ(0).normalize();
                start = minPos.add(0, 0, i * gridLength);
            }

            for (double j = 0; j < height; j += 0.2) {
                spawnParticle(start.add(vec.mul(j)));
            }
        }
    }

    private Pos findMinimumLocation() {
        double minX = Math.min(minimumPos.x(), maximumPos.x());
        double minY = Math.min(minimumPos.y(), maximumPos.y());
        double minZ = Math.min(minimumPos.z(), maximumPos.z());

        return new Pos(minimumPos.getWorld(), minX, minY, minZ);
    }

    private Pos findMaximumLocation() {
        double maxX = Math.max(minimumPos.x(), maximumPos.x());
        double maxY = Math.max(minimumPos.y(), maximumPos.y());
        double maxZ = Math.max(minimumPos.z(), maximumPos.z());

        return new Pos(minimumPos.getWorld(), maxX, maxY, maxZ);
    }

    public Pos getMinimumLocation() {
        return minimumPos;
    }

    public void setMinimumLocation(Pos minimumPos) {
        this.minimumPos = minimumPos;
    }

    public Pos getMaximumLocation() {
        return maximumPos;
    }

    public void setMaximumLocation(Pos maximumPos) {
        this.maximumPos = maximumPos;
    }

}
