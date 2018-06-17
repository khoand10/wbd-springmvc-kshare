package com.codegym.kshare.service;

import com.codegym.kshare.model.File;

import java.util.List;

public interface FileService {

    File save(File file);

    Iterable<File> findAll();

    void delete(int id);

    File findById(int id);

}
