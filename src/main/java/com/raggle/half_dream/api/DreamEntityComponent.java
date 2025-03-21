package com.raggle.half_dream.api;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface DreamEntityComponent extends Component {
	
	public byte getDream();
	public void setDream(byte b);
	
}