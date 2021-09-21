package com.upload.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;



@Service
public class StorageService {

	@Autowired
	private AmazonS3 s3Cliente;
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StorageService.class);
	
	public String uploadFile(MultipartFile file) {
		File fileObj = convertMutiPartFileToFile(file);
		String filename = System.currentTimeMillis()+"_"+file.getOriginalFilename();
		s3Cliente.putObject(new PutObjectRequest(bucketName, filename, fileObj));
		fileObj.delete();
		return "File uploaded: " + filename;
	}
	
	public byte[] downloadFile(String filename) {
		S3Object s3Object = s3Cliente.getObject(bucketName, filename);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String deleteFile(String filename) {
		s3Cliente.deleteObject(bucketName, filename);
		return filename + " removed"; 
	}
	
	
	private File convertMutiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream  fos = new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());
		}catch(IOException e) {
			log.error("Error converting multipartFile to file", e);
		}
		
		return convertedFile;
	}
	
}
