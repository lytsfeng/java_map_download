package com.jmd.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.entity.result.DownloadResult;
import com.jmd.inst.DownloadAmountInstance;
import com.jmd.util.CommonUtils;

@Component
public class HttpDownload {

	@Autowired
	private HttpClient http;
	@Autowired
	private DownloadAmountInstance downloadAmountInstance;

	/** 通过URL下载文件 */
	public DownloadResult downloadTile(String url, HashMap<String, String> headers, int imgType, String path,
			int retry) {
		DownloadResult result = new DownloadResult();
		boolean success = false;
		byte[] bytes = http.getFileBytes(url, headers);
		if (null != bytes) {
			byte[] imgData = imageSwitch(imgType, bytes);
			try {
				if (null != imgData) {
					// result.setImgData(imgData);
					File file = new File(path);
					FileUtils.writeByteArrayToFile(file, imgData);
					if (file.exists() && file.isFile()) {
						downloadAmountInstance.add(file.length());
						success = true;
					}
				}
			} catch (IOException e) {
				success = false;
				e.printStackTrace();
			}
		}
		if (success) {
			result.setSuccess(true);
		} else if (Thread.currentThread().isInterrupted()) {
			result.setSuccess(false);
		} else {
			retry = retry - 1;
			if (retry >= 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return downloadTile(url, headers, imgType, path, retry);
			} else {
				result.setSuccess(false);
			}
		}
		return result;
	}

	/** 下载的图片进行转码 */
	private byte[] imageSwitch(int imgType, byte[] imgData) {
		if (imgType == 0) {
			// 保持PNG
			return imgData;
		} else if (imgType == 1 || imgType == 2 || imgType == 3) {
			// 转换为JPG
			float quality = 0.9f;
			switch (imgType) {
			case 1:
				quality = 0.2f;
				break;
			case 2:
				quality = 0.6f;
				break;
			case 3:
				quality = 0.9f;
				break;
			default:
				break;
			}
			try {
				return CommonUtils.png2jpg(imgData, quality);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
