package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.dao.PermissionGroupDAO;
import com.kingbo401.iceacl.manager.PermissionGroupManager;
import com.kingbo401.iceacl.manager.UserPermissionGroupRefManager;
import com.kingbo401.iceacl.model.db.PermissionGroupDO;
import com.kingbo401.iceacl.model.db.PermissionGroupLite;
import com.kingbo401.iceacl.model.db.param.PermissionGroupQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.model.PermissionGroupTreeNode;

@Service
public class PermissionGroupManagerImpl implements PermissionGroupManager{
	@Autowired
	private PermissionGroupDAO permissionGroupDAO;
	@Autowired
	private UserPermissionGroupRefManager userPermissionGroupRefManager;
	
	@Override
	public PermissionGroupDTO createPermissionGroup(PermissionGroupDTO permissionGroupDTO) {
		Assert.notNull(permissionGroupDTO, "参数不能为空");
		Assert.isNull(permissionGroupDTO.getId(), "id必须为空");
		Assert.hasText(permissionGroupDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(permissionGroupDTO.getGroupName(), "groupName不能为空");
		Assert.hasText(permissionGroupDTO.getTenant(), "tenant不能为空");
		Date now = new Date();
		permissionGroupDTO.setCreateTime(now);
		permissionGroupDTO.setUpdateTime(now);
		permissionGroupDTO.setStatus(IceAclConstant.STATUS_NORMAL);

		PermissionGroupDO permissionGroupDO = new PermissionGroupDO();
		BeanUtils.copyProperties(permissionGroupDTO, permissionGroupDO);
		permissionGroupDAO.create(permissionGroupDO);
		permissionGroupDTO.setId(permissionGroupDO.getId());
		return permissionGroupDTO;
	}

	@Override
	public boolean updatePermissionGroup(PermissionGroupDTO permissionGroupDTO) {
		Assert.notNull(permissionGroupDTO, "参数不能为空");
		Assert.notNull(permissionGroupDTO.getId(), "id不能为空");
		Assert.hasText(permissionGroupDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(permissionGroupDTO.getGroupName(), "groupName不能为空");
		Assert.hasText(permissionGroupDTO.getTenant(), "tenant不能为空");
		PermissionGroupDO permissionGroupDO = permissionGroupDAO.getById(permissionGroupDTO.getId());
		Assert.notNull(permissionGroupDO, "权限组不存在");
		BeanUtils.copyProperties(permissionGroupDTO, permissionGroupDO);
		permissionGroupDO.setUpdateTime(new Date());
		permissionGroupDAO.update(permissionGroupDO);
		return true;
	}

	private boolean updatePermissionGroupStatus(String appKey, Long id, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		PermissionGroupDO permissionGroupDO = permissionGroupDAO.getById(id);
		Assert.notNull(permissionGroupDO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupDO.getAppKey()), "appkey权限组不匹配");
		permissionGroupDO.setStatus(status);
		permissionGroupDAO.update(permissionGroupDO);
		return true;
	}
	
	@Override
	public boolean removePermissionGroup(String appKey, long id) {
		Assert.isTrue(!userPermissionGroupRefManager.hasUserUse(id), "权限组有用户使用");
		return updatePermissionGroupStatus(appKey, id, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezePermissionGroup(String appKey, long id) {
		return updatePermissionGroupStatus(appKey, id, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezePermissionGroup(String appKey, long id) {
		return updatePermissionGroupStatus(appKey, id, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public List<PermissionGroupTreeNode> getPermissionGroupTree(PermissionGroupQueryParam permissionGroupQueryParam) {
		Assert.notNull(permissionGroupQueryParam, "permissionGroupQueryParam不能为空");
		String appKey = permissionGroupQueryParam.getAppKey();
		Assert.hasText(appKey, "appKey 不能为空");
		List<PermissionGroupDO> permissionGroupDOs = permissionGroupDAO.listPermissionGroup(permissionGroupQueryParam);
		if (CollectionUtils.isEmpty(permissionGroupDOs)) {
			return null;
		}
		List<PermissionGroupDO> rootPermissionGroups = new ArrayList<PermissionGroupDO>();// 根权限组
		Map<Long, List<PermissionGroupDO>> permissionGroupMap = Maps.newHashMap();
		for (PermissionGroupDO permissionGroup : permissionGroupDOs) {
			Long groupPid = permissionGroup.getGroupPid();
			if (groupPid == 0) {
				rootPermissionGroups.add(permissionGroup);
			}
			List<PermissionGroupDO> list = permissionGroupMap.get(groupPid);
			if (list == null) {
				list = Lists.newArrayList();
				permissionGroupMap.put(groupPid, list);
			}
			list.add(permissionGroup);
		}
		
		List<PermissionGroupTreeNode> treeNodes = new ArrayList<PermissionGroupTreeNode>();
		if (!CollectionUtils.isEmpty(rootPermissionGroups)) {
			for (PermissionGroupDO rootPermissionGroup : rootPermissionGroups) {
				PermissionGroupTreeNode permissionGroupTreeNode = new PermissionGroupTreeNode();
				BeanUtils.copyProperties(rootPermissionGroup, permissionGroupTreeNode);
				permissionGroupTreeNode.setLevel(1);
				this.recursivePermissionGroup(permissionGroupTreeNode, permissionGroupMap, 2);
				treeNodes.add(permissionGroupTreeNode);
			}
		}
		return treeNodes;
	}
	
	private void recursivePermissionGroup(PermissionGroupTreeNode parentTreePermissionGroup,
			Map<Long, List<PermissionGroupDO>> permissionGroupMap, int level) {
		List<PermissionGroupDO> permissionGroupChildren = permissionGroupMap.get(parentTreePermissionGroup.getId());
		if (!CollectionUtils.isEmpty(permissionGroupChildren)) {
			for (PermissionGroupDO permissionGroupDO : permissionGroupChildren) {
				PermissionGroupTreeNode permissionGroupTreeNode = new PermissionGroupTreeNode();
				permissionGroupTreeNode.setLevel(level);
				BeanUtils.copyProperties(permissionGroupDO, permissionGroupTreeNode);
				parentTreePermissionGroup.getChildren().add(permissionGroupTreeNode);
				this.recursivePermissionGroup(permissionGroupTreeNode, permissionGroupMap, level++);
			}
		}
	}

	@Override
	public List<PermissionGroupDTO> listPermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam) {
		Assert.notNull(permissionGroupQueryParam, "permissionGroupQueryParam不能为空");
		Assert.hasText(permissionGroupQueryParam.getAppKey(), "appKey 不能为空");
		List<PermissionGroupDO> permissionGroupDOs = permissionGroupDAO.listPermissionGroup(permissionGroupQueryParam);
		return BizUtils.buildPermissionGroupDTOs(permissionGroupDOs);
	}

	@Override
	public PageVO<PermissionGroupDTO> pagePermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam) {
		Assert.notNull(permissionGroupQueryParam, "permissionGroupQueryParam不能为空");
		Assert.hasText(permissionGroupQueryParam.getAppKey(), "appKey 不能为空");
		PageVO<PermissionGroupDTO> pageDTO = new PageVO<PermissionGroupDTO>(permissionGroupQueryParam);
		pageDTO.setPageNum(permissionGroupQueryParam.getPageNum());
		pageDTO.setPageSize(permissionGroupQueryParam.getPageSize());
		if(permissionGroupQueryParam.isReturnTotalCount()){
			long total = permissionGroupDAO.countPermissionGroup(permissionGroupQueryParam);
			pageDTO.setTotal(total);
			if(total == 0){
				return pageDTO;
			}
		}
		List<PermissionGroupDO> permissionGroupDOs = permissionGroupDAO.pagePermissionGroup(permissionGroupQueryParam);
		pageDTO.setItems(BizUtils.buildPermissionGroupDTOs(permissionGroupDOs));
		return pageDTO;
	}

	@Override
	public List<PermissionGroupDTO> getPermissionGroupByIds(String appKey, List<Long> groupIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(groupIds, "groupIds不能为空");
		List<PermissionGroupDO> permissionGroupDOs = permissionGroupDAO.getByIds(groupIds);
		if(CollectionUtils.isEmpty(permissionGroupDOs)){
			return null;
		}
		for(PermissionGroupDO permissionGroupDO : permissionGroupDOs){
			Assert.isTrue(appKey.equals(permissionGroupDO.getAppKey()), "appKey权限组不匹配");
		}
		return BizUtils.buildPermissionGroupDTOs(permissionGroupDOs);
	}

	@Override
	public List<PermissionGroupDTO> getPermissionGroupByIds(List<Long> groupIds) {
		Assert.notEmpty(groupIds, "groupIds不能为空");
		List<PermissionGroupDO> permissionGroupDOs = permissionGroupDAO.getByIds(groupIds);
		if(CollectionUtils.isEmpty(permissionGroupDOs)){
			return null;
		}
		return BizUtils.buildPermissionGroupDTOs(permissionGroupDOs);
	}

	@Override
	public PermissionGroupDTO getPermissionGroupById(long id) {
		PermissionGroupDO permissionGroupDO = permissionGroupDAO.getById(id);
		return BizUtils.buildPermissionGroupDTO(permissionGroupDO);
	}

	@Override
	public List<PermissionGroupLite> listPermissionGroupLite(String appKey, String tenant) {
		Assert.hasText(appKey, "appKey 不能为空");
		Assert.hasText(tenant, "tenant 不能为空");
		return permissionGroupDAO.listPermissionGroupLite(appKey, tenant);
	}
}
