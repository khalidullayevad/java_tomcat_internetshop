package kz.springboot.Homework7.controllers;

import kz.springboot.Homework7.entities.*;
import kz.springboot.Homework7.services.ItemService;
import kz.springboot.Homework7.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;



    @GetMapping(value = "/")
    public String index(Model model) {
        List<Items> items = itemService.getTopItems();
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("brands", brands);
        model.addAttribute("shops", items);
        return "index";
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String admin(Model model) {
        List<Items> items = itemService.getAllItems();
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("shops", items);
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getUserData());
        return "items";
    }
    @GetMapping(value = "/admin/orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminOrder(Model model) {
        List<Order> orders = itemService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", getUserData());
        return "order";
    }

    @GetMapping(value = "/admin/users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String users(Model model) {
        List<Users> users = userService.getAllUsers();
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("users", users);
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getUserData());
        return "users";
    }

    @GetMapping(value = "/admin/pictures")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String pictures(Model model) {
       List<Pictures> pictures = itemService.getAllPicturies();
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("pictures", pictures);
        model.addAttribute("currentUser", getUserData());
        return "pictures";
    }
    @GetMapping(value = "/admin/comments")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String comments(Model model) {
       List<Comments> comments = itemService.getAllComments();
        model.addAttribute("comments", comments);
        model.addAttribute("currentUser", getUserData());
        return "comments";
    }

    @GetMapping(value = "/admin/brands")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String brands(Model model) {
        List<Brands> brands = itemService.getAllBrands();
        List<Countries> countries = itemService.getAllCountry();
        model.addAttribute("countries", countries);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getUserData());
        return "brands";
    }

    @GetMapping(value = "/admin/country")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String country(Model model) {
        List<Countries> countries = itemService.getAllCountry();
        model.addAttribute("countries", countries);
        model.addAttribute("currentUser", getUserData());
        return "countries";
    }

    @GetMapping(value = "/admin/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String roles(Model model) {
        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("currentUser", getUserData());
        return "role";
    }

    @GetMapping(value = "/admin/categories")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String categories(Model model) {
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("currentUser", getUserData());
        return "categories";
    }


    @GetMapping(value = "/allItems")
    public String allItmes(Model model) {
        List<Items> items = itemService.getAllItems();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("shops", items);
        model.addAttribute("currentUser", getUserData());
        return "index";
    }

    @GetMapping(value = "/searchDetail")
    public String searchDetail(Model model, @RequestParam(name = "name", defaultValue = "name") String name) {
        List<Items> items = itemService.searchByName(name);
        List<Brands> brands = itemService.getAllBrands();
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("currentUser", getUserData());

        model.addAttribute("brands", brands);
        model.addAttribute("shops", items);
        if (name.equals(name)) {
            name = "";
        }
        model.addAttribute("item_name", name);
        model.addAttribute("brands", brands);
        return "searchDetail";
    }

    @GetMapping(value = "/between")
    public String between(Model model,
                          @RequestParam(name = "brand_id", defaultValue = "0") Long brand_id,
                          @RequestParam(name = "name", defaultValue = "name") String name,
                          @RequestParam(name = "from", defaultValue = "0") Double from,
                          @RequestParam(name = "to", defaultValue = "0") Double to) {
        List<Items> items = itemService.getFilter(name, from, to, brand_id);

        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("shops", items);
        if (name.equals("name")) {
            name = "";
        }
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("brand_id", brand_id);
        model.addAttribute("item_name", name);
        model.addAttribute("currentUser", getUserData());
        return "searchDetail";
    }

    @GetMapping(value = "/searchByBrand/{id}")
    public String searchByBrand(Model model,
                                @PathVariable(name = "id") Long id
    ) {
        List<Items> items = itemService.getFilter("name", 0, 0, id);
        System.out.println("Brand ID: " + id);

        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        model.addAttribute("shops", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brand_id", id);
        model.addAttribute("brands", brands);
        model.addAttribute("item_name", "");
        model.addAttribute("currentUser", getUserData());
        return "searchDetail";
    }

    @GetMapping(value = "/ASC")
    public String Asc(Model model,
                      @RequestParam(name = "name", defaultValue = "name") String name) {
        List<Items> items = itemService.ASC(name);
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("shops", items);
        model.addAttribute("item_name", name);
        model.addAttribute("currentUser", getUserData());
        return "searchDetail";
    }

    @GetMapping(value = "/DESC")
    public String Desc(Model model, @RequestParam(name = "name", defaultValue = "name") String name) {
        List<Items> items = itemService.Desc(name);
        //ArrayList<ShopItem> shops = DBManager.getAllShops();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("shops", items);
        model.addAttribute("item_name", name);
        model.addAttribute("currentUser", getUserData());
        return "searchDetail";
    }

    @PostMapping(value = "/addShopItem")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addTask(@RequestParam(name = "brand_id", defaultValue = "") Long brand_id,
                          @RequestParam(name = "shop_name", defaultValue = "") String name,
                          @RequestParam(name = "shop_description", defaultValue = "") String description,
                          @RequestParam(name = "shop_price", defaultValue = "0") double price,
                          @RequestParam(name = "small_shop_picture", defaultValue = "0") String small_picture,
                          @RequestParam(name = "large_shop_picture", defaultValue = "0") String large_picture,
                          @RequestParam(name = "shop_star", defaultValue = "0") int star,
                          @RequestParam(name = "isTop", defaultValue = "false") Boolean top) {
        Date currentSqlDate = new Date(System.currentTimeMillis());
        Brands brand = itemService.getBrand(brand_id);
        Items item = new Items();
        if (brand != null) {
            item.setBrands(brand);
        }
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setLargePicURL(large_picture);
        item.setSmallPicURL(small_picture);
        item.setInTopPage(top);
        item.setAddedDate(currentSqlDate);
        item.setStars(star);
        itemService.addItem(item);


        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin";

    }

    @PostMapping(value = "/addCategory")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addCategory(@RequestParam(name = "category_name", defaultValue = "") String name,
                              @RequestParam(name = "logoUrl", defaultValue = "") String logoUrl) {

        itemService.addCategories(new Categories(null, name, logoUrl));


        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/categories";

    }
    @PostMapping(value = "/minusAssignPicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String minusAssignPicture(Model model,
                                     @RequestParam(name = "picture_id") Long id,
                                     @RequestParam(name = "item_id") Long item_id) {
        Pictures pic=itemService.getPicture(id);
        Items item=itemService.getItem(item_id);
        if(pic!=null){

            itemService.deletePicture(pic);
        }

        return "redirect:/editDetail/"+item.getId();

    }
    @PostMapping(value = "/addPicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addPicture(@RequestParam(name = "url")MultipartFile file,
                             @RequestParam(name = "item_id") Long item_id) {
        Items item =itemService.getItem(item_id);
        if(item!=null) {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                try {
                    System.out.println("I PICTURE");
                    List<Pictures> pic = itemService.getAllPicturies();
                    int i =pic.size()+1;

                    String picName = DigestUtils.sha1Hex("avatar_" + item.getId() + i+"_!Picture");
                    byte bytes[] = file.getBytes();
                    Path path = Paths.get(uploadPath + picName + ".jpg");
                    Files.write(path, bytes);
                    Pictures picture = new Pictures();
                    picture.setUrl(picName);
                    picture.setItems(item);
                    itemService.savePictures(picture);
                    return "redirect:/editDetail/" + item.getId();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/";
    }
    @PostMapping(value = "/addUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "email", defaultValue = "") String user_email,
                          @RequestParam(name = "password", defaultValue = "") String user_password,
                          @RequestParam(name = "re-password", defaultValue = "") String user_repassword,
                          @RequestParam(name = "full_name", defaultValue = "") String user_fullName) {

        if (user_repassword.equals(user_password)) {
            Users newUser = new Users();
            newUser.setFullName(user_fullName);
            newUser.setPassword(user_password);
            newUser.setEmail(user_email);
            if (userService.addUser(newUser) != null) {
                return "redirect:admin/users";
            }
        }
        return "403";
    }

    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "country_name", defaultValue = "") String name,
                             @RequestParam(name = "country_code", defaultValue = "") String code) {

        itemService.addCountry(new Countries(null, name, code));


        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/country";

    }

    @PostMapping(value = "/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "item_id", defaultValue = "") Long id,
                             @RequestParam(name = "comment", defaultValue = "") String comment) {
        Users user = getUserData();
        Items items = itemService.getItem(id);
        Comments comments = new Comments();
        comments.setComment(comment);
        comments.setAuthor(user);
        comments.setItems(items);
        itemService.addComments(comments);

        return "redirect:/details/"+id;

    }

    @PostMapping(value = "/addRole")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "role_name", defaultValue = "") String name,
                          @RequestParam(name = "role_description", defaultValue = "") String description) {

        userService.addRole(new Roles(null, name, description));


        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/roles";

    }


    @PostMapping(value = "/addBrand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "brand_name", defaultValue = "") String name,
                           @RequestParam(name = "country_id", defaultValue = "") Long country_id) {

        Brands brands = new Brands();
        System.out.println("Brands :   ");
        System.out.println(country_id);
        System.out.println(name);
        brands.setName(name);
        Countries countries = itemService.getCountry(country_id);
        if (countries != null) {
            brands.setCountry(countries);
        }
        itemService.addBrands(brands);
        return "redirect:/admin/brands";

    }


    @GetMapping(value = "/editDetail/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editItemDetail(Model model, @PathVariable(name = "id") Long id) {
        Items item = itemService.getItem(id);
        model.addAttribute("item", item);
        List<Categories> categories = itemService.getAllCategories();
        List<Categories> existCategories = item.getCategories();

        if (existCategories != null) {
            List<Categories> copy = categories;
            for (Categories it : existCategories) {
                for (Categories jt : categories) {
                    if (it.getName().equals(jt.getName())) {
                        System.out.println(it.getName());
                        copy.remove(it);
                        break;
                    }
                }

            }
            categories = copy;
        }
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("currentUser", getUserData());
        List<Pictures> pictures = itemService.getAllItemPicturies(item);

        model.addAttribute("pictures", pictures);
        model.addAttribute("brands", brands);
        return "itemEdit";
    }

    @GetMapping(value = "/editUser/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUsers(Model model, @PathVariable(name = "id") Long id) {
        Users user = userService.getUser(id);

        model.addAttribute("user", user);
        List<Roles> roles = userService.getAllRoles();
        List<Roles> existRoles = user.getRoles();

        if (existRoles != null) {
            List<Roles> copy = roles;
            for (Roles it : existRoles) {
                for (Roles jt : roles) {
                    if (it.getName().equals(jt.getName())) {
                        System.out.println(it.getName());
                        copy.remove(it);
                        break;
                    }
                }

            }
            roles = copy;
        }
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("roles", roles);
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("brands", brands);
        return "editUser";
    }


    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id) {
        Items item = itemService.getItem(id);
        List<Comments>  comments= itemService.getByItem(id);
        List<Pictures> pictures = itemService.getAllItemPicturies(item);
        model.addAttribute("item", item);
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("pictures", pictures);
        model.addAttribute("comments", comments);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getUserData());
        return "itemDetail";
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping(value = "/editShopItem")
    public String editTask(@RequestParam(name = "brand_id", defaultValue = "") Long brand_id,
                           @RequestParam(name = "id", defaultValue = "0") Long id,
                           @RequestParam(name = "shop_name", defaultValue = "") String name,
                           @RequestParam(name = "shop_description", defaultValue = "") String description,
                           @RequestParam(name = "shop_price", defaultValue = "0") double price,
                           @RequestParam(name = "small_shop_picture", defaultValue = "0") String small_picture,
                           @RequestParam(name = "large_shop_picture", defaultValue = "0") String large_picture,
                           @RequestParam(name = "shop_star", defaultValue = "0") int star,
                           @RequestParam(name = "isTop", defaultValue = "false") Boolean top) {

        Items item = itemService.getItem(id);
        if (item != null) {
            Brands brand = itemService.getBrand(brand_id);
            if (brand != null) {
                item.setBrands(brand);
            }
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setLargePicURL(large_picture);
            item.setSmallPicURL(small_picture);
            item.setInTopPage(top);
            item.setStars(star);
            itemService.saveItems(item);
        }

        return "redirect:/editDetail/" + item.getId();

    }

    @PostMapping(value = "/editBrand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editBrand(@RequestParam(name = "brand_id", defaultValue = "") Long brand_id,
                            @RequestParam(name = "brand_name", defaultValue = "") String name,
                            @RequestParam(name = "country_id", defaultValue = "") Long country_id) {

        Brands brands = itemService.getBrand(brand_id);
        if (brands != null) {
            brands.setName(name);
            Countries countries = itemService.getCountry(country_id);
            if (countries != null) {
                brands.setCountry(countries);
            }
            itemService.saveBrands(brands);
        }
        return "redirect:/admin/brands";

    }

    @PostMapping(value = "/editCategory")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editCategory(@RequestParam(name = "category_id", defaultValue = "") Long category_id,
                               @RequestParam(name = "category_name", defaultValue = "") String name,
                               @RequestParam(name = "logoUrl", defaultValue = "") String logoUrl) {
        Categories categories = itemService.getCategory(category_id);

        if (categories != null) {
            categories.setName(name);
            categories.setLogoURL(logoUrl);
        }

        itemService.saveCategories(categories);
        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/categories";

    }
    @PostMapping(value = "/editComment")

    public String editComment(@RequestParam(name = "comment_id", defaultValue = "") Long comment_id,
                              @RequestParam(name = "user_id", defaultValue = "") Long user_id,
                              @RequestParam(name = "item_id", defaultValue = "") Long item_id,
                              @RequestParam(name = "comment", defaultValue = "") String comment) {
        Comments comments = itemService.getComments(comment_id);


        if (comments != null) {
            comments.setComment(comment);
            comments.setItems(itemService.getItem(item_id));
            comments.setAuthor(getUserData());
        }

        itemService.saveComments(comments);
        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/details/"+item_id;

    }

    @PostMapping(value = "/editCountry")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editCountry(@RequestParam(name = "country_id", defaultValue = "") Long country_id,
                              @RequestParam(name = "country_name", defaultValue = "") String name,
                              @RequestParam(name = "country_code", defaultValue = "") String code) {
        Countries countries = itemService.getCountry(country_id);
        System.out.println(country_id);
        System.out.println(name);
        System.out.println(code);
        if (countries != null) {
            countries.setName(name);
            countries.setCode(code);
        }

        itemService.saveCountry(countries);
        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/country";

    }

    @PostMapping(value = "/editRole")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editRole(@RequestParam(name = "role_id", defaultValue = "") Long role_id,
                           @RequestParam(name = "role_name", defaultValue = "") String name,
                           @RequestParam(name = "role_description", defaultValue = "") String description) {
        Roles role = userService.getRole(role_id);

        if (role != null) {
            role.setName(name);
            role.setDescription(description);
        }
        userService.saveRole(role);
        // itemService.addItem(new Items(null, name,description,price, star,small_picture, large_picture, currentSqlDate, top));
        return "redirect:/admin/roles";

    }

    @PostMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String delete(@PathVariable(name = "id") Long id) {
        {
            Items item = itemService.getItem(id);
            if (item != null) {
                itemService.deleteItem(item);
            }
            return "redirect:/admin";

        }
    }

    @PostMapping(value = "/deleteUser/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        {
            Users user = userService.getUser(id);
            if (user != null) {
                userService.deleteUser(user);
            }
            return "redirect:/admin/users";

        }
    }
    @PostMapping(value = "/deleteComment/{id}")
    public String deleteComment(@PathVariable(name = "id") Long id,
                                @RequestParam(name = "item_id", defaultValue = "") Long item_id) {
        {
            Comments comments= itemService.getComments(id);
            System.out.println(id);
            if (comments != null) {
                itemService.deleteComments(comments);
            }
            return "redirect:/details/"+ item_id;

        }
    }

    @PostMapping(value = "/deleteCommentAdmin/{id}")
    public String deleteCommentAdmin(@PathVariable(name = "id") Long id) {
        {
            Comments comments= itemService.getComments(id);
            System.out.println(id);
            if (comments != null) {
                itemService.deleteComments(comments);
            }
            return "redirect:/admin/comments";

        }
    }

    @PostMapping(value = "/deleteCountry/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCountry(@PathVariable(name = "id") Long id) {
        {
            Countries countries = itemService.getCountry(id);
            System.out.println(id);
            if (countries != null) {
                itemService.deleteCountry(countries);
            }
            return "redirect:/admin/country";

        }
    }

    @PostMapping(value = "/deleteRole/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteRole(@PathVariable(name = "id") Long id) {
        {
            Roles role = userService.getRole(id);
            System.out.println(id);
            if (role != null) {
                userService.deleteRole(role);
            }
            return "redirect:/admin/roles";

        }
    }

    @PostMapping(value = "/deleteCategory/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCategory(@PathVariable(name = "id") Long id) {
        {
            Categories categories = itemService.getCategory(id);

            if (categories != null) {
                itemService.deleteCategories(categories);
            }
            return "redirect:/admin/categories";

        }
    }

    @PostMapping(value = "/deleteBrand/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteBrand(@PathVariable(name = "id") Long id) {
        {
            Brands brands = itemService.getBrand(id);
            System.out.println(id);
            if (brands != null) {
                itemService.deleteBrand(brands);
            }
            return "redirect:/admin/brands";

        }
    }

    @PostMapping(value = "/assign")
    public String assignCategory(@RequestParam(name = "item_id", defaultValue = "0") Long itemId,
                                 @RequestParam(name = "category_id") Long categoryId) {
        Categories cat = itemService.getCategory(categoryId);
        if (cat != null) {
            Items items = itemService.getItem(itemId);
            if (items != null) {
                List<Categories> categories = items.getCategories();
                if (categories == null) {
                    categories = new ArrayList<>();
                }
                categories.add(cat);

                itemService.saveItems(items);
                return "redirect:/editDetail/" + items.getId();
            }

        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/roling")
    public String roling(@RequestParam(name = "user_id", defaultValue = "0") Long userId,
                         @RequestParam(name = "role_id") Long roleId) {
        Roles cat = userService.getRole(roleId);
        if (cat != null) {
            Users user = userService.getUser(userId);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                if (roles == null) {
                    roles = new ArrayList<>();
                }
                roles.add(cat);
                user.setRoles(roles);
                userService.saveUserRole(user);
                return "redirect:/editUser/" + user.getId();
            }

        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/disassign")
    public String disassignCategory(@RequestParam(name = "item_id", defaultValue = "0") Long itemId,
                                    @RequestParam(name = "category_id") Long categoryId) {
        Categories cat = itemService.getCategory(categoryId);
        if (cat != null) {
            Items items = itemService.getItem(itemId);
            if (items != null) {
                List<Categories> categories = items.getCategories();
                if (categories != null) {
                    List<Categories> copy = categories;
                    for (Categories it : categories) {
                        if (it.getName().equals(cat.getName())) {
                            copy.remove(it);
                            break;
                        }
                    }
                    categories = copy;
                }
                items.setCategories(categories);
                itemService.saveItems(items);
                return "redirect:/editDetail/" + items.getId();
            }

        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/diroling")
    public String diroling(@RequestParam(name = "user_id", defaultValue = "0") Long userId,
                           @RequestParam(name = "role_id") Long roleId) {
        Roles cat = userService.getRole(roleId);
        if (cat != null) {
            Users user = userService.getUser(userId);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                if (roles != null) {
                    List<Roles> copy = roles;
                    for (Roles it : roles) {
                        if (it.getName().equals(cat.getName())) {
                            copy.remove(it);
                            break;
                        }
                    }
                    roles = copy;
                }
                user.setRoles(roles);
                userService.saveUserRole(user);
                return "redirect:/editUser/" + user.getId();
            }

        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("currentUser", getUserData());
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        model.addAttribute("currentUser", getUserData());
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "profile";
    }

    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }


    @GetMapping(value = "/registration")
    @PreAuthorize("isAnonymous()")
    public String reistration(Model model) {
        model.addAttribute("currentUser", getUserData());
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "reistration";
    }

    @PostMapping(value = "/createAccount")
    @PreAuthorize("isAnonymous()")
    public String createAccount(@RequestParam(name = "user_email", defaultValue = "") String user_email,
                                @RequestParam(name = "user_password", defaultValue = "") String user_password,
                                @RequestParam(name = "user_re-password", defaultValue = "") String user_repassword,
                                @RequestParam(name = "user_full-name", defaultValue = "") String user_fullName) {
        System.out.println(user_password);
        System.out.println(user_repassword);
        if (user_repassword.equals(user_password)) {
            System.out.println("HI");
            Users newUser = new Users();
            newUser.setFullName(user_fullName);
            newUser.setPassword(user_password);
            newUser.setEmail(user_email);
            if (userService.addUser(newUser) != null) {
                return "redirect:/registration?success";
            }
        }

        return "redirect:/registration?error";

    }

    @PostMapping(value = "/editUserDetail")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editDetailUser(@RequestParam(name = "id", defaultValue = "") Long id,
                                 @RequestParam(name = "password", defaultValue = "") String user_password,
                                 @RequestParam(name = "full_name", defaultValue = "") String user_fullName) {


        Users newUser = userService.getUser(id);
        newUser.setFullName(user_fullName);
        newUser.setPassword(user_password);
        if (userService.saveUser(newUser) != null) {
            return "redirect:/admin/users?success";
        }


        return "redirect:/admin/users?error";

    }


    @PostMapping(value = "/editUserFullName")
    @PreAuthorize("isAuthenticated()")
    public String editFullName(@RequestParam(name = "id", defaultValue = "") Long id,
                               @RequestParam(name = "full_name", defaultValue = "") String user_fullName) {


        Users newUser = userService.getUser(id);
        newUser.setFullName(user_fullName);
        if (userService.saveUserRole(newUser) != null) {
            return "redirect:/profile/?success";
        }


        return "redirect:/profile/users?error";

    }


    @PostMapping(value = "/editUserPassword")
    @PreAuthorize("isAuthenticated()")
    public String editUserPassword(@RequestParam(name = "id", defaultValue = "") Long id,
                                   @RequestParam(name = "old-password", defaultValue = "") String oldpass,
                                   @RequestParam(name = "new-password", defaultValue = "") String newpass,
                                   @RequestParam(name = "re-password", defaultValue = "") String repass) {
        if (repass.equals(newpass)) {

            Users newUser = userService.getUser(id);
            if (userService.saveUserNewPass(newUser, oldpass, newpass) != null) {

                return "redirect:/profile?success";
            }
        }


        return "redirect:/profile?error";

    }

    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava")MultipartFile file){
        if(file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/png")) {
            try {

                Users currentUser =getUserData();
                String picName= DigestUtils.sha1Hex("avatar_"+currentUser.getId()+"_!Picture");
                byte bytes[] = file.getBytes();
                Path path = Paths.get(uploadPath + picName+".jpg");
                Files.write(path, bytes);
                currentUser.setPictureURL(picName);
                userService.saveUser(currentUser);
                return "redirect:/profile?success";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewPhoto/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url")String url )throws IOException {
        String pictureUrl =viewPath+defaultPicture;
        if(url!=null){
            pictureUrl=viewPath+url+".jpg";
        }
        InputStream in;
        try{
            ClassPathResource resource =new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource =new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "/viewPicture/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewPic(@PathVariable(name = "url")String url )throws IOException {
        String pictureUrl =viewPath+defaultPicture;
        if(url!=null){
            pictureUrl=viewPath+url+".jpg";
        }
        InputStream in;
        try{
            ClassPathResource resource =new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource =new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }


    @PostMapping(value = "/checkIn")
    public String checkIn(HttpSession session,
                          @RequestParam(name = "full_name") String full_name){
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        long millis=System.currentTimeMillis();
        java.sql.Date date_added = new java.sql.Date(millis);

        for(Basket b:baskets){
            Order order = new Order();
            order.setDateAdded(date_added);
            order.setFullName(full_name);
            order.setItems(b.getItems());
            order.setQuantity(b.getQuantity());
            itemService.addOrder(order);

            session.removeAttribute("basketItems");
            return "redirect:/basket";
        }
        return "redirect:/basket";
    }
    @PostMapping(value = "/addBasket")
    public String addBasket(@RequestParam(name = "id", defaultValue = "0") Long id,
                           HttpSession session){
        System.out.println("Hello world!");
        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");
        if (items == null) {
            items = new ArrayList<>();
        }

        Basket basket = new Basket();
        Items items1 = itemService.getItem(id);
        basket.setItems(items1);
        System.out.println(basket);
        if (items.size() > 0) {
            for (Basket b : items) {
                if (b.compareTo(basket) > 0) {
                    int existQuantity = b.getQuantity();
                    b.setQuantity(existQuantity + 1);
                    session.setAttribute("basketItems", items);
                    System.out.println(items);
                    return "redirect:/basket";
                }
            }
        }

        basket.setQuantity(basket.getQuantity() + 1);
        items.add(basket);
        session.setAttribute("basketItems", items);
        System.out.println(items);
        return "redirect:/basket";


    }


    @PostMapping(value = "/removeQuantity")
    public String removeQuantity(HttpSession session,
                                 @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");

        Items item = itemService.getItem(id);
        Basket basket = new Basket();
        basket.setItems(item);

        for(Basket b: items){
            if(b.compareTo(basket)>0){
                if (b.getQuantity() <= 1){
                    items.remove(b);
                    session.setAttribute("basketItems", items);
                    return "redirect:/basket";

                }

                int myQuantity = b.getQuantity();
                b.setQuantity(myQuantity - 1);
                session.setAttribute("basketItems", items);
                return "redirect:/basket";

            }
        }


        return "redirect:/basket";
    }



    @PostMapping(value = "/clearBasket")
    public String clearBasket(HttpSession session){
        session.removeAttribute("basketItems");

        return "redirect:/basket";
    }
    @PostMapping(value = "/addQuantity")
    public String addQuantity(HttpSession session,
                              @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");

        Items item = itemService.getItem(id);
        Basket basket = new Basket();
        basket.setItems(item);

        for(Basket b: items){
            if(b.compareTo(basket)>0){
                int myQuantity = b.getQuantity();
                b.setQuantity(myQuantity + 1);
                session.setAttribute("basketItems", items);
                return "redirect:/basket";
            }
        }


        return "redirect:/basket";
    }

    @GetMapping(value = "/basket")
    public String basket(Model model, HttpSession session){
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        model.addAttribute("currentUser", getUserData());
        int total = 0;
        if(baskets == null){
            baskets = new ArrayList<>();
        }
        for(Basket b:baskets){
            total += (b.getQuantity() * b.getItems().getPrice());
        }

        model.addAttribute("basket", baskets);
        model.addAttribute("total", total);
        if(baskets==null){
            model.addAttribute("size",0);
        }
    else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }

        return "basket";
    }

}