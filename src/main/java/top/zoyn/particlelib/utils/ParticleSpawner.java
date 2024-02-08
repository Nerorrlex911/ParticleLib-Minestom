package top.zoyn.particlelib.utils;


import net.kyori.adventure.audience.Audience;
import net.minestom.server.coordinate.Pos;

public interface ParticleSpawner {
    void spawn(Pos pos, Audience audience);
}
