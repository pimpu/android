package com.alchemistdigital.ziko.models;

/**
 * Created by user on 5/6/2016.
 */
public class DirectorySpinnerItem {
    private String directoryName;
    private String directoryPath;

    @Override
    public String toString() {
        return this.directoryName;
    }

    public DirectorySpinnerItem() {
    }

    public DirectorySpinnerItem(String directoryName, String directoryPath) {
        this.directoryName = directoryName;
        this.directoryPath = directoryPath;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
