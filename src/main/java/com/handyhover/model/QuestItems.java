package com.handyhover.model;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;

public class QuestItems
{
	@Getter
	@Setter
	private Integer itemId;

	@Getter
	@Setter
	private String itemName;

	@Getter
	@Setter
	private String foundIn;

	@Getter
	@Setter
	private String[] usedIn;

	@Getter
	@Setter
	private Boolean neededForQuest;

	@Getter
	@Setter
	private QuestState questState;

	@Getter
	@Setter
	private Quest widget = null;
}
