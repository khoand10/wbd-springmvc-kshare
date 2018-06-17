package com.codegym.kshare.controller;

import com.codegym.kshare.model.File;
import com.codegym.kshare.model.FormFile;
import com.codegym.kshare.model.User;
import com.codegym.kshare.service.FileService;
import com.codegym.kshare.until.StorageUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    JavaMailSender javaMailSender;

    final String VIEW_CREATE_FILE = "/file/create";
    final String VIEW_EDIT_FILE = "/file/edit";

    @RequestMapping("/")
    public ModelAndView loadIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/index");
        modelAndView.addObject("files", fileService.findAll());
        return modelAndView;
    }

    @GetMapping("/file/upload")
    public ModelAndView uploadForm(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("user").toString());
        ModelAndView modelAndView = new ModelAndView(VIEW_CREATE_FILE);
        modelAndView.addObject("fileform", new FormFile());
        return modelAndView;
    }

    @PostMapping("/file/upload")
    public ModelAndView uploadFile(@ModelAttribute("fileform") FormFile formFile, HttpServletRequest request, HttpSession session) {
        File file = new File();
        try {
            String randomCode = UUID.randomUUID().toString();
            String originFileName = formFile.getFile().getOriginalFilename();
            String randomName = randomCode + StorageUtils.getFileExtension(originFileName);
            formFile.getFile().transferTo(new java.io.File(StorageUtils.FEATURE_LOCATION + "/" + randomName));

            file.setName(formFile.getName());
            file.setUrl(randomName);

            file.setDescription(formFile.getDescription());
            file.setShare(formFile.isShare());
            file.setUser(formFile.getUser());
            System.out.println(file.toString());
            fileService.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView(VIEW_CREATE_FILE);
        fileService.save(file);
        modelAndView.addObject("fileform", new FormFile());
        modelAndView.addObject("message", "Upload Success!");
        return modelAndView;
    }

    @GetMapping("/file/edit/{id}")
    public ModelAndView editForm(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView(VIEW_EDIT_FILE);
        File file = fileService.findById(id);
        FormFile formFile = new FormFile();

        formFile.setId(file.getId());
        formFile.setName(file.getName());
        formFile.setUrl(file.getUrl());
        formFile.setDescription(file.getDescription());
        formFile.setShare(file.isShare());
        modelAndView.addObject("fileedit", formFile);
        return modelAndView;
    }

    @PostMapping("/file/edit")
    public ModelAndView editFile(@ModelAttribute("fileedit") FormFile formFile) {
        try {
            File file = fileService.findById(formFile.getId());
            if (!formFile.getFile().isEmpty()) {
                StorageUtils.removeFeature(file.getUrl());
                String randomCode = UUID.randomUUID().toString();
                String originFileName = formFile.getFile().getOriginalFilename();
                String randomName = randomCode + StorageUtils.getFileExtension(originFileName);
                formFile.getFile().transferTo(new java.io.File(StorageUtils.FEATURE_LOCATION + "/" + randomName));
                file.setUrl(randomName);
                formFile.setUrl(randomName);
            }
            file.setId(formFile.getId());
            file.setName(formFile.getName());
            file.setDescription(formFile.getDescription());
            file.setShare(formFile.isShare());
            fileService.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView("/file/edit");
        modelAndView.addObject("fileedit", formFile);
        modelAndView.addObject("message", "Update success!");
        return modelAndView;
    }

    @GetMapping("/file/delete/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        fileService.delete(id);
        modelAndView.addObject("message", "Delete success");
        return modelAndView;
    }

    @GetMapping("/file/download/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("id") int id) throws IOException {

        File file = fileService.findById(id);
        java.io.File file1 = new java.io.File(StorageUtils.FEATURE_LOCATION + "/" + file.getUrl());


        InputStreamResource resource = new InputStreamResource(new FileInputStream(file1));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(MediaType.MULTIPART_FORM_DATA).contentLength(file1.length())
                .body(resource);
    }

    @GetMapping("/file/email/{id}")
    public ModelAndView emailForm(@PathVariable("id") int id
            , ModelMap model
    ) {
        File file = fileService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/file/email");
        modelAndView.addObject("file", file);
        return modelAndView;
    }

    @PostMapping("/file/mail")
    public ModelAndView shareByEmail(
            @ModelAttribute("file") File file,
            @RequestParam("email") String emailR,
            @RequestParam("content") String content
    ) {
        ModelAndView modelAndView = new ModelAndView("/file/email");
        String url = "http://localhost:8080/file/download/" + file.getId();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailR);
        email.setSubject("Wanacry file");
        email.setText(content + "\n" + url);
        javaMailSender.send(email);
        modelAndView.addObject("message", "Send success");
        return modelAndView;
    }

    @GetMapping("/file/change/{id}")
    @ResponseBody
    public ResponseEntity<File> changStatus(@PathVariable("id") int id) {
        System.out.println("test sssssss");
        File file = fileService.findById(id);
        file.setShare(!file.isShare());
        fileService.save(file);
        return new ResponseEntity<>(file, HttpStatus.OK);

    }

}