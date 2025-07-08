package com.raggle.api;

import java.util.ArrayList;

import com.raggle.entity.FaeSkeleton;

public interface DreamServerPlayer {
	
	void addToList(FaeSkeleton skeleton);
	void removeFromList(FaeSkeleton skeleton);
	
	ArrayList<FaeSkeleton> getList();
	
}