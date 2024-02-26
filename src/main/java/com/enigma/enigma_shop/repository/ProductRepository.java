package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Query Method
    // find by name -> tidak boleh sembarang buat nama
    // Optional<T> findBy... -> List/Satuan
    // Stream<T>/List<T> findAllBy... -> List
    // List<Product> findAllByNameAndPrice(String name, Long price);
    List<Product> findAllByNameLike(String name);

    // simulasi filter berdasarkan name, minPrice, maxPrice, dan stock menggunakan operasi or
    // jika ga ketemu maka tampilkan semua product
}
