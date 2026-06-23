package org.yuemi.libs.plugin.gui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.SignInputBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public final class SignInputBuilderImpl implements SignInputBuilder {

    public static final Map<Player, SignSession> ACTIVE_SESSIONS = new ConcurrentHashMap<>();

    private String initialText = "";
    private String description = "";
    private int maxLength = Integer.MAX_VALUE;
    private BiConsumer<Player, String> onSubmit = (player, text) -> {};

    @Override
    public @NotNull SignInputBuilder initialText(@NotNull String text) {
        this.initialText = text;
        return this;
    }

    @Override
    public @NotNull SignInputBuilder description(@NotNull String description) {
        this.description = description;
        return this;
    }

    @Override
    public @NotNull SignInputBuilder maxLength(int limit) {
        this.maxLength = limit;
        return this;
    }

    @Override
    public @NotNull SignInputBuilder onSubmit(@NotNull BiConsumer<Player, String> callback) {
        this.onSubmit = callback;
        return this;
    }

    @Override
    public void open(@NotNull Player player) {
        // Clean up any existing session for the player first
        SignSession existing = ACTIVE_SESSIONS.remove(player);
        if (existing != null) {
            existing.restore();
        }

        Location loc = player.getLocation().getBlock().getLocation();
        BlockState originalState = loc.getBlock().getState();

        // Place a temporary Oak Sign without triggering physics updates
        loc.getBlock().setType(Material.OAK_SIGN, false);
        Sign sign = (Sign) loc.getBlock().getState();

        // Format the sign layout
        SignSide side = sign.getSide(Side.FRONT);
        side.setLine(0, "-------------");
        side.setLine(1, initialText);
        side.setLine(2, "-------------");
        side.setLine(3, description);
        sign.update(true, false);

        // Map the session
        ACTIVE_SESSIONS.put(player, new SignSession(loc, originalState, onSubmit, maxLength));

        // Open the editor
        player.openSign(sign);
    }

    public static final class SignSession {
        private final Location location;
        private final BlockState originalState;
        private final BiConsumer<Player, String> onSubmit;
        private final int maxLength;

        public SignSession(Location location, BlockState originalState, BiConsumer<Player, String> onSubmit, int maxLength) {
            this.location = location;
            this.originalState = originalState;
            this.onSubmit = onSubmit;
            this.maxLength = maxLength;
        }

        public Location getLocation() {
            return location;
        }

        public BiConsumer<Player, String> getOnSubmit() {
            return onSubmit;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public void restore() {
            // Restore the original block state without physics updates
            location.getBlock().setBlockData(originalState.getBlockData(), false);
        }
    }
}
