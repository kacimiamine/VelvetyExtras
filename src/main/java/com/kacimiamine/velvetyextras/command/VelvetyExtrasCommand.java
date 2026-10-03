package com.kacimiamine.velvetyextras.command;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.permissions.Permission;

public class VelvetyExtrasCommand {

    private static final Permission reloadPermission = new Permission("velvetyextras.reload");

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("velvetyextras")
                .requires(css -> css.getSender().hasPermission("velvetyextras.command"))
                .then(Commands.literal("reload")
                        .requires(css -> css.getSender().hasPermission(reloadPermission))
                        .executes(VelvetyExtrasCommand::executeReload)
                ).build();
    }

    private static int executeReload(CommandContext<CommandSourceStack> ctx) {
        VelvetyExtras.getInstance().reload();

        ctx.getSource().getSender().sendMessage(Component.text("VelvetyExtras successfully reloaded."));

        return Command.SINGLE_SUCCESS;
    }
}
