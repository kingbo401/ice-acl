package com.kingbo401.acl.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.kingbo401.acl.model.dto.DataPropertyDTO;
import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.entity.DataPropertyDO;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.PermissionGroupDO;

public class BizUtils {
	public static PermissionDTO buildPermissionDTO(PermissionDO userRoleRefDO){
		if(userRoleRefDO == null){
			return null;
		}
		PermissionDTO permissionDTO = new PermissionDTO();
		BeanUtils.copyProperties(userRoleRefDO, permissionDTO);
		return permissionDTO;
	}
	
	public static List<PermissionDTO> buildPermissionDTOs(List<PermissionDO> userRoleRefPOs){
		if(CollectionUtils.isEmpty(userRoleRefPOs)){
			return null;
		}
		List<PermissionDTO> permissionDTOs = new ArrayList<PermissionDTO>();
		for(PermissionDO userRoleRefDO : userRoleRefPOs){
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
	
	public static List<PermissionGroupDTO> buildPermissionGroupDTOs(List<PermissionGroupDO> permissionGroupPOs){
		if(CollectionUtils.isEmpty(permissionGroupPOs)){
			return null;
		}
		List<PermissionGroupDTO> permissionGroupDTOs = new ArrayList<PermissionGroupDTO>();
		for(PermissionGroupDO permissionGroupDO : permissionGroupPOs){
			permissionGroupDTOs.add(buildPermissionGroupDTO(permissionGroupDO));
		}
		return permissionGroupDTOs;
	}
	
	public static DataPropertyDTO buildDataPropertyDTO(DataPropertyDO dataPropertyDO){
		if(dataPropertyDO == null){
			return null;
		}
		DataPropertyDTO dataPropertyDTO = new DataPropertyDTO();
		BeanUtils.copyProperties(dataPropertyDO, dataPropertyDTO);
		return dataPropertyDTO;
	}

	public static List<DataPropertyDTO> buildDataPropertyDTOs(List<DataPropertyDO> dataPropertyPOs) {
		List<DataPropertyDTO> lists = new ArrayList<DataPropertyDTO>();
		for (DataPropertyDO propertyDO : dataPropertyPOs) {
			lists.add(buildDataPropertyDTO(propertyDO));
		}
		return lists;
	}
}
