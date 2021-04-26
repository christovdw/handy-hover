package com.handyhover;

import java.awt.Color;

public class QuestColors
{
	public Color getStandardColor()
	{
		return new Color(255, 255, 255);
	}

	public Color getInProgressColor()
	{
		return new Color(255, 255, 0);
	}

	public Color getFinishedColor()
	{
		return new Color(0, 255, 0);
	}

	public Color getNotStartedColor()
	{
		return new Color(255, 0, 0);
	}
}
