package com.raggle.faesied.api;

import java.util.ArrayList;

import com.raggle.faesied.common.entity.FaeSkeleton;

public interface DreamServerPlayer extends DreamPlayer{

	void syncDream();
	
	void addToList(FaeSkeleton skeleton);
	void removeFromList(FaeSkeleton skeleton);
	
	ArrayList<FaeSkeleton> getList();
	
}