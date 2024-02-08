package top.zoyn.particlelib.utils.coordinate;

import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.LocationUtils;

/**
 * 表示一个将X轴显示在玩家面前的坐标器
 * <p>自动修正在XZ平面上的粒子朝向</p>
 *
 * @author Zoyn
 */
public class PlayerFixedCoordinate implements Coordinate {

    /**
     * 原点
     */
    private final Pos originDot;
    /**
     * 旋转角度
     */
    private final double rotateAngle;

    public PlayerFixedCoordinate(Pos playerPos) {
        // 旋转的角度
        rotateAngle = playerPos.yaw();
        originDot = playerPos
        // 重设仰俯角, 防止出现仰头后旋转角度不正确的问题
        .withPitch(0);
    }

    public Pos getOriginDot() {
        return originDot;
    }

    @Override
    public Pos newLocation(double x, double y, double z) {
        return LocationUtils.rotateLocationAboutPoint(originDot.add(-x, y, z), rotateAngle, originDot);
    }
}