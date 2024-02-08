package top.zoyn.particlelib.utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

/**
 * 向量工具类
 *
 * @author Zoyn
 */
public class VectorUtils {

    /**
     * 只通过数字本身相减得到向量, 减少额外克隆的损耗
     *
     * @param start 起点
     * @param end   终点
     * @return {@link Vec}
     */
    public static Vec createVector(Pos start, Pos end) {
        return new Vec(end.x() - start.x(), end.y() - start.y(), end.z() - start.z());
    }

    public static Vec getLeftDirection(Pos pos) {
        return rotateAroundAxisY(pos.direction(), 90);
    }

    public static Vec getRightDirection(Pos pos) {
        return rotateAroundAxisY(pos.direction(), -90);
    }

    /**
     * 得到一个单位为 1 的向上的向量
     *
     * @return {@link Vec}
     */
    public static Vec getUpVector() {
        return getUpVector(1);
    }

    public static Vec getUpVector(double multiply) {
        return new Vec(0, 1, 0).mul(multiply);
    }

    public static Vec rotateAroundAxisX(Vec v, double angle) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.y() * cos - v.z() * sin;
        double z = v.y() * sin + v.z() * cos;
        return v.withY(y).withZ(z);
    }

    public static Vec rotateAroundAxisY(Vec v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.x() * cos + v.z() * sin;
        double z = v.x() * -sin + v.z() * cos;
        return v.withX(x).withZ(z);
    }

    public static Vec rotateAroundAxisZ(Vec v, double angle) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.x() * cos - v.y() * sin;
        double y = v.x() * sin + v.y() * cos;
        return v.withX(x).withY(y);
    }

    /**
     * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z angles.
     * <p>
     * Thanks to SexyToad!
     * <p>
     * 将一个非单位向量使用yaw和pitch来代替X, Y, Z的角旋转方式
     *
     * @param v            向量
     * @param yawDegrees   yaw的角度
     * @param pitchDegrees pitch的角度
     * @return {@link Vec}
     */
    public static Vec rotateVector(Vec v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_Axis rotation (Pitch)
        initialX = v.x();
        initialY = v.y();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_Axis rotation (Yaw)
        initialZ = v.z();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vec(x, y, z);
    }

    /**
     * 判断一个向量是否已单位化
     *
     * @param vec 向量
     * @return 是否单位化
     */
    public static boolean isNormalized(Vec vec) {
        return vec.isNormalized();
    }

    /**
     * 空间向量绕任一向量旋转
     *
     * @param vec 待旋转向量
     * @param axis   旋转轴向量
     * @param angle  旋转角度
     * @return {@link Vec}
     */
    public static Vec rotateAroundAxis(Vec vec, Vec axis, double angle) {
        return rotateAroundNonUnitAxis(vec, isNormalized(axis) ? axis : axis.normalize(), angle);
    }

    /**
     * 空间向量绕任一向量旋转
     * <p>注: 这里的旋转轴必须为已单位化才可使用!</p>
     * <p>
     * 罗德里格旋转公式: https://zh.wikipedia.org/wiki/%E7%BD%97%E5%BE%B7%E9%87%8C%E6%A0%BC%E6%97%8B%E8%BD%AC%E5%85%AC%E5%BC%8F
     * <p>
     * 正常人能看懂的: https://www.cnblogs.com/wubugui/p/3734627.html
     *
     * @param vec 要旋转的向量
     * @param axis   旋转轴向量
     * @param angle  旋转角度
     * @return {@link Vec}
     */
    public static Vec rotateAroundNonUnitAxis(Vec vec, Vec axis, double angle) {
        double x = vec.x(), y = vec.y(), z = vec.z();
        double x2 = axis.x(), y2 = axis.y(), z2 = axis.z();

        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = vec.dot(axis);

        double xPrime = x2 * dotProduct * (1d - cosTheta)
                + x * cosTheta
                + (-z2 * y + y2 * z) * sinTheta;
        double yPrime = y2 * dotProduct * (1d - cosTheta)
                + y * cosTheta
                + (z2 * x - x2 * z) * sinTheta;
        double zPrime = z2 * dotProduct * (1d - cosTheta)
                + z * cosTheta
                + (-y2 * x + x2 * y) * sinTheta;

        return vec.withX(xPrime).withY(yPrime).withZ(zPrime);
    }

}
