package com.foodscan.WsHelper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by c157 on 31/01/18.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTOProduct implements Parcelable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("product_image")
    private String productImage;
    @JsonProperty("barcode_id")
    private String barcodeId;
    @JsonProperty("ingrediants")
    private String ingrediants;
    @JsonProperty("protein_amount")
    private String proteinAmount;
    @JsonProperty("fat_amount")
    private String fatAmount;
    @JsonProperty("calories")
    private String calories;
    @JsonProperty("is_organic")
    private String isOrganic;
    @JsonProperty("is_healthy")
    private String isHealthy;
    @JsonProperty("vitamin")
    private String vitamin;
    @JsonProperty("saturated_fats")
    private String saturatedFats;
    @JsonProperty("protein")
    private String protein;
    @JsonProperty("sugar")
    private String sugar;
    @JsonProperty("salt")
    private String salt;
    @JsonProperty("carbohydrate")
    private String carbohydrate;
    @JsonProperty("dietary_fiber")
    private String dietaryFiber;
    @JsonProperty("license_no")
    private String licenseNo;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("modified_date")
    private String modifiedDate;
    @JsonProperty("is_delete")
    private String isDelete;
    @JsonProperty("is_test")
    private String isTest;
    @JsonProperty("is_favourite")
    private String isFavourite;
    @JsonProperty("history_id")
    private String historyId;
    @JsonProperty("history_created_date")
    private String historyCreatedDate;
    @JsonProperty("favourite_id")
    private String favouriteId;
    @JsonProperty("favourite_created_date")
    private String favouriteCreatedDate;


    protected DTOProduct(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        productName = in.readString();
        companyName = in.readString();
        productImage = in.readString();
        barcodeId = in.readString();
        ingrediants = in.readString();
        proteinAmount = in.readString();
        fatAmount = in.readString();
        calories = in.readString();
        isOrganic = in.readString();
        isHealthy = in.readString();
        vitamin = in.readString();
        saturatedFats = in.readString();
        protein = in.readString();
        sugar = in.readString();
        salt = in.readString();
        carbohydrate = in.readString();
        dietaryFiber = in.readString();
        licenseNo = in.readString();
        categoryId = in.readString();
        createdDate = in.readString();
        modifiedDate = in.readString();
        isDelete = in.readString();
        isTest = in.readString();
        isFavourite = in.readString();
        historyId = in.readString();
        historyCreatedDate = in.readString();
        favouriteId = in.readString();
        favouriteCreatedDate = in.readString();
    }

    public DTOProduct() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(productName);
        dest.writeString(companyName);
        dest.writeString(productImage);
        dest.writeString(barcodeId);
        dest.writeString(ingrediants);
        dest.writeString(proteinAmount);
        dest.writeString(fatAmount);
        dest.writeString(calories);
        dest.writeString(isOrganic);
        dest.writeString(isHealthy);
        dest.writeString(vitamin);
        dest.writeString(saturatedFats);
        dest.writeString(protein);
        dest.writeString(sugar);
        dest.writeString(salt);
        dest.writeString(carbohydrate);
        dest.writeString(dietaryFiber);
        dest.writeString(licenseNo);
        dest.writeString(categoryId);
        dest.writeString(createdDate);
        dest.writeString(modifiedDate);
        dest.writeString(isDelete);
        dest.writeString(isTest);
        dest.writeString(isFavourite);
        dest.writeString(historyId);
        dest.writeString(historyCreatedDate);
        dest.writeString(favouriteId);
        dest.writeString(favouriteCreatedDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DTOProduct> CREATOR = new Creator<DTOProduct>() {
        @Override
        public DTOProduct createFromParcel(Parcel in) {
            return new DTOProduct(in);
        }

        @Override
        public DTOProduct[] newArray(int size) {
            return new DTOProduct[size];
        }
    };

    @JsonProperty("history_id")
    public String getHistoryId() {
        return historyId;
    }

    @JsonProperty("history_id")
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("company_name")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("company_name")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("product_image")
    public String getProductImage() {
        return productImage;
    }

    @JsonProperty("product_image")
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @JsonProperty("barcode_id")
    public String getBarcodeId() {
        return barcodeId;
    }

    @JsonProperty("barcode_id")
    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    @JsonProperty("ingrediants")
    public String getIngrediants() {
        return ingrediants;
    }

    @JsonProperty("ingrediants")
    public void setIngrediants(String ingrediants) {
        this.ingrediants = ingrediants;
    }

    @JsonProperty("protein_amount")
    public String getProteinAmount() {
        return proteinAmount;
    }

    @JsonProperty("protein_amount")
    public void setProteinAmount(String proteinAmount) {
        this.proteinAmount = proteinAmount;
    }

    @JsonProperty("fat_amount")
    public String getFatAmount() {
        return fatAmount;
    }

    @JsonProperty("fat_amount")
    public void setFatAmount(String fatAmount) {
        this.fatAmount = fatAmount;
    }

    @JsonProperty("calories")
    public String getCalories() {
        return calories;
    }

    @JsonProperty("calories")
    public void setCalories(String calories) {
        this.calories = calories;
    }

    @JsonProperty("is_organic")
    public String getIsOrganic() {
        return isOrganic;
    }

    @JsonProperty("is_organic")
    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    @JsonProperty("is_healthy")
    public String getIsHealthy() {
        return isHealthy;
    }

    @JsonProperty("is_healthy")
    public void setIsHealthy(String isHealthy) {
        this.isHealthy = isHealthy;
    }

    @JsonProperty("vitamin")
    public String getVitamin() {
        return vitamin;
    }

    @JsonProperty("vitamin")
    public void setVitamin(String vitamin) {
        this.vitamin = vitamin;
    }

    @JsonProperty("saturated_fats")
    public String getSaturatedFats() {
        return saturatedFats;
    }

    @JsonProperty("saturated_fats")
    public void setSaturatedFats(String saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    @JsonProperty("protein")
    public String getProtein() {
        return protein;
    }

    @JsonProperty("protein")
    public void setProtein(String protein) {
        this.protein = protein;
    }

    @JsonProperty("sugar")
    public String getSugar() {
        return sugar;
    }

    @JsonProperty("sugar")
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    @JsonProperty("salt")
    public String getSalt() {
        return salt;
    }

    @JsonProperty("salt")
    public void setSalt(String salt) {
        this.salt = salt;
    }

    @JsonProperty("carbohydrate")
    public String getCarbohydrate() {
        return carbohydrate;
    }

    @JsonProperty("carbohydrate")
    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    @JsonProperty("dietary_fiber")
    public String getDietaryFiber() {
        return dietaryFiber;
    }

    @JsonProperty("dietary_fiber")
    public void setDietaryFiber(String dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    @JsonProperty("license_no")
    public String getLicenseNo() {
        return licenseNo;
    }

    @JsonProperty("license_no")
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    @JsonProperty("category_id")
    public String getCategoryId() {
        return categoryId;
    }

    @JsonProperty("category_id")
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("created_date")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("created_date")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("modified_date")
    public String getModifiedDate() {
        return modifiedDate;
    }

    @JsonProperty("modified_date")
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @JsonProperty("is_delete")
    public String getIsDelete() {
        return isDelete;
    }

    @JsonProperty("is_delete")
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @JsonProperty("is_test")
    public String getIsTest() {
        return isTest;
    }

    @JsonProperty("is_test")
    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }

    @JsonProperty("is_favourite")
    public String getIsFavourite() {
        return isFavourite;
    }

    @JsonProperty("is_favourite")
    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    @JsonProperty("history_created_date")
    public String getHistoryCreatedDate() {
        return historyCreatedDate;
    }

    @JsonProperty("history_created_date")
    public void setHistoryCreatedDate(String historyCreatedDate) {
        this.historyCreatedDate = historyCreatedDate;
    }

    @JsonProperty("favourite_id")
    public String getFavouriteId() {
        return favouriteId;
    }

    @JsonProperty("favourite_id")
    public void setFavouriteId(String favouriteId) {
        this.favouriteId = favouriteId;
    }

    @JsonProperty("favourite_created_date")
    public String getFavouriteCreatedDate() {
        return favouriteCreatedDate;
    }

    @JsonProperty("favourite_created_date")
    public void setFavouriteCreatedDate(String favouriteCreatedDate) {
        this.favouriteCreatedDate = favouriteCreatedDate;
    }

}
