package top.zoyn.particlelib.utils.coordinate;

import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.LocationUtils;

/**
 * 表示一个玩家后背坐标系
 * <p>将玩家背后转换为一个直角坐标系</p>
 *
 * @author Zoyn
 */
public class PlayerBackCoordinate implements Coordinate {

    private final Pos originDot;
    private final double rotateAngle;

    public PlayerBackCoordinate(Pos playerPos) {
        // 旋转的角度
        rotateAngle = playerPos.yaw();
        originDot = playerPos
        // 重设仰俯角
        .withPitch(0);
        // 使原点与玩家有一点点距离
        originDot.add(originDot.direction().mul(-0.3));
    }

    @Override
    public Pos newLocation(double x, double y, double z) {
        return LocationUtils.rotateLocationAboutPoint(originDot.add(-x, y, z), rotateAngle, originDot);
    }

}
