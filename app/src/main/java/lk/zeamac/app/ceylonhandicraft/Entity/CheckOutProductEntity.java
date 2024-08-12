package lk.zeamac.app.ceylonhandicraft.Entity;

public class CheckOutProductEntity {
    private String imgPath;
    private String title;
    private String price;
    private String qty;

    public CheckOutProductEntity(String imgPath, String title, String price, String qty) {
        this.imgPath = imgPath;
        this.title = title;
        this.price = price;
        this.qty = qty;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
