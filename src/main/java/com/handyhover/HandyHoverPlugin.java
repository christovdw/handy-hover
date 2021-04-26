package com.handyhover;

import com.google.gson.Gson;
import com.google.inject.Provides;
import com.handyhover.model.QuestItems;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemComposition;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Handy Hover",
	description = "Displays an overlay of related quests for an item",
	tags = {"quest", "items", "overlay", "hover", "handy"}
)
public class HandyHoverPlugin extends Plugin
{
	@Inject
	ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HandyHoverOverlay handyHoverOverlay;

	public QuestItems[] questItems;

	@Override
	protected void startUp() throws Exception
	{
		InputStream questItemsFile = HandyHoverPlugin.class.getResourceAsStream("/quest_items.json");
		questItems = new Gson().fromJson(new InputStreamReader(questItemsFile), QuestItems[].class);

		overlayManager.add(handyHoverOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(handyHoverOverlay);
	}

	private String getItemName(int itemId)
	{
		// 552
		ItemComposition itemComp = itemManager.getItemComposition(itemId);
		String itemName = itemComp.getName();

		return itemName; // Ghostspeak Amulet
	}

	@Provides
	HandyHoverConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HandyHoverConfig.class);
	}
}
