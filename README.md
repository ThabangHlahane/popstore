# PopStore

> 🚧 **Status: work in progress.** Features are still being added and things may changge.

A shoe catalogue web app built with Spring Boot, Thymeleaf and MySQL. It lets you view, add, edit and delete products.

## Built with
- Java 17 and Spring Boot
- Spring Data JPA and Hibernate
- MySQL / MariaDB (XAMPP)
- Thymeleaf and Bootstrap 5

## Done so far
- [x] Product list table with images
- [x] Create, edit and delete products
- [x] Click an image to view it larger
- [x] Image upload from the form
- 
## Planned

- [ ] Customer-facing shop page with product cards
- [ ] Search and filter by brand and category
- [ ] Admin login

## How to run
1. Start MySQL in XAMPP and create an empty database called `popstore`.
2. Check the database settings in `src/main/resources/application.properties`.
3. Run `PopstoreApplication`. The `products` table is created automatically.
4. Open http://localhost:8080/products

## Note about images
Product images are not included in this repository. To see pictures, add your own
image files to `src/main/resources/static/products/images/` and type each file name
in the "Image file name" box when creating a product.