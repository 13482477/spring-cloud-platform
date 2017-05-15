package com.siebre.basic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.csource.common.IniFileReader;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.DownloadStream;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 李志强
 *
 */
public class FdfsClient {
	private static Logger logger = LoggerFactory.getLogger(FdfsClient.class);
	private static TrackerClient tracker;
	private static String urlPrefix;

	private static NameValuePair[] createMetaData(Map<String, String> map) {
		NameValuePair[] nameValuePairs = new NameValuePair[map.size()];
		int i = 0;
		for (Map.Entry entry : map.entrySet()) {
			nameValuePairs[(i++)] = new NameValuePair((String) entry.getKey(), (String) entry.getValue());
		}
		return nameValuePairs;
	}

	public String upload(String local_filename, String file_ext_name, Map<String, String> metaData) {
		String fileId = null;
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			if ((metaData != null) && (metaData.size() > 0))
				fileId = client.upload_file1(local_filename, file_ext_name, createMetaData(metaData));
			else {
				fileId = client.upload_file1(local_filename, file_ext_name, null);
			}

			logger.info("upload success. file id is: {}", fileId);
		} catch (Exception e) {
			logger.error("FDFS上传异常", e);
		} finally {
			try {
				if (trackerServer != null)
					trackerServer.close();
			} catch (IOException e) {
				logger.error("Tracker Server Connection关闭异常", e);
			}
		}
		return urlPrefix + fileId;
	}

	public String upload(byte[] file_bytes, String file_ext_name, Map<String, String> metaData) {
		String fileId = null;
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			if ((metaData != null) && (metaData.size() > 0))
				fileId = client.upload_file1(file_bytes, file_ext_name, createMetaData(metaData));
			else {
				fileId = client.upload_file1(file_bytes, file_ext_name, null);
			}
			logger.info("upload success. file id is: {}", fileId);
		} catch (Exception e) {
			logger.error("FDFS上传异常", e);
		} finally {
			try {
				if (trackerServer != null)
					trackerServer.close();
			} catch (IOException e) {
				logger.error("Tracker Server Connection关闭异常", e);
			}
		}
		return urlPrefix + fileId;
	}

	public String upload(long file_size, String file_ext_name, InputStream inputStream, Map<String, String> metaData) {
		String fileId = null;
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			if ((metaData != null) && (metaData.size() > 0))
				fileId = client.upload_file1(null, file_size, new UploadStream(inputStream, file_size), file_ext_name, createMetaData(metaData));
			else {
				fileId = client.upload_file1(null, file_size, new UploadStream(inputStream, file_size), file_ext_name, null);
			}
			logger.info("upload success. file id is: {}", fileId);
		} catch (Exception e) {
			logger.error("FDFS上传异常", e);
		} finally {
			try {
				if (trackerServer != null)
					trackerServer.close();
			} catch (IOException e) {
				logger.error("Tracker Server Connection关闭异常", e);
			}
		}
		return urlPrefix + fileId;
	}

	public byte[] download(String fileId) {
		byte[] fileByte = null;
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			fileByte = client.download_file1(fileId);
			logger.info("download success. file id is: {}", fileId);
		} catch (Exception e) {
			logger.error("FDFS下载异常", e);
		} finally {
			try {
				if (trackerServer != null)
					trackerServer.close();
			} catch (IOException e) {
				logger.error("Tracker Server Connection关闭异常", e);
			}
		}
		return fileByte;
	}

	public void download(String fileId, OutputStream outputStream) {
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		try {
			trackerServer = tracker.getConnection();
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			client.download_file1(fileId, new DownloadStream(outputStream));
			logger.info("download success. file id is: {}", fileId);
		} catch (Exception e) {
			logger.error("FDFS下载异常", e);
		} finally {
			try {
				if (trackerServer != null)
					trackerServer.close();
			} catch (IOException e) {
				logger.error("Tracker Server Connection关闭异常", e);
			}
		}
	}

	static {
		try {
			String classPath = FdfsClient.class.getClassLoader().getResource("").getPath();
			String configPath = classPath + "fdfs_client.conf";
			ClientGlobal.init(configPath);
			IniFileReader iniReader = new IniFileReader(configPath);
			urlPrefix = iniReader.getStrValue("http.url.prefix") != null ? iniReader.getStrValue("http.url.prefix") : "";
			logger.info("network_timeout={}ms", Integer.valueOf(ClientGlobal.g_network_timeout));
			logger.info("charset={}");
			tracker = new TrackerClient();
		} catch (Exception e) {
			logger.error("FDFS初始化异常", e);
		}
	}
}