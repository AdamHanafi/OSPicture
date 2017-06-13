package ru.mrchebik.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mrchebik on 21.05.17.
 */
@Entity
@Table(name = "files")
public class DataKeyFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String keyFile;
    private String originalFilename;
    private String path;
    private String mimeType;
    private Date startDate;
    private String size;
    private String resolution;
    private String minPath;
    private String path500px;
    private String path200px;

    public DataKeyFile() {
    }

    public DataKeyFile(String keyFile, String originalFilename, String path, String mimeType, String size, String resolution, Date startDate, String path500px, String path200px) {
        this.keyFile = keyFile;
        this.originalFilename = originalFilename;
        this.path = path;
        this.mimeType = mimeType;
        this.size = size;
        this.resolution = resolution;
        this.startDate = startDate;
        this.path500px = path500px;
        this.path200px = path200px;
    }

    public DataKeyFile(String keyFile, String originalFilename, String path, String mimeType, String size, String resolution, Date startDate, String minPath, String path500px, String path200px) {
        this.keyFile = keyFile;
        this.originalFilename = originalFilename;
        this.path = path;
        this.mimeType = mimeType;
        this.size = size;
        this.resolution = resolution;
        this.startDate = startDate;
        this.minPath = minPath;
        this.path500px = path500px;
        this.path200px = path200px;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getScale() {
        return resolution;
    }

    public void setScale(String resolution) {
        this.resolution = resolution;
    }

    public String getMinPath() {
        return minPath;
    }

    public void setMinPath(String minPath) {
        this.minPath = minPath;
    }

    public String getPath500px() {
        return path500px;
    }

    public void setPath500px(String path500px) {
        this.path500px = path500px;
    }

    public String getPath200px() {
        return path200px;
    }

    public void setPath200px(String path200px) {
        this.path200px = path200px;
    }
}
