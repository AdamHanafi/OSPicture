package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mrchebik.bean.Utils;
import ru.mrchebik.exception.ResourceNotFoundException;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.model.FilenameFormat;
import ru.mrchebik.model.InfoImage;
import ru.mrchebik.service.DataKeyFileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrchebik on 15.05.17.
 */
@Controller
public class LinkController {
    private final DataKeyFileService dataKeyFileService;
    private final Utils utils;

    @Autowired
    public LinkController(DataKeyFileService dataKeyFileService,
                          Utils utils) {
        this.dataKeyFileService = dataKeyFileService;
        this.utils = utils;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(Model model) {
        model.addAttribute("notFound", "true");
        return "404";
    }

    @GetMapping("/")
    public String handleAbsolutePath() {
        return "index";
    }

    @GetMapping("/info_image/{key}")
    @ResponseBody
    public ResponseEntity<InfoImage> handleGetInfoImage(@PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);
        InfoImage infoImage = new InfoImage();

        String keyFile = dataKeyFile.getKeyFile();
        if (keyFile.length() == utils.KEY_LENGTH) {
            String folderPath = dataKeyFile.getPath().split(keyFile)[0];
            if (!folderPath.equals(utils.PATH_PICTURES)) {
                File folder = new File(folderPath);
                String[] folderFiles = folder.list();
                for (int i = 0; i < folderFiles.length; i++) {
                    if (folderFiles[i].contains(key)) {
                        infoImage.setFolderLeft(folderFiles[i == 0 ? folderFiles.length - 1 : (i - 1)].substring(0, utils.KEY_LENGTH));
                        infoImage.setFolderRight(folderFiles[i == folderFiles.length - 1 ? 0 : (i + 1)].substring(0, utils.KEY_LENGTH));
                        break;
                    }
                }
            }
        }

        infoImage.setKey(dataKeyFile.getKeyFile());
        infoImage.setName(dataKeyFile.getOriginalFilename());
        infoImage.setSize(dataKeyFile.getSize());
        boolean isOctetStream = dataKeyFile.getMimeType().equals("octet-stream");
        infoImage.setFormat(isOctetStream ? "png" : dataKeyFile.getMimeType());
        infoImage.setIsOctetStream(String.valueOf(isOctetStream));
        infoImage.setResolution(dataKeyFile.getScale());

        DataKeyFile px500 = dataKeyFileService.get(dataKeyFile.getPath500px());
        DataKeyFile px200 = dataKeyFileService.get(dataKeyFile.getPath200px());

        boolean isEqual500 = key.contains("500_"), isEqual200 = key.contains("200_");

        infoImage.setPx500Path(isEqual500 ? "image/" + px500.getKeyFile() : "image/500_" + (isEqual200 ? px200.getKeyFile() : key));
        infoImage.setPx200Path(isEqual200 ? "image/" + px200.getKeyFile() : "image/200_" + (isEqual500 ? px500.getKeyFile() : key));

        if (isEqual500) {
            infoImage.setPx500TRUE(String.valueOf(1));
        } else if (isEqual200) {
            infoImage.setPx200TRUE(String.valueOf(1));
        }

        return new ResponseEntity<>(infoImage, HttpStatus.OK);
    }
    
    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        if (dataKeyFile == null) {
            throw new ResourceNotFoundException();
        } else {
            String keyFile = dataKeyFile.getKeyFile();
            if (keyFile.length() == utils.KEY_LENGTH) {
                String folderPath = dataKeyFile.getPath().split(keyFile)[0];
                if (!folderPath.equals(utils.PATH_PICTURES)) {
                    File folder = new File(folderPath);
                    model.addAttribute("isFromFolder", folder.getName());
                    String[] folderFiles = folder.list();
                    for (int i = 0; i < folderFiles.length; i++) {
                        if (folderFiles[i].contains(key)) {
                            model.addAttribute("folderLeft", folderFiles[i == 0 ? folderFiles.length - 1 : (i - 1)].substring(0, utils.KEY_LENGTH));
                            model.addAttribute("folderRight", folderFiles[i == folderFiles.length - 1 ? 0 : (i + 1)].substring(0, utils.KEY_LENGTH));
                            break;
                        }
                    }
                }
            }

            model.addAttribute("key", dataKeyFile.getKeyFile());
            model.addAttribute("name", dataKeyFile.getOriginalFilename());
            model.addAttribute("size", dataKeyFile.getSize());
            boolean isOctetStream = dataKeyFile.getMimeType().equals("octet-stream");
            model.addAttribute("format", isOctetStream ? "png" : dataKeyFile.getMimeType());
            model.addAttribute("isOctetStream", isOctetStream);
            model.addAttribute("resolution", dataKeyFile.getScale());

            DataKeyFile px500 = dataKeyFileService.get(dataKeyFile.getPath500px());
            DataKeyFile px200 = dataKeyFileService.get(dataKeyFile.getPath200px());

            boolean isEqual500 = key.contains("500_"), isEqual200 = key.contains("200_");

            model.addAttribute("px500Path", isEqual500 ? "image/" + px500.getKeyFile() : "image/500_" + (isEqual200 ? px200.getKeyFile() : key));
            model.addAttribute("px200Path", isEqual200 ? "image/" + px200.getKeyFile() : "image/200_" + (isEqual500 ? px500.getKeyFile() : key));

            if (isEqual500) {
                model.addAttribute("px500TRUE", 1);
            } else if (isEqual200) {
                model.addAttribute("px200TRUE", 1);
            }

            return "index";
        }
    }

    @GetMapping("/folder/{key}")
    public String handleGetFolder(Model model,
                                   @PathVariable String key) throws IOException {
        File folder = new File(utils.PATH_PICTURES + key);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            ArrayList<FilenameFormat> keyFiles = new ArrayList<>();

            for (File file : files) {
                DataKeyFile dataKeyFile = dataKeyFileService.get(file.getName().substring(0, 10));
                keyFiles.add(dataKeyFile.getMimeType().equals("octet-stream") ? new FilenameFormat(dataKeyFile.getKeyFile(), dataKeyFile.getMimeType(), true) : new FilenameFormat(dataKeyFile.getKeyFile(), dataKeyFile.getMimeType()));
            }

            model.addAttribute("files", keyFiles);
            model.addAttribute("folder", true);

            return "index";
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
