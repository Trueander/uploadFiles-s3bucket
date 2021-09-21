package com.upload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upload.service.StorageService;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageService storageService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
		return new ResponseEntity<>(storageService.uploadFile(file), HttpStatus.OK);
	}
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filename){
		byte[] data = storageService.downloadFile(filename);
		ByteArrayResource resource = new ByteArrayResource(data);
		
		return ResponseEntity
				.ok()
				.contentLength(data.length)
				.header("Content.type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + filename + "\"")
				.body(resource);
	}
	
	@DeleteMapping("/delete/{filename}")
	public ResponseEntity<String> deleteFile(@PathVariable String filename){
		return new ResponseEntity<String>(storageService.deleteFile(filename), HttpStatus.OK);
	}
	
	
}
