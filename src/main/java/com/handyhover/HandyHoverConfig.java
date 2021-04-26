package com.handyhover;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("handyhover")
public interface HandyHoverConfig extends Config
{
	@ConfigItem(
		keyName = "enableBank",
		name = "Enable overlay for bank",
		description = "Displays overlay in bank",
		position = 1
	)
	default boolean enableBank()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableInventory",
		name = "Enable overlay for inventory",
		description = "The message to show to the user when they login",
		position = 2
	)
	default boolean enableInventory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "includeOrigin",
		name = "Display origin quest",
		description = "Display the quest where the item originated",
		position = 3
	)
	default boolean includeOrigin()
	{
		return true;
	}

}
