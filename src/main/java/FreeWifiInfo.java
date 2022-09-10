import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
class FreeWifiInfo{
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
	private int cnstc_year;
	private String inout_door;
	private String remars3;
	private Double lat;
	private Double lnt;
	private Date work_dttm;
	
	FreeWifiInfo(String mgr_no, String wrdofc, String main_nm, String adres1,
	String adres2, String instl_floor, String instl_ty, String instl_mby,
	String svc_se, String cmcwr, int cnstc_year, String inout_door, String remars3,
	Double lat, Double lnt, Date work_dttm){
		this.mgr_no = mgr_no;
		this.wrdofc = wrdofc;
		this.main_nm =main_nm;
		this.adres1= adres1;
		this.adres2= adres2;
		this.instl_floor=instl_floor;
		this.instl_ty=instl_ty;
		this.instl_mby=instl_mby;
		this.svc_se=svc_se;
		this.cmcwr=cmcwr;
		this.cnstc_year=cnstc_year;
		this.inout_door=inout_door;
		this.remars3=remars3;
		this.lat=lat;
		this.lnt=lnt;
		this.work_dttm=work_dttm;
	}
	
}