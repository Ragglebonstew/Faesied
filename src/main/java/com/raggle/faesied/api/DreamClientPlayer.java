package com.raggle.faesied.api;

public interface DreamClientPlayer extends DreamPlayer{

	public void setListSize(int i);
	public int getListSize();
	public void incrementList();
	public void decrementList();

}