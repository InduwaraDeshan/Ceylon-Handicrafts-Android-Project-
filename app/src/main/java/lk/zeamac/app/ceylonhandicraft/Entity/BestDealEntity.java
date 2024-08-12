package lk.zeamac.app.ceylonhandicraft.Entity;

public class BestDealEntity {
    private String imgPath ;
    private String title;
    private String description;
    private String price;
    private int category;

    public BestDealEntity(String imgPath, String title, String description, String price,int category) {
        this.imgPath = imgPath;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category= category;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
