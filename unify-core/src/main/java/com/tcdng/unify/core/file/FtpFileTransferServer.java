/*
 * Copyright (c) 2018-2026 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tcdng.unify.core.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;

/**
 * File transfer server based on FTP.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(name = ApplicationComponents.APPLICATION_FTPTRANSFERSERVER, description = "FTP")
public class FtpFileTransferServer extends AbstractFileTransferServer {

    @Override
    public List<FileInfo> getRemoteFileList(FileTransferSetup fileTransferSetup) throws UnifyException {
        List<FileInfo> list = Collections.emptyList();
        FtpFileFilter ftpFileFilter = new FtpFileFilter(fileTransferSetup);
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            FTPFile[] files = ftpClient.listFiles(getNormalizedRemotePath(fileTransferSetup), ftpFileFilter);
            if (files != null && files.length > 0) {
                list = new ArrayList<FileInfo>();
                for (FTPFile file : files) {
                    list.add(new FileInfo(file.getName(), null, file.getSize(), file.getTimestamp().getTimeInMillis(),
                            file.getTimestamp().getTimeInMillis(), file.isFile(), false));
                }
            }
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            restoreFTPClient(ftpClient);
        }
        return list;
    }

    @Override
    public boolean remoteDirectoryExists(FileTransferSetup fileTransferSetup) throws UnifyException {
        return remoteDirectoryExists(fileTransferSetup, getNormalizedRemotePath(fileTransferSetup));
    }

    @Override
    public boolean remoteFileExists(FileTransferSetup fileTransferSetup, String serverFile) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        InputStream inputStream = null;
        try {
            inputStream = ftpClient.retrieveFileStream(getNormalizedRemotePath(fileTransferSetup) + serverFile);
            if (inputStream == null || ftpClient.getReplyCode() == 550) {
                return false;
            }
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
            restoreFTPClient(ftpClient);
        }
        return true;
    }

    @Override
    public void createRemoteDirectory(FileTransferSetup fileTransferSetup) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            makeRemoteDirectories(fileTransferSetup, ftpClient, getNormalizedRemotePath(fileTransferSetup));
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

    @Override
    public void createRemoteFile(FileTransferSetup fileTransferSetup, String serverFile) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
            boolean created = ftpClient.storeFile(pathName, new ByteArrayInputStream(DataUtils.ZEROLEN_BYTE_ARRAY));
            if (!created) {
                throw new Exception("Failed to create file [" + ftpClient.getReplyString() + "].");
            }
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

    @Override
    public void deleteRemoteFile(FileTransferSetup fileTransferSetup, String serverFile) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
            boolean deleted = ftpClient.deleteFile(pathName);
            if (!deleted) {
                throw new Exception("Failed to delete file [" + ftpClient.getReplyCode() + "].");
            }
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

    @Override
    public byte[] readRemoteBlock(FileTransferSetup fileTransferSetup, String serverFile, long index, int size)
            throws UnifyException {
        throwUnsupportedOperationException();
        return null;
    }

	@Override
	public void uploadFile(FileTransferSetup fileTransferSetup, String serverFile, String localFile)
			throws UnifyException {
		FTPClient ftpClient = getFTPClient(fileTransferSetup);
		try {
			String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
			File actLocalFile = getLocalFile(fileTransferSetup, localFile);
			try (InputStream in = new FileInputStream(actLocalFile)) {
				uploadFile(ftpClient, pathName, in);
			}

			if (fileTransferSetup.isDeleteSourceOnTransfer()) {
				actLocalFile.delete();
				logDebug("Local file deleted.");
			}
		} catch (Exception e) {
			throwOperationErrorException(e);
		} finally {
			restoreFTPClient(ftpClient);
		}
	}

    @Override
	public void uploadFile(FileTransferSetup fileTransferSetup, String serverFile, InputStream in)
			throws UnifyException {
		FTPClient ftpClient = getFTPClient(fileTransferSetup);
		try {
			String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
			uploadFile(ftpClient, pathName, in);
		} catch (Exception e) {
			throwOperationErrorException(e);
		} finally {
			restoreFTPClient(ftpClient);
		}
	}

	@Override
	public void uploadFile(FileTransferSetup fileTransferSetup, String serverFile, byte[] file) throws UnifyException {
		FTPClient ftpClient = getFTPClient(fileTransferSetup);
		try(ByteArrayInputStream in = new ByteArrayInputStream(file)) {
			String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
			uploadFile(ftpClient, pathName, in);
		} catch (Exception e) {
			throwOperationErrorException(e);
		} finally {
			restoreFTPClient(ftpClient);
		}
	}

	@Override
    public void uploadFiles(FileTransferSetup fileTransferSetup) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            String remotePath = getNormalizedRemotePath(fileTransferSetup);
            File localDir = new File(getNormalizedLocalPath(fileTransferSetup));
            FileFilter fileFilter = new FileFilter(fileTransferSetup);
            uploadFiles(fileTransferSetup, ftpClient, remotePath, localDir, fileFilter);
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

    @Override
    public void downloadFile(FileTransferSetup fileTransferSetup, String serverFile, String localFile)
            throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
            File actLocalFile = getLocalFile(fileTransferSetup, localFile);
			try (OutputStream out = new FileOutputStream(actLocalFile)) {
				downloadFile(ftpClient, pathName, out, fileTransferSetup.isDeleteSourceOnTransfer());
			}
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

	@Override
	public void downloadFile(FileTransferSetup fileTransferSetup, String serverFile, OutputStream out)
			throws UnifyException {
		FTPClient ftpClient = getFTPClient(fileTransferSetup);
		try {
			String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
			downloadFile(ftpClient, pathName, out, false);
			out.flush();
		} catch (Exception e) {
			throwOperationErrorException(e);
		} finally {
			restoreFTPClient(ftpClient);
		}
	}

	@Override
	public Optional<byte[]> downloadFile(FileTransferSetup fileTransferSetup, String serverFile) throws UnifyException {
		FTPClient ftpClient = getFTPClient(fileTransferSetup);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			String pathName = getNormalizedRemotePath(fileTransferSetup) + serverFile;
			downloadFile(ftpClient, pathName, baos, false);
			baos.flush();
			return Optional.of(baos.toByteArray());
		} catch (Exception e) {
			throwOperationErrorException(e);
		} finally {
			restoreFTPClient(ftpClient);
		}
		
		return Optional.empty();
	}

	@Override
    public void downloadFiles(FileTransferSetup fileTransferSetup) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            String remotePath = getNormalizedRemotePath(fileTransferSetup);
            File localDir = new File(getNormalizedLocalPath(fileTransferSetup));
            FtpFileFilter ftpFileFilter = new FtpFileFilter(fileTransferSetup);
            downloadFiles(ftpClient, remotePath, localDir, ftpFileFilter, fileTransferSetup.isDeleteSourceOnTransfer());
        } finally {
            restoreFTPClient(ftpClient);
        }
    }

	private void uploadFiles(FileTransferSetup fileTransferSetup, FTPClient ftpClient, String remotePath, File localDir,
			FileFilter fileFilter) throws UnifyException {
		makeRemoteDirectories(fileTransferSetup, ftpClient, remotePath);
		try {
			File[] files = localDir.listFiles(fileFilter);
			for (File file : files) {
				if (file.isDirectory()) {
					String newRemotePath = remotePath + file.getName() + '/';
					uploadFiles(fileTransferSetup, ftpClient, newRemotePath, file, fileFilter);
				} else {
					try (InputStream in = new FileInputStream(file)) {
						String remoteFile = remotePath + file.getName();
						uploadFile(ftpClient, remoteFile, in);
					}
					
					if (fileTransferSetup.isDeleteSourceOnTransfer()) {
						file.delete();
					}
				}
			}
		} catch (IOException e) {
			throwOperationErrorException(e);
		}
	}

    private void uploadFile(FTPClient ftpClient, String remoteFile, InputStream in)
            throws UnifyException {
        try {
            logDebug("Upload: [Remote: {0}]", remoteFile);
            boolean uploaded = ftpClient.storeFile(remoteFile, in);
            if (uploaded) {
                logDebug("Upload: [Status: SENT ]");
            } else {
                throw new Exception("Failed to upload file [" + ftpClient.getReplyString() + "].");
            }
        } catch (Exception e) {
            throwOperationErrorException(e);
        }
    }

	private void downloadFiles(FTPClient ftpClient, String remotePath, File localDir, FtpFileFilter ftpFileFilter,
			boolean deleteOnTransfer) throws UnifyException {
		try {
			localDir.mkdirs();
			FTPFile[] files = ftpClient.listFiles(remotePath, ftpFileFilter);
			for (FTPFile file : files) {
				File localFile = new File(getNormalizedLocalPath(localDir.getAbsolutePath()) + file.getName());
				if (file.isDirectory()) {
					String newRemotePath = remotePath + file.getName() + '/';
					downloadFiles(ftpClient, newRemotePath, localFile, ftpFileFilter, deleteOnTransfer);
				} else {
					try (OutputStream out = new FileOutputStream(localFile)) {
						String remoteFile = remotePath + file.getName();
						downloadFile(ftpClient, remoteFile, out, deleteOnTransfer);
					}
				}
			}
		} catch (IOException e) {
			throwOperationErrorException(e);
		}
	}

    private void downloadFile(FTPClient ftpClient, String remoteFile, OutputStream out, boolean deleteOnTransfer)
            throws UnifyException {
        try {
            logDebug("Download: [File: {0}]", remoteFile);
            boolean downloaded = ftpClient.retrieveFile(remoteFile, out);
            if (downloaded) {
                logDebug("Download: [Status: RECEIVED ]");
                if (deleteOnTransfer) {
                    ftpClient.deleteFile(remoteFile);
                    logDebug("Remote file deleted.");
                }
            } else {
                throw new Exception("Failed to download file [" + ftpClient.getReplyString() + "].");
            }
        } catch (Exception e) {
            throwOperationErrorException(e);
        }
    }

    private void makeRemoteDirectories(FileTransferSetup fileTransferSetup, FTPClient ftpClient, String remotePath)
            throws UnifyException {
        try {
            if (remotePath.startsWith("/")) {
                remotePath = remotePath.substring(1);
            }

            if (remotePath.endsWith("/")) {
                remotePath = remotePath.substring(0, remotePath.length() - 1);
            }

            String[] elems = remotePath.split("/");
            StringBuilder sb = new StringBuilder();
            for (String elem : elems) {
                sb.append('/');
                sb.append(elem);
                String actRemotePath = sb.toString();
                if (!remoteDirectoryExists(fileTransferSetup, actRemotePath)) {
                    boolean created = ftpClient.makeDirectory(actRemotePath);
                    if (!created) {
                        throw new Exception("Failed to create directory [" + ftpClient.getReplyString() + "].");
                    }
                }
            }

        } catch (Exception e) {
            throwOperationErrorException(e);
        }
    }

    private boolean remoteDirectoryExists(FileTransferSetup fileTransferSetup, String remotePath) throws UnifyException {
        FTPClient ftpClient = getFTPClient(fileTransferSetup);
        try {
            ftpClient.changeWorkingDirectory(remotePath);
            if (ftpClient.getReplyCode() == 550) {
                return false;
            }
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            restoreFTPClient(ftpClient);
        }
        return true;
    }

    private FTPClient getFTPClient(FileTransferSetup fileTransferSetup) throws UnifyException {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(fileTransferSetup.getRemoteHost(), fileTransferSetup.getRemotePort());
            if (!ftpClient.login(fileTransferSetup.getAuthenticationId(),
                    fileTransferSetup.getAuthenticationPassword())) {
                throw new Exception("Failed to log into FTP server [" + fileTransferSetup.getRemoteHost() + "].");
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

        return ftpClient;
    }

    private void restoreFTPClient(FTPClient ftpClient) throws UnifyException {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            throwOperationErrorException(e);
        }
    }

    private class FtpFileFilter extends FileFilter implements FTPFileFilter {

        public FtpFileFilter(FileTransferSetup fileTransferSetup) {
            super(fileTransferSetup.getFilePrefixes(), fileTransferSetup.getFileSuffixes());
        }

        @Override
        public boolean accept(FTPFile ftpFile) {
            return accept(ftpFile.getName(), ftpFile.isFile());
        }

    }
}
