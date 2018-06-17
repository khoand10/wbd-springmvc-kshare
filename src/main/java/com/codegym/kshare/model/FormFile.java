package com.codegym.kshare.model;

import org.springframework.web.multipart.MultipartFile;

public class FormFile {
    private int id;
    private String name;
    private MultipartFile file;
    private String url;
    private String description;
    private boolean share;
    private User user;

    public FormFile(){

    }

    public FormFile(int id, String name, MultipartFile file, String url, String description, boolean share, User user) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.url = url;
        this.description = description;
        this.share = share;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isShare() {
        return share;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "FormFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", share=" + share +
                ", user=" + user +
                '}';
    }
}
