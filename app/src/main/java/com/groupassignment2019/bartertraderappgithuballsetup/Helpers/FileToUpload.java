package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.net.Uri;

public class FileToUpload {
    private final Uri localUri;
    private final String remoteFileName;
    private final String remotePath;

    public Uri getLocalUri() {
        return localUri;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public boolean isCompleted() {
        return downloadUri != null;
    }

    public Uri getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(Uri downloadUri) {
        this.downloadUri = downloadUri;
    }

    private boolean isCompleted;
    private Uri downloadUri;

    public FileToUpload(Uri localUri, String remoteFileName, String remotePath) {
        this.localUri = localUri;
        this.remoteFileName = remoteFileName;
        this.remotePath = remotePath;
    }


}
