package egovframework.sprink.dto;

import org.springframework.web.multipart.MultipartFile;

public class CommonFileVo {
	private MultipartFile mpfile;

	private String fileName;
	private String orgFileName;
	
	public MultipartFile getMpfile() {
		return mpfile;
	}
	public void setMpfile(MultipartFile mpfile) {
		this.mpfile = mpfile;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOrgFileName() {
		return orgFileName;
	}
	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}
		
}
