package kz.springboot.Homework7.db;

import java.util.ArrayList;



public class DBManager {
    private static ArrayList<ShopItem> shops = new ArrayList<>();
    static {
        shops.add(new ShopItem(1L, "Iphone 12 pro max", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 24850, 10, 4, "https://s.appleinsider.ru/2020/08/iphone12_specs-750x384.jpg"));
        shops.add(new ShopItem(2L, "Meizu 16 pro", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 850, 10, 3, "https://mobile-review.com/articles/2019/image/press-meizu-16s-pro/off/3.jpg"));
        shops.add(new ShopItem(3L, "Samsung Galaxy 20", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 850, 10, 3, "https://www.technodom.kz/media/catalog/product/cache/1366e688ed42c99cd6439df4031862c6/2/c/2c46b0d1891692e9ff3e3104aac71388a83b1a0a_215396_2as.jpg"));
        shops.add(new ShopItem(4L, "Nokia 9 pureview", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 850, 10, 3, "https://mobiltelefon.ru/photo/december18/31/nokia_9_render_01.jpg"));
        shops.add(new ShopItem(5L, "Xiaomi redmi note 9", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 850, 10, 3, "https://xiaomi-store.kz/images/stories/virtuemart/product/Xiaomi_Redmi_Note_9_Pro.jpg"));
        shops.add(new ShopItem(6L, "Motorola razr 5g", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 850, 10, 3, "https://cdn57.androidauthority.net/wp-content/uploads/2020/09/Blush-Gold_Combo.jpg"));
    }
    private static Long id = 5L;
    public static  ArrayList<ShopItem> getAllShops(){
        return shops;
    }
    public static void addShop(ShopItem shopItem){
        shopItem.setId(id);
        shops.add(shopItem);
        id++;
    }
}
