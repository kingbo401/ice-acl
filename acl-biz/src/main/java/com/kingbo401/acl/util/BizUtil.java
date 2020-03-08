package com.kingbo401.acl.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.kingbo401.acl.common.model.dto.PermissionDTO;
import com.kingbo401.acl.common.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.PermissionGroupDO;

public class BizUtil {
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
	public static PermissionDTO buildPermissionDTO(PermissionDO userRoleRefDO){
		if(userRoleRefDO == null){
			return null;
		}
		PermissionDTO permissionDTO = new PermissionDTO();
		BeanUtils.copyProperties(userRoleRefDO, permissionDTO);
		return permissionDTO;
	}
	
	public static List<PermissionDTO> buildPermissionDTOs(List<PermissionDO> userRoleRefDOs){
		if(CollectionUtils.isEmpty(userRoleRefDOs)){
			return null;
		}
		List<PermissionDTO> permissionDTOs = new ArrayList<PermissionDTO>();
		for(PermissionDO userRoleRefDO : userRoleRefDOs){
			PermissionDTO permissionDTO = new PermissionDTO();
			BeanUtils.copyProperties(userRoleRefDO, permissionDTO);
			permissionDTOs.add(permissionDTO);
		}
		return permissionDTOs;
	}
	
	public static PermissionGroupDTO buildPermissionGroupDTO(PermissionGroupDO permissionGroupDO){
		if(permissionGroupDO == null){
			return null;
		}
		PermissionGroupDTO permissionGroupDTO = new PermissionGroupDTO();
		BeanUtils.copyProperties(permissionGroupDO, permissionGroupDTO);
		return permissionGroupDTO;
	}
	
	public static List<PermissionGroupDTO> buildPermissionGroupDTOs(List<PermissionGroupDO> permissionGroupDOs){
		if(CollectionUtils.isEmpty(permissionGroupDOs)){
			return null;
		}
		List<PermissionGroupDTO> permissionGroupDTOs = new ArrayList<PermissionGroupDTO>();
		for(PermissionGroupDO permissionGroupDO : permissionGroupDOs){
			permissionGroupDTOs.add(buildPermissionGroupDTO(permissionGroupDO));
		}
		return permissionGroupDTOs;
	}
}
