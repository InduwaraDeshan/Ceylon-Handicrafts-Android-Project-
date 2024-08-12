package lk.zeamac.app.ceylonhandicraft.Entity;

public class AllCategoryEntity {
    private String imgPath ;
    private String title;
    private String description;
    private String price;


    public AllCategoryEntity(String imgPath, String title, String description, String price) {
        this.imgPath = imgPath;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
