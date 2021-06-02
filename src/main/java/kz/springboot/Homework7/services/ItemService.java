package kz.springboot.Homework7.services;

import kz.springboot.Homework7.entities.*;

import java.util.List;

public interface ItemService {
    Items  addItem(Items item);
    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItems(Items item);
    List<Items> getTopItems();
    List<Items> searchByName(String name);
    List<Items> ASC(String name);
    List<Items> Desc(String name);
    List<Items> getFilter(String name, double pr1, double pr2, Long brand);
    List<Brands> getAllBrands();
    Brands getBrand(Long id);
    Brands addBrands(Brands brands);
    Brands saveBrands(Brands brands);
    void deleteBrand(Brands brands);
    List<Countries> getAllCountry();
    Countries getCountry(Long id);
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    void deleteCountry(Countries country);

    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    Categories addCategories(Categories categories);
    Categories saveCategories(Categories categories);
    void deleteCategories(Categories categories );

    List<Pictures> getAllPicturies();
    Pictures getPicture(Long id);
    Pictures addPicture(Pictures pictures);
    Pictures savePictures(Pictures pictures);
    void deletePicture(Pictures pictures);
    List<Pictures> getAllItemPicturies(Items items);


    List<Comments> getAllComments();
    Comments getComments(Long id);
    Comments addComments(Comments comments);
    Comments saveComments(Comments comments);
    void deleteComments(Comments comments );
    List<Comments> getByItem(Long id);

    Order addOrder(Order order);
    List<Order> getAllOrders();


}
