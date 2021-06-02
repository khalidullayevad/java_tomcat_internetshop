package kz.springboot.Homework7.repositories;

import kz.springboot.Homework7.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items,Long> {
    List<Items> findAllByInTopPage(boolean bt);
    List<Items> findAllByNameLike(String name);
    List<Items> findAllByNameLikeAndBrandsId(String name, Long brand);
    List<Items> findAllByBrandsId(Long id);
    List<Items> findAllByNameLikeAndPriceBetween(String name,double pr, double pr2);
    List<Items> findAllByBrandsIdAndPriceBetween(Long Id,double pr, double pr2);
    List<Items> findAllByPriceAfterAndBrandsId(double pr, Long Id);
    List<Items> findAllByPriceBeforeAndBrandsId(double pr, Long Id);
    List<Items>findAllByNameLikeOrderByPriceAsc(String name);
    List<Items>findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> findAllByPriceBetween(double pr, double pr2);
    List<Items> findAllByPriceAfter(double pr);
    List<Items> findAllByPriceBefore(double pr);
    List<Items>  findAllByNameLikeAndPriceBetweenAndBrandsId(String name,double pr, double pr2, Long id );

}
