package com.handyhover;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.handyhover.model.QuestItems;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Slf4j
class HandyHoverOverlay extends Overlay
{
	private static final int INVENTORY_ITEM_WIDGETID = WidgetInfo.INVENTORY.getPackedId();
	private static final int BANK_INVENTORY_ITEM_WIDGETID = WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId();
	private static final int BANK_ITEM_WIDGETID = WidgetInfo.BANK_ITEM_CONTAINER.getPackedId();
	private static final int EXPLORERS_RING_ITEM_WIDGETID = WidgetInfo.EXPLORERS_RING_ALCH_INVENTORY.getPackedId();
	private static final int SEED_VAULT_ITEM_WIDGETID = WidgetInfo.SEED_VAULT_ITEM_CONTAINER.getPackedId();
	private static final int SEED_VAULT_INVENTORY_ITEM_WIDGETID = WidgetInfo.SEED_VAULT_INVENTORY_ITEMS_CONTAINER.getPackedId();

	private final Client client;
	private final HandyHoverConfig config;
	private final TooltipManager tooltipManager;

	@Inject
	HandyHoverOverlay(Client client, HandyHoverConfig config, TooltipManager tooltipManager)
	{
		setPosition(OverlayPosition.DYNAMIC);
		this.client = client;
		this.config = config;
		this.tooltipManager = tooltipManager;
	}

	@Inject
	ItemManager itemManager;

	@Inject
	QuestColors questColors;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.isMenuOpen())
		{
			return null;
		}

		final MenuEntry[] menuEntries = client.getMenuEntries();
		final int last = menuEntries.length - 1;

		if (last < 0)
		{
			return null;
		}

		final MenuEntry menuEntry = menuEntries[last];
		final MenuAction action = MenuAction.of(menuEntry.getType());
		final int widgetId = menuEntry.getParam1();
		final int groupId = WidgetInfo.TO_GROUP(widgetId);

		switch (action)
		{
			case ITEM_USE_ON_WIDGET:
			case CC_OP:
			case ITEM_USE:
			case ITEM_FIRST_OPTION:
			case ITEM_SECOND_OPTION:
			case ITEM_THIRD_OPTION:
			case ITEM_FOURTH_OPTION:
			case ITEM_FIFTH_OPTION:
				switch (groupId)
				{
					case WidgetID.INVENTORY_GROUP_ID:
					case WidgetID.BANK_GROUP_ID:
					case WidgetID.BANK_INVENTORY_GROUP_ID:
					case WidgetID.SEED_VAULT_GROUP_ID:
					case WidgetID.SEED_VAULT_INVENTORY_GROUP_ID:
						// Make tooltip
						final String text = makeQuestTooltip(menuEntry);

						if (text != null)
						{
							tooltipManager.add(new Tooltip(text));
						}
						break;
				}
				break;
		}
		return null;
	}

	// Displays the overlay depending on the menu that is hovered over
	private String makeQuestTooltip(MenuEntry menuEntry)
	{
		if (config.enableInventory() == false)
		{
			return null;
		}

		// Check if it is enabled

		final int widgetId = menuEntry.getParam1();
		ItemContainer container = null;

		// Inventory item
		if (widgetId == INVENTORY_ITEM_WIDGETID || widgetId == BANK_INVENTORY_ITEM_WIDGETID ||
			widgetId == EXPLORERS_RING_ITEM_WIDGETID || widgetId == SEED_VAULT_INVENTORY_ITEM_WIDGETID)
		{
			container = client.getItemContainer(InventoryID.INVENTORY);
		}
		else if (widgetId == BANK_ITEM_WIDGETID)
		{
			container = client.getItemContainer(InventoryID.BANK);
		}
		else if (widgetId == SEED_VAULT_ITEM_WIDGETID)
		{
			container = client.getItemContainer(InventoryID.SEED_VAULT);
		}

		if (container == null)
		{
			return null;
		}


		final int index = menuEntry.getParam0();
		final Item item = container.getItem(index);

		if (item != null)
		{
			return questTooltipText(item);
		}

		return null;
	}

	// Build the tooltip
	private String buildQuestRow(Integer itemId, Client c)
	{
		/*
		InputStream questFile = DintPlugin.class.getResourceAsStream("/quests.json");

		QuestInfo[] questInfo = new Gson().fromJson(new InputStreamReader(questFile), QuestInfo[].class);
		StringBuilder sb = new StringBuilder();

		// For each quest
		for (Quest quest : Quest.values())
		{
			// From quests.json
			for (QuestInfo info : questInfo)
			{
				// If ItemID matches from the .json file
				if (itemId.intValue() == info.getItemId().intValue())
				{
					// For each quest in the UsedIn segment of the .json file
					for (String usedIn : info.getUsedIn())
					{
						Quest questName = Quest.valueOf(usedIn);

						// If the quest name matches the usedIn
						if (quest.getName().equalsIgnoreCase(questName.getName()))
						{
							info.setQuestState(quest.getState(c));

							if (info.getQuestState() == QuestState.FINISHED)
							{
								sb.append(ColorUtil.prependColorTag(quest.getName(), getFinishedColor()) + CRLF);
							}
							else if (info.getQuestState() == QuestState.IN_PROGRESS)
							{
								sb.append(ColorUtil.prependColorTag(quest.getName(), getInProgressColor()) + CRLF);
							}
							else if (info.getQuestState() == QuestState.NOT_STARTED)
							{
								sb.append(ColorUtil.prependColorTag(quest.getName(), getNotStartedColor()) + CRLF);
							}

						}
					}
				}
			}
		}
		return sb.toString();
		*/

		return "";
	}

	private String questTooltipText(Item item)
	{
		// Get the ItemID
		int id = itemManager.canonicalize(item.getId());

		// Build tooltip text
		String questList = buildQuestRow(id, client);

		return questList;
	}

}
