package com.thabang.popstore;

import com.thabang.popstore.models.ProductDto;
import com.thabang.popstore.models.products;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

// This class handles every web address that starts with /products
@Controller
@RequestMapping("/products")
public class ProductsController {

    // The repository is how we talk to the database
    private final ProductsRepository repo;

    // The folder where uploaded images are saved (set in application.properties)
    @Value("${app.upload-dir}")
    private String uploadDir;

    public ProductsController(ProductsRepository repo) {
        this.repo = repo;
    }

    // ---------- LIST ----------
    @GetMapping({"", "/"})
    public String list(Model model) {
        model.addAttribute("products", repo.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return "products/index";
    }

    // ---------- CREATE ----------
    // Shows the empty form
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return showForm(model, "New Shoe", "/products/create");
    }

    // Runs when you click Save on the New Shoe form
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("productDto") ProductDto dto,
                         BindingResult result, Model model) throws IOException {

        // A new shoe must have a picture
        if (dto.getImageFile() == null || dto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "An image is required"));
        } else if (!isImage(dto.getImageFile())) {
            result.addError(new FieldError("productDto", "imageFile", "The file must be an image"));
        }

        // If anything is wrong, show the form again with the error messages
        if (result.hasErrors()) {
            return showForm(model, "New Shoe", "/products/create");
        }

        // Copy the form values into a new product and save it
        products product = new products();
        copyFields(dto, product);
        product.setImageFileName(saveImage(dto.getImageFile()));
        product.setCreatedAt(new Date());
        repo.save(product);
        return "redirect:/products";
    }

    // ---------- EDIT ----------
    // Shows the form filled in with the shoe's details
    @GetMapping("/edit")
    public String editForm(@RequestParam int id, Model model) {
        products product = repo.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }
        // Fill the form object from the database values
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        model.addAttribute("productDto", dto);
        model.addAttribute("currentImage", product.getImageFileName());
        return showForm(model, "Edit Shoe", "/products/edit?id=" + id);
    }

    // Runs when you click Save on the Edit Shoe form
    @PostMapping("/edit")
    public String edit(
            @RequestParam int id,
            @Valid @ModelAttribute("productDto") ProductDto dto,
            BindingResult result,
            Model model) {

        products product = repo.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        if (result.hasErrors()) {
            model.addAttribute("currentImage", product.getImageFileName());
            return "products/form";
        }

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        repo.save(product);

        return "redirect:/products";
    }

    // ---------- DELETE ----------
    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        products product = repo.findById(id).orElse(null);
        if (product != null) {
            deleteImage(product.getImageFileName());  // remove the picture file too
            repo.deleteById(id);
        }
        return "redirect:/products";
    }

    // ---------- HELPERS ----------

    // Sends the title and the form's target address to the page, then shows form.html
    private String showForm(Model model, String title, String action) {
        model.addAttribute("title", title);
        model.addAttribute("action", action);
        return "products/form";
    }

    // Copies the text fields from the form object into the database object
    private void copyFields(ProductDto dto, products product) {
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
    }

    // True if the uploaded file is a picture
    private boolean isImage(MultipartFile file) {
        String type = file.getContentType();
        return type != null && type.startsWith("image/");
    }

    // Saves the picture in the upload folder and returns the new file name
    private String saveImage(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));            // make the folder if it doesn't exist
        String original = file.getOriginalFilename();
        String extension = ".jpg";
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf('.')).toLowerCase();
        }
        // A random name stops two uploads with the same name from overwriting each other
        String fileName = UUID.randomUUID() + extension;
        Files.copy(file.getInputStream(), Paths.get(uploadDir).resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    // Deletes an uploaded picture. Old pictures in the static folder are left alone.
    private void deleteImage(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(Paths.get(uploadDir).resolve(fileName));
        } catch (IOException e) {
            // If the file can't be deleted, carry on
        }


    }

}