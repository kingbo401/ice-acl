package com.kingbo401.iceacl.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.po.DataPropertyPO;
import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.PermissionGroupPO;

public class BizUtils {
	public static PermissionDTO buildPermissionDTO(PermissionPO userRoleRefPO){
		if(userRoleRefPO == null){
			return null;
		}
		PermissionDTO permissionDTO = new PermissionDTO();
		BeanUtils.copyProperties(userRoleRefPO, permissionDTO);
		return permissionDTO;
	}
	
	public static List<PermissionDTO> buildPermissionDTOs(List<PermissionPO> userRoleRefPOs){
		if(CollectionUtils.isEmpty(userRoleRefPOs)){
			return null;
		}
		List<PermissionDTO> permissionDTOs = new ArrayList<PermissionDTO>();
		for(PermissionPO userRoleRefPO : userRoleRefPOs){
			PermissionDTO permissionDTO = new PermissionDTO();
			BeanUtils.copyProperties(userRoleRefPO, permissionDTO);
			permissionDTOs.add(permissionDTO);
		}
		return permissionDTOs;
	}
	
	public static PermissionGroupDTO buildPermissionGroupDTO(PermissionGroupPO permissionGroupPO){
		if(permissionGroupPO == null){
			return null;
		}
		PermissionGroupDTO permissionGroupDTO = new PermissionGroupDTO();
		BeanUtils.copyProperties(permissionGroupPO, permissionGroupDTO);
		return permissionGroupDTO;
	}
	
	public static List<PermissionGroupDTO> buildPermissionGroupDTOs(List<PermissionGroupPO> permissionGroupPOs){
		if(CollectionUtils.isEmpty(permissionGroupPOs)){
			return null;
		}
		List<PermissionGroupDTO> permissionGroupDTOs = new ArrayList<PermissionGroupDTO>();
		for(PermissionGroupPO permissionGroupPO : permissionGroupPOs){
			permissionGroupDTOs.add(buildPermissionGroupDTO(permissionGroupPO));
		}
		return permissionGroupDTOs;
	}
	
	public static DataPropertyDTO buildDataPropertyDTO(DataPropertyPO dataPropertyPO){
		if(dataPropertyPO == null){
			return null;
		}
		DataPropertyDTO dataPropertyDTO = new DataPropertyDTO();
		BeanUtils.copyProperties(dataPropertyPO, dataPropertyDTO);
		return dataPropertyDTO;
	}

	public static List<DataPropertyDTO> buildDataPropertyDTOs(List<DataPropertyPO> dataPropertyPOs) {
		List<DataPropertyDTO> lists = new ArrayList<DataPropertyDTO>();
		for (DataPropertyPO propertyPO : dataPropertyPOs) {
			lists.add(buildDataPropertyDTO(propertyPO));
		}
		return lists;
	}
}
