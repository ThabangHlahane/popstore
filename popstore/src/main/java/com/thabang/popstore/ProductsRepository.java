package com.thabang.popstore;

import com.thabang.popstore.models.products;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductsRepository extends JpaRepository<products,Integer> {
}
