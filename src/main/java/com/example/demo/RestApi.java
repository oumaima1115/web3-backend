package com.example.demo;


import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {

    Model model = JenaEngine.readModel("data/inclusify.owl");





    @GetMapping("/chaud")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherAliment() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_chaud.txt");
//            OutputStream res2 =  JenaEngine.executeQueryFile(inferedModel, "data/query_OrigineVegetale.txt");
//            OutputStream res3 =  JenaEngine.executeQueryFile(inferedModel, "data/query_Liquide.txt");

//            String res = res1.toString() + res2.toString() + res3.toString() ;

            System.out.println(res);
            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }
    }
    @GetMapping("/froid")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherFroid() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_froid.txt");
//            OutputStream res2 =  JenaEngine.executeQueryFile(inferedModel, "data/query_OrigineVegetale.txt");
//            OutputStream res3 =  JenaEngine.executeQueryFile(inferedModel, "data/query_Liquide.txt");

//            String res = res1.toString() + res2.toString() + res3.toString() ;

            System.out.println(res);
            return res.toString();

        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/national")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherMarqueInternational() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_national.txt");
            System.out.println(res);


            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }
    }


    @GetMapping("/international")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherInternational() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_international.txt");
            System.out.println(res);


            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/post")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherComment() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_Post.txt");
//            OutputStream res2 =  JenaEngine.executeQueryFile(inferedModel, "data/query_OrigineVegetale.txt");
//            OutputStream res3 =  JenaEngine.executeQueryFile(inferedModel, "data/query_Liquide.txt");

//            String res = res1.toString() + res2.toString() + res3.toString() ;

            System.out.println(res);
            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/product")
    @CrossOrigin(origins = "http://localhost:3000")
    public String showProduct() {
        String NS = "";
        if (model != null) {
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_product.txt");
            System.out.println(res);
            return res.toString();
        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/productsearch")
    @CrossOrigin(origins = "http://localhost:3000")
    public String showProductSearch(@RequestParam("productName") String productName) {
        String NS = "";
        if (model != null) {
            System.out.println(productName);
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res = JenaEngine.executeQueryFileParams(inferedModel, "data/query_product_name_search.txt",productName);
            System.out.println(res);
            return res.toString();
        } else {
            return "Error when reading model from ontology";
        }
    }
}
