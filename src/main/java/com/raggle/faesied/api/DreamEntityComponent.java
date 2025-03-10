package com.raggle.faesied.api;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public interface DreamEntityComponent extends Component {

	public boolean isDream();
	public void setDream(boolean b);
	
	public byte getDream();
	public void setDream(byte b);
	
	public NbtCompound getPersistantData();
	
}