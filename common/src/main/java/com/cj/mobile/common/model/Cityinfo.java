package com.cj.mobile.common.model;

import java.io.Serializable;

/**
 * 城市信息
 * @author 王力杨
 *
 */
@SuppressWarnings("serial")
public class Cityinfo implements Serializable {
	// 自增
	private int _id;
	private String regionid;			//id
	private String regionname;			//名称
	private String parentid;			//父级id
	private String grade;				//等级：国家 = 0，省份 = 1，城市 = 2，……

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getRegionid() {
		return regionid;
	}

	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Cityinfo{" +
				"_id=" + _id +
				", regionid='" + regionid + '\'' +
				", regionname='" + regionname + '\'' +
				", parentid='" + parentid + '\'' +
				", grade='" + grade + '\'' +
				'}';
	}
}
