package com.example.demo;


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

    @GetMapping("/eventsOnline")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherEventsOnline() {
        String NS = "";
        if (model != null) {
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_Online.txt");
            System.out.println(res);
            return res.toString();
        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/OnsiteEvents")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherEventsOnsite() {
        String NS = "";
        if (model != null) {
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_Onsite.txt");
            System.out.println(res);
            return res.toString();
        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/events")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getOnlineEvents(
            @RequestParam(value = "categoryEvent", required = false) String categoryEvent,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "regexParam", required = false) String regexParam,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType
    ) {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_onLineEvents.txt");


            // Set the value of ?categoryEventParam
            if (categoryEvent != null && !categoryEvent.isEmpty()) {
                // Replace the parameter placeholder with the actual categoryEvent value
                queryStr = queryStr.replace("?categoryEventParam",  '\"'+categoryEvent+'\"' );
            } else {
                // If categoryEvent is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?categoryEventParam != \"\" && ?categoryEvent = ?categoryEventParam)", "");
            }

            // Set the value of ?userNameParam
            if (userName != null && !userName.isEmpty()) {
                // Replace the parameter placeholder with the actual userName value
                queryStr = queryStr.replace("?userNameParam",  '\"'+userName+'\"' );
            } else {
                // If userName is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?userNameParam != \"\" && ?userName = ?userNameParam)", "");
            }

            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam", '\"' + regexParam + '\"');
            } else {
                // If regexParam is not provided, remove the FILTER condition for both title
                queryStr = queryStr.replace("FILTER regex(?title, ?regexParam, \"i\")", "");
            }


            if (orderBy != null && !orderBy.isEmpty() && orderType != null && !orderType.isEmpty()
                    && (orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC"))) {
                queryStr = queryStr.replace("?orderBy",  '?'+orderBy.toLowerCase() );
                queryStr = queryStr.replace("?orderType",  orderType.toUpperCase() );
            } else {
                queryStr = queryStr.replace("ORDER BY ?orderType(?orderBy)", "");
            }

            System.out.println(queryStr);
            // Execute the query
            Query query = QueryFactory.create(queryStr);
            QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel);

            // Execute the query
            ResultSet results = qexec.execSelect();

            JsonArray jsonArray = new JsonArray();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("online", new JsonPrimitive(solution.get("online").toString()));
                jsonObject.add("title", new JsonPrimitive(solution.get("title").toString()));
                jsonObject.add("userName", new JsonPrimitive(solution.get("userName").toString()));
                jsonObject.add("categoryEvent", new JsonPrimitive(solution.get("categoryEvent").toString()));
                jsonArray.add(jsonObject);
            }

            // Convert the JSON to a string
            String jsonResult = jsonArray.toString();

            System.out.println(jsonResult);
            return new ResponseEntity<>(jsonResult, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Error when reading model from ontology", HttpStatus.OK);
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


}
