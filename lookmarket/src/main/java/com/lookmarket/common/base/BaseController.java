package com.lookmarket.common.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lookmarket.goods.vo.ImageFileVO;

public class BaseController {
	private static final String CURR_IMAGE_REPO_PATH = "C:\\lookmarket_resources\\file_repo";
	
	protected List<ImageFileVO> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List <ImageFileVO> fileList = new ArrayList<ImageFileVO>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()) {
			ImageFileVO imageFileVO = new ImageFileVO();
			String i_file_name = fileNames.next();
			imageFileVO.setI_file_type(i_file_name);
			MultipartFile mFile = multipartRequest.getFile(i_file_name);
			
			String originalFileName = mFile.getOriginalFilename();
			imageFileVO.setI_file_name(originalFileName);
			fileList.add(imageFileVO);
			
			File file = new File(CURR_IMAGE_REPO_PATH + "\\" + i_file_name);
			if(mFile.getSize() != 0) {
				if(! file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH + "\\" + "temp" + "\\" + originalFileName));
			}
		}
		return fileList;
	}
}
