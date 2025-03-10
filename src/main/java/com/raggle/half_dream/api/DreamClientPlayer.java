package com.raggle.half_dream.api;

public interface DreamClientPlayer extends DreamEntityComponent{

	public void setListSize(int i);
	public int getListSize();
	public void incrementList();
	public void decrementList();

}