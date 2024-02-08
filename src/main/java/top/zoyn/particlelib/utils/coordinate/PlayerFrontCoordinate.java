package top.zoyn.particlelib.utils.coordinate;

import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.LocationUtils;

/**
 * 表示一个玩家面前的坐标系
 * <p>将玩家面前作为一个新坐标系
 * 暂时不会受到仰俯角的控制</p>
 *
 * @author Zoyn
 */
public class PlayerFrontCoordinate implements Coordinate {
    /**
     * 原点
     */
    private final Pos originDot;
    /**
     * 旋转角度
     */
    private final double rotateAngle;

    public PlayerFrontCoordinate(Pos playerPos) {
        // 旋转的角度
        rotateAngle = playerPos.yaw() + 90D;
        originDot = playerPos
        // 重设仰俯角, 防止出现仰头后旋转角度不正确的问题
        .withPitch(0);
    }

    public Pos getOriginDot() {
        return originDot;
    }

    @Override
    public Pos newLocation(double x, double y, double z) {
        return LocationUtils.rotateLocationAboutPoint(originDot.add(y, z, x), rotateAngle, originDot);
    }

}
