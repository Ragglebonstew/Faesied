package com.raggle.half_dream.api;

import java.util.ArrayList;

import com.raggle.half_dream.common.entity.FaeSkeleton;

public interface DreamServerPlayer {
	
	void addToList(FaeSkeleton skeleton);
	void removeFromList(FaeSkeleton skeleton);
	
	ArrayList<FaeSkeleton> getList();
	
}