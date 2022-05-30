package premium.conservadores.sistema_conservadores.Clases;

public class Cls_Mensaje {

    public String Titulo;
    public String Mensaje;
    public String Estatus;
    public String Url_PDF;
    public Boolean isCreatePDF;
    public Boolean isSendEmail;
    public String ID;
    public String ID_Cambio;
    public String ID_Extension;
    public String Informe_Tecnico;


    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    public String getUrl_PDF() {
        return Url_PDF;
    }

    public void setUrl_PDF(String url_PDF) {
        Url_PDF = url_PDF;
    }

    public Boolean getCreatePDF() {
        return isCreatePDF;
    }

    public void setCreatePDF(Boolean createPDF) {
        isCreatePDF = createPDF;
    }

    public Boolean getSendEmail() {
        return isSendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        isSendEmail = sendEmail;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID_Cambio() {
        return ID_Cambio;
    }

    public void setID_Cambio(String ID_Cambio) {
        this.ID_Cambio = ID_Cambio;
    }

    public String getID_Extension() {
        return ID_Extension;
    }

    public void setID_Extension(String ID_Extension) {
        this.ID_Extension = ID_Extension;
    }

    public String getInforme_Tecnico() {
        return Informe_Tecnico;
    }

    public void setInforme_Tecnico(String informe_Tecnico) {
        Informe_Tecnico = informe_Tecnico;
    }
}
