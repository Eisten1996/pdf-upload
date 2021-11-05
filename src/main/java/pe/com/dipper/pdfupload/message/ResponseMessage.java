package pe.com.dipper.pdfupload.message;

public class ResponseMessage {
    private String message;
    private String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseMessage(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public ResponseMessage(String message) {
        this.message = message;
    }
}
