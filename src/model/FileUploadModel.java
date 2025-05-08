package model;

import java.io.File;

public class FileUploadModel {
    private File[] selectedFiles;

    public File[] getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(File[] files) {
        this.selectedFiles = files;
    }

    public boolean hasFiles() {
        return selectedFiles != null && selectedFiles.length > 0;
    }
}
