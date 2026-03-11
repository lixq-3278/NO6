package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


@TableName("tongzhijilu")
public class TongzhijiluEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public TongzhijiluEntity() {
		
	}
	
	public TongzhijiluEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private Long tongzhiid;
	
	private String tongzhibianhao;
	
	private String zhanghao;
	
	private String shouji;
	
	private String tongzhineirong;
	
	private String zhuangtai;
	
	private Integer chongshicishu;
	
	private Date zuijinfasongshijian;
	
	private String cuowuxinxi;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTongzhiid() {
		return tongzhiid;
	}
	public void setTongzhiid(Long tongzhiid) {
		this.tongzhiid = tongzhiid;
	}
	
	public String getTongzhibianhao() {
		return tongzhibianhao;
	}
	public void setTongzhibianhao(String tongzhibianhao) {
		this.tongzhibianhao = tongzhibianhao;
	}
	
	public String getZhanghao() {
		return zhanghao;
	}
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	
	public String getShouji() {
		return shouji;
	}
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	
	public String getTongzhineirong() {
		return tongzhineirong;
	}
	public void setTongzhineirong(String tongzhineirong) {
		this.tongzhineirong = tongzhineirong;
	}
	
	public String getZhuangtai() {
		return zhuangtai;
	}
	public void setZhuangtai(String zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	
	public Integer getChongshicishu() {
		return chongshicishu;
	}
	public void setChongshicishu(Integer chongshicishu) {
		this.chongshicishu = chongshicishu;
	}
	
	public Date getZuijinfasongshijian() {
		return zuijinfasongshijian;
	}
	public void setZuijinfasongshijian(Date zuijinfasongshijian) {
		this.zuijinfasongshijian = zuijinfasongshijian;
	}
	
	public String getCuowuxinxi() {
		return cuowuxinxi;
	}
	public void setCuowuxinxi(String cuowuxinxi) {
		this.cuowuxinxi = cuowuxinxi;
	}

}
