package pe.com.dipper.pdfupload.models.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author Dipper
 * @project pdf-upload
 * @created 19/11/2020 - 10:45
 */

public interface IUploadFileService {

    public Resource load(String filename) throws MalformedURLException;

    public String copy(MultipartFile file) throws IOException;

    public boolean delete(String filename);

    public void deleteAll();

    public void init() throws IOException;

    public void convertPdfToImage(String filename, String extension, Integer pags[]) throws IOException;
}
