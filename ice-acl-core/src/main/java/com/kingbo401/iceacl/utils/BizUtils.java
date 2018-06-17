package com.kingbo401.iceacl.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.kingbo401.iceacl.model.db.DataPropertyDO;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.PermissionGroupDO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;

public class BizUtils {
	public static PermissionDTO buildPermissionDTO(PermissionDO permissionDO){
		if(permissionDO == null){
			return null;
		}
		PermissionDTO permissionDTO = new PermissionDTO();
		BeanUtils.copyProperties(permissionDO, permissionDTO);
		return permissionDTO;
	}
	
	public static List<PermissionDTO> buildPermissionDTOs(List<PermissionDO> permissionDOs){
		if(CollectionUtils.isEmpty(permissionDOs)){
			return null;
		}
		List<PermissionDTO> permissionDTOs = new ArrayList<PermissionDTO>();
		for(PermissionDO permissionDO : permissionDOs){
			PermissionDTO permissionDTO = new PermissionDTO();
			BeanUtils.copyProperties(permissionDO, permissionDTO);
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
	
	public static DataPropertyDTO buildDataPropertyDTO(DataPropertyDO dataPropertyDO){
		if(dataPropertyDO == null){
			return null;
		}
		DataPropertyDTO dataPropertyDTO = new DataPropertyDTO();
		BeanUtils.copyProperties(dataPropertyDO, dataPropertyDTO);
		return dataPropertyDTO;
	}

	public static List<DataPropertyDTO> buildDataPropertyDTOs(List<DataPropertyDO> dataPropertyDOs) {
		List<DataPropertyDTO> lists = new ArrayList<DataPropertyDTO>();
		for (DataPropertyDO propertyDO : dataPropertyDOs) {
			lists.add(buildDataPropertyDTO(propertyDO));
		}
		return lists;
	}
}
