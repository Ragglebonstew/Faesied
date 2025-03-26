package com.raggle.half_dream.api;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface DreamPlayerComponent extends Component {
	
	public boolean isInterloped();
	public void setInterlope(boolean b);
	
}