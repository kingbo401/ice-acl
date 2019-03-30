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
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.po.PermissionGroupPO;
import com.kingbo401.iceacl.model.po.PermissionGroupLite;
import com.kingbo401.iceacl.model.po.param.PermissionGroupQueryParam;
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

		PermissionGroupPO permissionGroupPO = new PermissionGroupPO();
		BeanUtils.copyProperties(permissionGroupDTO, permissionGroupPO);
		permissionGroupDAO.create(permissionGroupPO);
		permissionGroupDTO.setId(permissionGroupPO.getId());
		return permissionGroupDTO;
	}

	@Override
	public boolean updatePermissionGroup(PermissionGroupDTO permissionGroupDTO) {
		Assert.notNull(permissionGroupDTO, "参数不能为空");
		Assert.notNull(permissionGroupDTO.getId(), "id不能为空");
		Assert.hasText(permissionGroupDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(permissionGroupDTO.getGroupName(), "groupName不能为空");
		Assert.hasText(permissionGroupDTO.getTenant(), "tenant不能为空");
		PermissionGroupPO permissionGroupPO = permissionGroupDAO.getById(permissionGroupDTO.getId());
		Assert.notNull(permissionGroupPO, "权限组不存在");
		BeanUtils.copyProperties(permissionGroupDTO, permissionGroupPO);
		permissionGroupPO.setUpdateTime(new Date());
		permissionGroupDAO.update(permissionGroupPO);
		return true;
	}

	private boolean updatePermissionGroupStatus(String appKey, Long id, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		PermissionGroupPO permissionGroupPO = permissionGroupDAO.getById(id);
		Assert.notNull(permissionGroupPO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupPO.getAppKey()), "appkey权限组不匹配");
		permissionGroupPO.setStatus(status);
		permissionGroupDAO.update(permissionGroupPO);
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
		List<PermissionGroupPO> permissionGroupPOs = permissionGroupDAO.listPermissionGroup(permissionGroupQueryParam);
		if (CollectionUtils.isEmpty(permissionGroupPOs)) {
			return null;
		}
		List<PermissionGroupPO> rootPermissionGroups = new ArrayList<PermissionGroupPO>();// 根权限组
		Map<Long, List<PermissionGroupPO>> permissionGroupMap = Maps.newHashMap();
		for (PermissionGroupPO permissionGroup : permissionGroupPOs) {
			Long groupPid = permissionGroup.getGroupPid();
			if (groupPid == 0) {
				rootPermissionGroups.add(permissionGroup);
			}
			List<PermissionGroupPO> list = permissionGroupMap.get(groupPid);
			if (list == null) {
				list = Lists.newArrayList();
				permissionGroupMap.put(groupPid, list);
			}
			list.add(permissionGroup);
		}
		
		List<PermissionGroupTreeNode> treeNodes = new ArrayList<PermissionGroupTreeNode>();
		if (!CollectionUtils.isEmpty(rootPermissionGroups)) {
			for (PermissionGroupPO rootPermissionGroup : rootPermissionGroups) {
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
			Map<Long, List<PermissionGroupPO>> permissionGroupMap, int level) {
		List<PermissionGroupPO> permissionGroupChildren = permissionGroupMap.get(parentTreePermissionGroup.getId());
		if (!CollectionUtils.isEmpty(permissionGroupChildren)) {
			for (PermissionGroupPO permissionGroupPO : permissionGroupChildren) {
				PermissionGroupTreeNode permissionGroupTreeNode = new PermissionGroupTreeNode();
				permissionGroupTreeNode.setLevel(level);
				BeanUtils.copyProperties(permissionGroupPO, permissionGroupTreeNode);
				parentTreePermissionGroup.getChildren().add(permissionGroupTreeNode);
				this.recursivePermissionGroup(permissionGroupTreeNode, permissionGroupMap, level++);
			}
		}
	}

	@Override
	public List<PermissionGroupDTO> listPermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam) {
		Assert.notNull(permissionGroupQueryParam, "permissionGroupQueryParam不能为空");
		Assert.hasText(permissionGroupQueryParam.getAppKey(), "appKey 不能为空");
		List<PermissionGroupPO> permissionGroupPOs = permissionGroupDAO.listPermissionGroup(permissionGroupQueryParam);
		return BizUtils.buildPermissionGroupDTOs(permissionGroupPOs);
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
		List<PermissionGroupPO> permissionGroupPOs = permissionGroupDAO.pagePermissionGroup(permissionGroupQueryParam);
		pageDTO.setItems(BizUtils.buildPermissionGroupDTOs(permissionGroupPOs));
		return pageDTO;
	}

	@Override
	public List<PermissionGroupDTO> getPermissionGroupByIds(String appKey, List<Long> groupIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(groupIds, "groupIds不能为空");
		List<PermissionGroupPO> permissionGroupPOs = permissionGroupDAO.getByIds(groupIds);
		if(CollectionUtils.isEmpty(permissionGroupPOs)){
			return null;
		}
		for(PermissionGroupPO permissionGroupPO : permissionGroupPOs){
			Assert.isTrue(appKey.equals(permissionGroupPO.getAppKey()), "appKey权限组不匹配");
		}
		return BizUtils.buildPermissionGroupDTOs(permissionGroupPOs);
	}

	@Override
	public List<PermissionGroupDTO> getPermissionGroupByIds(List<Long> groupIds) {
		Assert.notEmpty(groupIds, "groupIds不能为空");
		List<PermissionGroupPO> permissionGroupPOs = permissionGroupDAO.getByIds(groupIds);
		if(CollectionUtils.isEmpty(permissionGroupPOs)){
			return null;
		}
		return BizUtils.buildPermissionGroupDTOs(permissionGroupPOs);
	}

	@Override
	public PermissionGroupDTO getPermissionGroupById(long id) {
		PermissionGroupPO permissionGroupPO = permissionGroupDAO.getById(id);
		return BizUtils.buildPermissionGroupDTO(permissionGroupPO);
	}

	@Override
	public List<PermissionGroupLite> listPermissionGroupLite(String appKey, String tenant) {
		Assert.hasText(appKey, "appKey 不能为空");
		Assert.hasText(tenant, "tenant 不能为空");
		return permissionGroupDAO.listPermissionGroupLite(appKey, tenant);
	}
}
