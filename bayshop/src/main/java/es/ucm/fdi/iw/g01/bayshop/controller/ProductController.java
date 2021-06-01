package es.ucm.fdi.iw.g01.bayshop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.g01.bayshop.LocalData;
import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.User;
import es.ucm.fdi.iw.g01.bayshop.model.Product.ProductStatus;
import es.ucm.fdi.iw.g01.bayshop.model.User.Role;

@Controller
@RequestMapping("producto")
public class ProductController {
    private static Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;


    @Transactional
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product newProduct, Model model, HttpSession session, @RequestParam("photos") MultipartFile photos){
        
        User requester = (User)session.getAttribute("u");

        newProduct.setEnabled(true);
        newProduct.setStatus(ProductStatus.PENDING);
        newProduct.setUser(requester);
        newProduct.setCreationDate(LocalDateTime.now());

        entityManager.persist(newProduct);
        entityManager.flush();

        String path = "product/" + newProduct.getId();
		File img = localData.getFile(path, Long.toString(newProduct.getId()));

		if(photos.isEmpty()){
			logger.warn("REGISTER IMAGE EMPTY");
		} else{
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))){
				byte[] bytes = photos.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				logger.warn("ERROR UPLOADING REGISTER IMAGE");
			}
		}

        return "redirect:/index";
    }

    @GetMapping(value = { "/crear", "/crear/" })
    public String productCreate(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear producto");
        return "productoCrear";
    }


    @Transactional
    @GetMapping("/{id}")
    public String product_id(HttpSession session, @PathVariable long id, Model model, @RequestParam(required = false) Integer entero) {
        
        User requester = (User)session.getAttribute("u");
        Product p = entityManager.find(Product.class, id);
        List<Product>prod = null;
        String query= null;

        // Mostar sugerencias de Otros Productos u Otros Productos del vendedor en función del ROL
        if (requester.hasRole(Role.USER)) {
            query = "select p from Product p where status = 0 and id != :idP";
            prod = entityManager.createQuery(query).setParameter("idP", id).getResultList();  
        } else {
            query = "select p from Product p where user_id = :seller and id != :idP";
            prod = entityManager.createQuery(query).setParameter("seller", p.getUser()).setParameter("idP", id).getResultList();
        }

        model.addAttribute("p", p);
        model.addAttribute("prod", prod);
        model.addAttribute("title", "BayShop | Producto " + p.getId());
        model.addAttribute("canBuy", p.getStatus() == ProductStatus.ACCEPTED);

        return "producto";
    }

    
    @Transactional
    @GetMapping("/delete/{id}")
    public String rechazar(HttpSession session, Model model, @PathVariable long id, @RequestParam(required = false) Integer entero) {
        
        User requester = (User)session.getAttribute("u");
        Product p = entityManager.find(Product.class, id);

        if(requester.hasRole(Role.ADMIN) || requester.hasRole(Role.MODERATOR) || requester.getId() == p.getUser().getId()){
            p.setEnabled(false);
        }
    
        return "redirect:/";
    }


    // GET porduct photo
	@GetMapping("/api/photo/{id}")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException{
		
        String path = "product/" + id;

        File file = localData.getFile(path, Long.toString(id));
		InputStream in;

		if (file.exists()) {
			in = new BufferedInputStream(new FileInputStream(file));
		} else {
			in = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("static/img/default-camiseta.png"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}


}
