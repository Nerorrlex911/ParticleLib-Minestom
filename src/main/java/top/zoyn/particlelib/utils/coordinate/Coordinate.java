package top.zoyn.particlelib.utils.coordinate;

import net.minestom.server.coordinate.Pos;

/**
 * 表示一个坐标器
 *
 * @author Zoyn
 */
public interface Coordinate {

    Pos newLocation(double x, double y, double z);

}
