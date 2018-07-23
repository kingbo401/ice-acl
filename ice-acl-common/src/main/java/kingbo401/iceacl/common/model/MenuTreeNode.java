package kingbo401.iceacl.common.model;

import java.util.ArrayList;
import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class MenuTreeNode extends BasePojo{
	private Long id;
	private String menuName;
	private Long menuPid;
	private String menuUrl;
	private String menuIcon;
	private Integer menuType;
	private Integer menuOrder;
	private Integer level;
	private List<MenuTreeNode> children = new ArrayList<MenuTreeNode>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Long getMenuPid() {
		return menuPid;
	}
	public void setMenuPid(Long menuPid) {
		this.menuPid = menuPid;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	
	public Integer getMenuType() {
		return menuType;
	}
	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<MenuTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeNode> children) {
		this.children = children;
	}
}
