package com.mycompany.pharmacystore.DTO;

public class NhaCungCapDTO {
    private int maNcc;
    private String tenNcc;
    private String sdtNcc;
    private String diaChi;
    private String emailNcc;

    // Constructor
    public NhaCungCapDTO(int maNcc, String tenNcc, String sdtNcc, String diaChi, String emailNcc) {
        this.maNcc = maNcc;
        this.tenNcc = tenNcc;
        this.sdtNcc = sdtNcc;
        this.diaChi = diaChi;
        this.emailNcc = emailNcc;
    }

    // get
    public int getMaNcc() { return maNcc; }
    public String getTenNcc() { return tenNcc; }
    public String getSdtNcc() { return sdtNcc; }
    public String getDiaChi() { return diaChi; }
    public String getEmailNcc() { return emailNcc; }

    // set
    public void setMaNcc(int maNcc) { this.maNcc = maNcc; }
    public void setTenNcc(String tenNcc) { this.tenNcc = tenNcc; }
    public void setSdtNcc(String sdtNcc) { this.sdtNcc = sdtNcc; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public void setEmailNcc(String emailNcc) { this.emailNcc = emailNcc; }

    @Override
    public String toString() {
        return "NhaCungCapDTO{" +
                "maNcc=" + maNcc +
                ", tenNcc='" + tenNcc + '\'' +
                ", sdtNcc='" + sdtNcc + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", emailNcc='" + emailNcc + '\'' +
                '}';
    }
}