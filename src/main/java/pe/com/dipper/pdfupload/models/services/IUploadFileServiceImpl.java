package pe.com.dipper.pdfupload.models.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dipper
 * @project pdf-upload
 * @created 19/11/2020 - 10:48
 */
@Service
public class IUploadFileServiceImpl implements IUploadFileService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathArchivo = getPath(filename);
        log.info("pathArchivo: " + pathArchivo);
        Resource recurso = null;

        recurso = new UrlResource(pathArchivo.toUri());
        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("Error no se puede cargar el archivo: " + pathArchivo.toString());
        }
        return recurso;
    }

    @Override
    public String copy(MultipartFile file, String uuid) throws IOException {
        String uniqueFilename = uuid + "_" + file.getOriginalFilename();
        Path rootPath = getPath(uniqueFilename);

        log.info("rootPath: " + rootPath);

        Files.copy(file.getInputStream(), rootPath);

        return uniqueFilename;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPath = getPath(filename);
        File archivo = rootPath.toFile();
        if (archivo.exists() && archivo.canRead()) {
            if (archivo.delete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    @Override
    public void init() throws IOException {
        Files.createDirectories(Paths.get(UPLOADS_FOLDER));
    }

    @Override
    public void convertPdfToImage(String filename, String extension, Integer page, String uuid) throws IOException {
        PDDocument pdf2 = PDDocument.load(new File(filename));
        PDFRenderer pdfRenderer = new PDFRenderer(pdf2);
        BufferedImage bim = pdfRenderer.renderImageWithDPI(page - 1, 100, ImageType.RGB);
        System.out.println("src/output/img-" + uuid);
        ImageIOUtil.writeImage(bim, String.format("uploads/%s.%s", uuid, extension), 100);

        pdf2.close();
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
