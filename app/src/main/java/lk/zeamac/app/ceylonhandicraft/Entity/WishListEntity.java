package lk.zeamac.app.ceylonhandicraft.Entity;

public class WishListEntity {

    private String title;
    private String price;
    private String imagePath;


    public WishListEntity(String imagePath,String title, String price) {
        this.title = title;
        this.price = price;
        this.imagePath= imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
