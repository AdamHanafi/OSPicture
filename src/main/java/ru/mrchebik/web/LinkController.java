package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.mrchebik.bean.Utils;
import ru.mrchebik.exception.ResourceNotFoundException;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.model.FilenameFormat;
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

    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        if (dataKeyFile == null) {
            throw new ResourceNotFoundException();
        } else {
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
