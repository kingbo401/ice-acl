package kingbo401.iceacl.common.utils;

import java.util.Date;

public class MixAll {
	public static void checkEffectiveExpireTime(Date effectiveTime, Date expireTime){
		if(effectiveTime == null && expireTime == null){
			return;
		}
		
		Date now = new Date();
		if(effectiveTime == null && expireTime != null){
			if(expireTime.before(now) || expireTime.equals(now)){
				throw new IllegalArgumentException("expireTime不能早于当前时间");
			}
		}
		
		if(effectiveTime != null && expireTime == null){
			return;
		}
		
		if(effectiveTime != null && expireTime != null){
			if(expireTime.before(effectiveTime) || expireTime.equals(effectiveTime)){
				throw new IllegalArgumentException("expireTime不能早于effectiveTime");
			}
		}
	}
}
