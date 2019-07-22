package com.bobo.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("cfg.oss-config")
public class OssConfig {

	private String endpoint;
	private String bucketName;
	private String imgServer;
	private String accessKeyId;
	private String accessKeySecret;
	private String roleArn;
	private String durationSeconds;
	private String regionCnHangzhou;
	private String stsApiVersion;

	private String securityToken;
	private Integer serverDateTime;
	private Long suggestCacheTime;
	private String expiration;

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public Integer getServerDateTime() {
		return serverDateTime;
	}

	public void setServerDateTime(Integer serverDateTime) {
		this.serverDateTime = serverDateTime;
	}

	public Long getSuggestCacheTime() {
		return suggestCacheTime;
	}

	public void setSuggestCacheTime(Long suggestCacheTime) {
		this.suggestCacheTime = suggestCacheTime;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getImgServer() {
		return imgServer;
	}

	public void setImgServer(String imgServer) {
		this.imgServer = imgServer;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getRoleArn() {
		return roleArn;
	}

	public void setRoleArn(String roleArn) {
		this.roleArn = roleArn;
	}

	public String getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(String durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public String getRegionCnHangzhou() {
		return regionCnHangzhou;
	}

	public void setRegionCnHangzhou(String regionCnHangzhou) {
		this.regionCnHangzhou = regionCnHangzhou;
	}

	public String getStsApiVersion() {
		return stsApiVersion;
	}

	public void setStsApiVersion(String stsApiVersion) {
		this.stsApiVersion = stsApiVersion;
	}

}
