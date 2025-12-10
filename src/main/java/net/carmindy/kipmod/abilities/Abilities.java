// Package containing all ability-related classes and interfaces
package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Represents a generic ability in the mod.
 * Each ability has an ID, name, description, activation logic, and cooldown.
 */
public interface Abilities {
    String getId(); // Unique identifier for this ability (e.g., "flame")
    String getName(); // Human-readable name (e.g., "Flame Burst")
    String getDescription(); // Short description of what the ability does

    // Logic that executes when a player activates this ability
    void activate(ServerPlayerEntity player);

    boolean isOneTimeUse(); // True if the ability can only be used once
    int getCooldownTicks(); // Number of game ticks before the ability can be reused
}
