package com.nostra13.universalimageloader.core.download;

import android.text.TextUtils;

/**
 * SendSuiSuiActivity 中 表单  文件 对象
 * @author DayuXu
 *
 * 2011-5-23下午08:26:05
 */
public class FormFile {
	/* 上传文件的数据 */
	private byte[] data;
	/* 文件名称 */
	private String filename;
	/* 表单字段名称*/
	private String formname;
	/* 内容类型 */
	private String contentType = "application/octet-stream";
	
//	public FormFile(String contentType) {
//		if(contentType!=null) this.contentType = contentType;
//	}
	
	public FormFile(String formname, byte[] data,String filename ) {
		this.data = data;
		if(TextUtils.isEmpty(filename)){
			this.filename="1.png";
		}else
			this.filename = filename;
		this.formname = formname;
		//if(contentType!=null) this.contentType = contentType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}