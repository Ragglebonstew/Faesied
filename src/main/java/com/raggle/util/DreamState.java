package com.raggle.util;

public enum DreamState {
	AWAKE,
	ASLEEP,
	DUAL;
	
	public static DreamState fromByte(byte b) {
        switch(b) {
        case 0:
            return AWAKE;
        case 1:
            return ASLEEP;
        case 2:
            return DUAL;
        }
        return AWAKE;
    }
	public static byte toByte(DreamState dreamState) {
		if(dreamState == null)
			return 0;
        switch(dreamState) {
        	case AWAKE:
        		return 0;
        	case ASLEEP:
        		return 1;
        	case DUAL:
        		return 2;
        }
        return 0;
    }
	public byte asByte() {
        switch(this) {
        	case AWAKE:
        		return 0;
        	case ASLEEP:
        		return 1;
        	case DUAL:
        		return 2;
        }
        return 0;
    }
}
