package top.zoyn.particlelib.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import top.zoyn.particlelib.pobject.Astroid;

public class ParticleCommand extends Command {
    Tag<Integer> step = Tag.Integer("step").defaultValue(0);
    public ParticleCommand() {
        super("particlelib", "particle", "plib");
        ArgumentString particleId = ArgumentType.String("particleId");
        ArgumentInteger size = ArgumentType.Integer("size");
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) return;
            switch (context.get(particleId)) {
                case "astroid":
                    Astroid astroid = new Astroid(context.get(size), ((Player) sender).getPosition().add(0,1,0), 10);
                    astroid.setParticleSpawner((pos, audience) -> {
                        ParticlePacket particlePacket = new ParticlePacket(
                                Particle.END_ROD, pos,
                                Vec.ZERO, 0.0f, 1
                        );
                        sender.sendMessage("playing Astroid: "+pos);
                        ((Player) sender).sendPacket(particlePacket);
//                        audience.sendPacketToViewers(particlePacket);
                    });
                    astroid.setAudience((Player) sender);
                    ((Player) sender).scheduler().submitTask(() -> {
                        int stepAmount = sender.getTag(step);
                        if(stepAmount > 360) {
                            sender.setTag(step, 0);
                            return TaskSchedule.stop();
                        }
                        sender.setTag(step, stepAmount + 10);
                        astroid.playNextPoint();
                        return TaskSchedule.nextTick();
                    });
            }
        }, particleId,size);
    }
}
