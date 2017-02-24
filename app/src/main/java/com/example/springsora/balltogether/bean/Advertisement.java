package com.example.springsora.balltogether.bean;

public class Advertisement {
	private String pic_path;
	private String url;
	public Advertisement(String pic_path, String url) {
		super();
		this.pic_path = pic_path;
		this.url = url;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
