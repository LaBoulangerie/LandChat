package net.laboulangerie.landschat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;

public class AsyncChatHookEvent extends Event {

    private static HandlerList HANDLERS = new HandlerList();

    private AsyncChatEvent event;

    public AsyncChatHookEvent(AsyncChatEvent event, boolean async) {
        super(async);
        this.event = event;
    }

    public AsyncChatEvent getEvent() {
        return event;
    }

    public Component getMessage() {
        return event.message();
    }

    public Player getPlayer() {
        return event.getPlayer();
    }

    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
