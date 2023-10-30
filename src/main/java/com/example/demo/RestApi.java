package com.example.demo;


import java.io.OutputStream;

import java.io.OutputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getallproducts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getallproducts(
            @RequestParam(value = "typeParam", required = false) String typeParam,
            @RequestParam(value = "regexParam", required = false) String regexParam,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType
    ) {
        String NS = "";
        if (model != null) {
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_all_products.txt");

            if (typeParam != null && !typeParam.isEmpty()) {
                queryStr = queryStr.replace("?typeParam", '\"'+ typeParam +'\"' );
            } else {
                queryStr = queryStr.replace("FILTER (?typeParam != \"\" && ?type = ?typeParam)", "");
            }
            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam",  '\"'+regexParam+'\"' );
            } else {
                queryStr = queryStr.replace("FILTER regex(?name, ?regexParam, \"i\")", "");
            }
            if (orderBy != null && !orderBy.isEmpty() && orderType != null && !orderType.isEmpty()
                    && (orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC"))) {
                queryStr = queryStr.replace("?orderBy",  '?'+orderBy );
                queryStr = queryStr.replace("?orderType",  orderType.toUpperCase() );
            } else {
                queryStr = queryStr.replace("ORDER BY ?orderType(?orderBy)", "");
            }
            System.out.println(queryStr);
            Query query = QueryFactory.create(queryStr);
            QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel);
            ResultSet results = qexec.execSelect();
            JsonArray jsonArray = new JsonArray();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("name", new JsonPrimitive(solution.getLiteral("name").getString()));
                jsonObject.add("type", new JsonPrimitive(solution.getLiteral("type").getString()));
                jsonObject.add("description", new JsonPrimitive(solution.getLiteral("description").getString()));
                jsonObject.add("price", new JsonPrimitive(solution.getLiteral("price").getDouble()));
                jsonObject.add("quantity", new JsonPrimitive(solution.getLiteral("quantity").getInt()));
                jsonObject.add("date", new JsonPrimitive(solution.getLiteral("date").getString()));
                jsonArray.add(jsonObject);
            }
            String jsonResult = jsonArray.toString();
            System.out.println(jsonResult);
            return new ResponseEntity<>(jsonResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error when reading model from ontology", HttpStatus.OK);
        }
    }

    @GetMapping("/productsearch")
    @CrossOrigin(origins = "http://localhost:3000")
    public String showProductSearch(@RequestParam("productName") String productName) {
        String NS = "";
        if (model != null) {
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res = JenaEngine.executeQueryFileParams(inferedModel, "data/query_product_name_search.txt",productName);
            System.out.println(res);
            return res.toString();
        } else {
            return "Error when reading model from ontology";
        }
    }

    @GetMapping("/productsearchsort")
    @CrossOrigin(origins = "http://localhost:3000")
    public String showProductSearchSort(@RequestParam(name = "productName", required = false, defaultValue = "") String productName,
                                        @RequestParam(name = "type", required = false, defaultValue = "") String type,
                                        @RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy) {

        String sparqlQuery = "PREFIX ns: <http://www.semanticweb.org/rayenbourguiba/ontologies/2023/9/untitled-ontology-4#> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "SELECT * " +
                "WHERE { " +
                "?Product rdf:type ns:Product; " +
                "        ns:name ?name; " +
                "        ns:description ?description; " +
                "        ns:date ?date; " +
                "        ns:price ?price; " +
                "        ns:quantity ?quantity; " +
                "        ns:type ?type;";

        if (productName != null && !productName.isEmpty()) {
            sparqlQuery += " FILTER(regex(?name, \"" + productName + "\", \"i\"))";
        }
        if (type != null && !type.isEmpty()) {
            sparqlQuery += " FILTER(regex(?type, \"" + type + "\", \"i\"))";
        }

// ORDER BY clause should be placed after all FILTER conditions
        if (sortBy != null && !sortBy.isEmpty()) {
            if ("price".equals(sortBy)) {
                sparqlQuery += " } ORDER BY ?price";
            } else if ("quantity".equals(sortBy)) {
                sparqlQuery += " } ORDER BY ?quantity";
            }
        }

        sparqlQuery += " }"; // Close the SPARQL query
        if (model != null) {
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res = JenaEngine.executeQuery(inferredModel, sparqlQuery);
            System.out.println(res);
            return res.toString();
        } else {
            return "Error when reading model from ontology";
        }
    }
}
