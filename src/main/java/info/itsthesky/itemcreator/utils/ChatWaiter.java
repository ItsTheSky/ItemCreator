package info.itsthesky.itemcreator.utils;

import info.itsthesky.itemcreator.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChatWaiter implements Listener {
    private final Predicate<AsyncPlayerChatEvent> verify;
    private final Consumer<AsyncPlayerChatEvent> consumer;
    private final boolean isDeleted;
    private final boolean isCancelled;
    private boolean alreadyDeleted = false;

    public ChatWaiter(Player player, Consumer<AsyncPlayerChatEvent> consumer, boolean isDeleted, boolean isCancelled) {
        this(ev -> ev.getPlayer().equals(player), consumer, isDeleted, isCancelled);
    }

    public ChatWaiter(Predicate<AsyncPlayerChatEvent> verify, Consumer<AsyncPlayerChatEvent> consumer, boolean isDeleted, boolean isCancelled) {
        this.consumer = consumer;
        this.verify = verify;
        this.isDeleted = isDeleted;
        this.isCancelled = isCancelled;
        Bukkit.getPluginManager().registerEvents(this, ItemCreator.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (alreadyDeleted) return;
        if (isCancelled) event.setCancelled(true);
        if (verify.test(event)) {
            Bukkit.getScheduler().runTask(ItemCreator.getInstance(), () -> consumer.accept(event));
            if (isDeleted)
                alreadyDeleted = true;
        }
    }
}