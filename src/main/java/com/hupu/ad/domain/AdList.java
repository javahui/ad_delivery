
package com.hupu.ad.domain;

public class AdList extends AbstractPojo {
	public AdList (){}
	
	private int adId;
	private String title;
	private String name;
	private String introduce;
	private int cid;
	private String mediaUri;
	private String url;
	private int classify;
	private int classifyId;
	private int isPersonal;
	private int isFree;
	
	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getIntroduce() {return introduce;}
	public void setIntroduce(String introduce) {this.introduce = introduce;}
	
	public int getCid() {return cid;}
	public void setCid(int cid) {this.cid = cid;}
	
	public String getMediaUri() {return mediaUri;}	
	public void setMediaUri(String mediaUri) {this.mediaUri = mediaUri;}
	
	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}

	public int getClassify() {return classify;}
	public void setClassify(int classify) {this.classify = classify;}
	
	public int getClassifyId() {return classifyId;}
	public void setClassifyId(int classifyId) {this.classifyId = classifyId;}
	
	public int getIsPersonal() {return isPersonal;}
	public void setIsPersonal(int isPersonal) {this.isPersonal = isPersonal;}
	
	public int getIsFree() {return isFree;}
	public void setIsFree(int isFree) {this.isFree = isFree;}
}