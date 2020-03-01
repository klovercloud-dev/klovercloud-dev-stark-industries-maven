package com.starkindustries.business.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.starkindustries.business.api.S3Api;
import com.starkindustries.business.common.Util;
import com.starkindustries.business.service.S3Service;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("api/v1/")
public class S3ApiImplementation implements S3Api{
	
	@Autowired
	S3Service s3Service;

	@Override
	public ResponseEntity<?> uploadMultipartFile(@RequestBody MultipartFile file) {
		try {
			s3Service.uploadFile(file.getOriginalFilename(), file);
			return ResponseEntity.ok().body("Operation Successful!");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Failed to upload files!");
		}
	}

	@Override
	public ResponseEntity<?> downloadFile(@PathVariable  String keyname) {
		try {		
		ByteArrayOutputStream downloadInputStream = s3Service.downloadFile(keyname);
		return ResponseEntity.ok()
					.contentType(Util.contentType(keyname))
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
					.body(downloadInputStream.toByteArray());
		}catch (Exception ioe) {
			return ResponseEntity.badRequest()
					.contentType(Util.contentType(keyname))
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
					.body(ioe);
		}
		
	}

}
