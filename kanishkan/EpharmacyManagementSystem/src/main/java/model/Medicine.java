package model;

public class Medicine {

    int medicineId;
    String name;
    String brand;
    String strength;
    String composition;
    String dosageForm;
    double mrp;
    boolean prescription_required;
    int categoryId;
    String storageInstructions;
    int manufactureId;
    String scheduleType;
    int taxPercentage;
    int discountPercentage;
    double finalPrice;

    Medicine(int medicineId, String name, String brand, String strength, String composition, String dosageForm,
             double mrp, boolean prescription, int categoryId, String storageInstructions, int manufactureId,
             String scheduleType, int taxPercentage, int discountPercentage, double finalPrice) {
        this.medicineId = medicineId;
        this.name = name;
        this.brand = brand;
        this.strength = strength;
        this.composition = composition;
        this.dosageForm = dosageForm;
        this.mrp = mrp;
        this.prescription_required = prescription;
        this.categoryId = categoryId;
        this.storageInstructions = storageInstructions;
        this.manufactureId = manufactureId;
        this.scheduleType = scheduleType;
        this.taxPercentage = taxPercentage;
        this.discountPercentage = discountPercentage;
        this.finalPrice = finalPrice;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public boolean isPrescription_required() {
        return prescription_required;
    }

    public void setPrescription_required(boolean prescription_required) {
        this.prescription_required = prescription_required;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public int getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(int manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
