package com.codegym.kshare.service.impl;

import com.codegym.kshare.model.File;
import com.codegym.kshare.repository.FileRepository;
import com.codegym.kshare.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public Iterable<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public void delete(int id) {
        fileRepository.delete(id);
    }

    @Override
    public File findById(int id) {
        return fileRepository.findOne(id);
    }

}
