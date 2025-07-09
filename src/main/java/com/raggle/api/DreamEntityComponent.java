package com.raggle.api;

import com.raggle.util.DreamState;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface DreamEntityComponent extends Component {
	
	public DreamState getDream();
	public void setDream(DreamState dreamState);
	public boolean shouldUpdateClient();
	
}