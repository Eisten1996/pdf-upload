package pe.com.dipper.pdfupload.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.com.dipper.pdfupload.message.ResponseMessage;
import pe.com.dipper.pdfupload.models.services.IUploadFileService;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author Dipper
 * @project pdf-upload
 * @created 19/11/2020 - 15:46
 */
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class UploadPdfController {
    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping(value = "/output/{filename:.+}")
    public ResponseEntity<Resource> verArchivo(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(recurso);
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> guardar(@RequestParam("file") MultipartFile pdf, @RequestParam("pags") String pags) {
        Integer[] pages = {Integer.parseInt(pags)};
        String message = "";
        String uniqueFilename = null;
        try {
            uniqueFilename = uploadFileService.copy(pdf);
            System.out.println(uniqueFilename);
            message = "Uploaded the file successfully: " + pdf.getOriginalFilename();
            uploadFileService.convertPdfToImage("uploads\\" + uniqueFilename, "jpg", pages);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException e) {
            message = "Could not upload the file: " + pdf.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
