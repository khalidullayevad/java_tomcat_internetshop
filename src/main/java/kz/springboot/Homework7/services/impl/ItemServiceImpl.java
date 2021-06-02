package kz.springboot.Homework7.services.impl;

import kz.springboot.Homework7.entities.*;
import kz.springboot.Homework7.repositories.*;
import kz.springboot.Homework7.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private  OrderRepository orderRepository;


    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }
    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Countries country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoriesRepository.getOne(id);
    }

    @Override
    public Categories addCategories(Categories categories) {
        return categoriesRepository.save(categories) ;
    }

    @Override
    public Categories saveCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    @Override
    public void deleteCategories(Categories categories) {
        categoriesRepository.delete(categories);
    }

    @Override
    public List<Pictures> getAllPicturies() {
        return pictureRepository.findAll();
    }

    @Override
    public Pictures getPicture(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public Pictures addPicture(Pictures pictures) {
        return pictureRepository.save(pictures);
    }

    @Override
    public Pictures savePictures(Pictures pictures) {
        return pictureRepository.save(pictures);
    }

    @Override
    public void deletePicture(Pictures pictures) {
        pictureRepository.delete(pictures);
    }

    @Override
    public List<Pictures> getAllItemPicturies(Items item) {
        return pictureRepository.findAllByItems(item);
    }

    @Override
    public List<Comments> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comments getComments(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public Comments addComments(Comments comments) {
        return commentRepository.save(comments);
    }

    @Override
    public Comments saveComments(Comments comments) {
        return commentRepository.save(comments);
    }

    @Override
    public void deleteComments(Comments comments) {
        commentRepository.delete(comments);
    }

    @Override
    public List<Comments> getByItem(Long id) {
        return commentRepository.findAllByItemsIdOrderByAddedDateDesc(id);
    }

    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }

    @Override
    public Items saveItems(Items item) {
        return  itemRepository.save(item);
    }

    @Override
    public List<Items> getTopItems() {
        return itemRepository.findAllByInTopPage(true);
    }

    @Override
    public List<Items> searchByName(String name) {
        name="%"+name+"%";
        return itemRepository.findAllByNameLike(name);
    }

    @Override
    public List<Items> ASC(String name) {
        name="%"+name+"%";
        return itemRepository.findAllByNameLikeOrderByPriceAsc(name);
    }

    @Override
    public List<Items> Desc(String name) {
        name="%"+name+"%";
        return itemRepository.findAllByNameLikeOrderByPriceDesc(name);
    }

    @Override
    public List<Items> getFilter(String name, double pr1, double pr2, Long brand) {
        if(name.equals("name") &&  brand==0 && pr1 ==0 && pr2 ==0){
            return itemRepository.findAll();
        }
        else if(!name.equals("name") &&  brand!=0 && pr1 !=0 && pr2 !=0){
            return itemRepository.findAllByNameLikeAndPriceBetweenAndBrandsId(name,pr1,pr2,brand);
        }
        else if(!name.equals("name") &&  brand==0 && pr1 ==0 && pr2 ==0){
            name="%"+name+"%";
            return itemRepository.findAllByNameLike(name);
        }
        else if(!name.equals("name") &&  brand!=0&& pr1 ==0 && pr2 ==0){
            name="%"+name+"%";
            return itemRepository. findAllByNameLikeAndBrandsId(name, brand);
        }
        else if(name.equals("name") &&  !brand.equals("brand") && pr1 ==0 && pr2 ==0){

            return itemRepository. findAllByBrandsId( brand);
        }
        else if(!name.equals("name") &&  brand!=0 && pr1 !=0 && pr2 !=0){
            name="%"+name+"%";
            return itemRepository.findAllByNameLikeAndPriceBetween(name, pr1,pr2);
        }
        else if(name.equals("name") &&  brand!=0 && pr1 !=0 && pr2 !=0){

            return itemRepository.findAllByBrandsIdAndPriceBetween(brand, pr1,pr2);
        }
        else if(name.equals("name") &&  brand==0 && pr1 !=0 && pr2 !=0){
            return itemRepository.findAllByPriceBetween( pr1,pr2);
        }
        else if(name.equals("name") &&  brand==0  && pr1 !=0 && pr2 ==0){
            return itemRepository.findAllByPriceAfter( pr1);
        }
        else if(name.equals("name") && brand==0  && pr1 ==0 && pr2 !=0){
            return itemRepository.findAllByPriceBefore( pr2);
        }
        else if(name.equals("name") && brand!=0  && pr1 !=0 && pr2 ==0){
            return itemRepository.findAllByPriceAfterAndBrandsId( pr1, brand);
        }
        else  if(name.equals("name") && brand!=0  && pr1 ==0 && pr2 !=0){
            return itemRepository.findAllByPriceBeforeAndBrandsId( pr2, brand);
        }
        return null;
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brands getBrand(Long id) {
        return brandRepository.getOne(id);    }

    @Override
    public Brands addBrands(Brands brands) {
        return brandRepository.save(brands);
    }

    @Override
    public Brands saveBrands(Brands brands) {
        return brandRepository.save(brands);
    }

    @Override
    public void deleteBrand(Brands brands) {
        brandRepository.delete(brands);
    }

    @Override
    public List<Countries> getAllCountry() {
        return countryRepository.findAll();    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }


}
