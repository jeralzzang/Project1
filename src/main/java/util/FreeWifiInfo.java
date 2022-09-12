package util;
import java.util.Date;

import lombok.*;

@Setter
public class FreeWifiInfo{
	private int req_id;
	private String mgr_no;
	private String wrdofc;
	private String main_nm;
	private String adres1;
	private String adres2;
	private String instl_floor;
	private String instl_ty;
	private String instl_mby;
	private String svc_se;
	private String cmcwr;
	private String cnstc_year;
	private String inout_door;
	private String remars3;
	private double lat;
	private double lnt;
	private String work_dttm;
	private double distance;
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setReq_id(int req_id) {
		this.req_id = req_id;
	}
	
	public void setMgr_no(String mgr_no) {
		this.mgr_no = mgr_no;
	}


	public void setWrdofc(String wrdofc) {
		this.wrdofc = wrdofc;
	}


	public void setMain_nm(String main_nm) {
		this.main_nm = main_nm;
	}


	public void setAdres1(String adres1) {
		this.adres1 = adres1;
	}


	public void setAdres2(String adres2) {
		this.adres2 = adres2;
	}


	public void setInstl_floor(String instl_floor) {
		this.instl_floor = instl_floor;
	}


	public void setInstl_ty(String instl_ty) {
		this.instl_ty = instl_ty;
	}


	public void setInstl_mby(String instl_mby) {
		this.instl_mby = instl_mby;
	}


	public void setSvc_se(String svc_se) {
		this.svc_se = svc_se;
	}


	public void setCmcwr(String cmcwr) {
		this.cmcwr = cmcwr;
	}


	public void setCnstc_year(String cnstc_year) {
		this.cnstc_year = cnstc_year;
	}


	public void setInout_door(String inout_door) {
		this.inout_door = inout_door;
	}


	public void setRemars3(String remars3) {
		this.remars3 = remars3;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public void setLnt(double lnt) {
		this.lnt = lnt;
	}


	public void setWork_dttm(String work_dttm) {
		this.work_dttm = work_dttm;
	}

	public int getReq_id() {
		return req_id;
	}
	public String getMgr_no() {
		return mgr_no;
	}


	public String getWrdofc() {
		return wrdofc;
	}


	public String getMain_nm() {
		return main_nm;
	}


	public String getAdres1() {
		return adres1;
	}


	public String getAdres2() {
		return adres2;
	}


	public String getInstl_floor() {
		return instl_floor;
	}


	public String getInstl_ty() {
		return instl_ty;
	}


	public String getInstl_mby() {
		return instl_mby;
	}


	public String getSvc_se() {
		return svc_se;
	}


	public String getCmcwr() {
		return cmcwr;
	}


	public String getCnstc_year() {
		return cnstc_year;
	}


	public String getInout_door() {
		return inout_door;
	}


	public String getRemars3() {
		return remars3;
	}


	public double getLat() {
		return lat;
	}


	public double getLnt() {
		return lnt;
	}


	public String getWork_dttm() {
		return work_dttm;
	}
	
	public double getDistance() {
		return distance;
	}


	FreeWifiInfo() {
		// TODO Auto-generated constructor stub
	}
	
}