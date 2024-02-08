package top.zoyn.particlelib.utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.minestom.server.coordinate.Vec;

/**
 * 坐标工具类
 *
 * @author Zoyn
 */
public class LocationUtils {

    /**
     * 在二维平面上利用给定的中心点逆时针旋转一个点
     *
     * @param pos 待旋转的点
     * @param angle    旋转角度
     * @param point    中心点
     * @return {@link Pos}
     */
    public static Pos rotateLocationAboutPoint(Pos pos, double angle, Pos point) {
        double radians = Math.toRadians(angle);
        double dx = pos.x() - point.x();
        double dz = pos.z() - point.z();

        double newX = dx * Math.cos(radians) - dz * Math.sin(radians) + point.x();
        double newZ = dz * Math.cos(radians) + dx * Math.sin(radians) + point.z();
        return new Pos(newX, pos.y(), newZ);
    }

    public static Pos rotateLocationAboutVector(Pos pos, Pos origin, double angle, Vec axis) {
        Vec vec = pos.sub(origin).asVec();
        return origin.add(VectorUtils.rotateAroundAxis(vec, axis, angle));
    }

    /**
     * 判断一个是否处在实体面向的扇形区域内
     * <p>
     * 通过反三角算向量夹角的算法
     *
     * @param target       目标坐标
     * @param livingEntity 实体
     * @param radius       扇形半径
     * @param angle        扇形角度
     * @return 如果处于扇形区域则返回 true
     */
    public static boolean isPointInEntitySector(Pos target, Entity livingEntity, double radius, double angle) {
        Vec v1 = livingEntity.getPosition().direction();
        Vec v2 = target.sub(livingEntity.getPosition()).asVec();

        double cosTheta = v1.dot(v2) / (v1.length() * v2.length());
        double degree = Math.toDegrees(Math.acos(cosTheta));
        // 距离判断
        if (target.distance(livingEntity.getPosition()) < radius) {
            // 向量夹角判断
            return degree < angle * 0.5f;
        }
        return false;
    }

    /**
     * 判断一个是否处在实体面向的扇形区域内
     * <p>
     * 通过叉乘算法
     *
     * @param target       目标坐标
     * @param livingEntity 实体
     * @param radius       扇形半径
     * @param angle        扇形角度
     * @return 如果处于扇形区域则返回 true
     */
    public static boolean isInsideSector(Pos target, Entity livingEntity, double radius, double angle) {
        Vec sectorStart = VectorUtils.rotateAroundAxisY(livingEntity.getPosition().direction(), -angle / 2);
        Vec sectorEnd = VectorUtils.rotateAroundAxisY(livingEntity.getPosition().direction(), angle / 2);

        Vec v = target.sub(livingEntity.getPosition()).asVec();

        boolean start = -sectorStart.x() * v.z() + sectorStart.z() * v.x() > 0;
        boolean end = -sectorEnd.x() * v.z() + sectorEnd.z() * v.x() > 0;
        return !start && end && target.distance(livingEntity.getPosition()) < radius;
    }
}
