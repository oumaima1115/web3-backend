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
@CrossOrigin("*")
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

    @GetMapping("/JobsList")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getJobs() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_Job.txt");
//            OutputStream res2 =  JenaEngine.executeQueryFile(inferedModel, "data/query_OrigineVegetale.txt");
//            OutputStream res3 =  JenaEngine.executeQueryFile(inferedModel, "data/query_Liquide.txt");

//            String res = res1.toString() + res2.toString() + res3.toString() ;

            System.out.println(res);
            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }

    }

    @GetMapping("/jobsSearch")
    @CrossOrigin(origins = "*")
    public String getJobs(
            @RequestParam(value = "domain", required = false) String domain,
            @RequestParam(value="regexParam", required = false) String regexParam,
            @RequestParam(value ="orderType",required =false )String orderType,
            @RequestParam(value = "orderBy",required =false) String orderBy

    )

             {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            /*OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_Skill.txt");

            System.out.println(res);
            return res.toString();*/

            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_Job.txt");


            // Set the value of ?domainParam
            if (domain != null && !domain.isEmpty()) {
                // Replace the parameter placeholder with the actual domain value
                queryStr = queryStr.replace("?titleParam",  '\"'+domain+'\"' );
            } else {
                // If domain is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?titleParam != \"\" && ?title = ?titleParam)", "");
            }

            if (regexParam != null && !regexParam.isEmpty()) {
                // If regexParam is provided, replace the parameter placeholder
                queryStr = queryStr.replace("?regexParam", '\"' + regexParam + '\"');
            } else {
                // If regexParam is not provided, remove the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?regexParam != \"\" && ?description = ?regexParam)", "");
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
                jsonObject.add("Job", new JsonPrimitive(solution.get("Job").toString()));
                jsonObject.add("title", new JsonPrimitive(solution.get("title").toString()));
                jsonObject.add("description", new JsonPrimitive(solution.get("description").toString()));
                jsonObject.add("type", new JsonPrimitive(solution.get("type").toString()));
                jsonObject.add("address", new JsonPrimitive(solution.get("address").toString()));
                jsonObject.add("salary_range", new JsonPrimitive(solution.get("salary_range").toString()));




                jsonArray.add(jsonObject);
            }

            // Convert the JSON to a string
            String jsonResult = jsonArray.toString();

            System.out.println(jsonResult);
            return jsonResult;

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

    @GetMapping("/events")
    @CrossOrigin(origins = "http://localhost:3000")
    public String afficherEvents() {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            // lire le Namespace de l�ontologie
            NS = model.getNsPrefixURI("");

            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // query on the model after inference
            OutputStream res =  JenaEngine.executeQueryFile(inferedModel, "data/query_OnlineEvent.txt");
//            OutputStream res2 =  JenaEngine.executeQueryFile(inferedModel, "data/query_OrigineVegetale.txt");
//            OutputStream res3 =  JenaEngine.executeQueryFile(inferedModel, "data/query_Liquide.txt");

//            String res = res1.toString() + res2.toString() + res3.toString() ;

            System.out.println(res);
            return res.toString();


        } else {
            return ("Error when reading model from ontology");
        }
    }

    @GetMapping("/skills")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getSkills(
            @RequestParam(value = "domain", required = false) String domain,
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
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_Skill.txt");


            // Set the value of ?domainParam
            if (domain != null && !domain.isEmpty()) {
                // Replace the parameter placeholder with the actual domain value
                queryStr = queryStr.replace("?domainParam",  '\"'+domain+'\"' );
            } else {
                // If domain is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?domainParam != \"\" && ?domain = ?domainParam)", "");
            }

            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam",  '\"'+regexParam+'\"' );
            } else {
                queryStr = queryStr.replace("FILTER regex(?name, ?regexParam, \"i\")", "");
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
                jsonObject.add("skill", new JsonPrimitive(solution.get("skill").toString()));
                jsonObject.add("name", new JsonPrimitive(solution.get("name").toString()));
                jsonObject.add("domain", new JsonPrimitive(solution.get("domain").toString()));
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

    @GetMapping("/questions")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getQuestions(
            @RequestParam(value = "score", required = false) String score,
            @RequestParam(value = "regexParam", required = false) String regexParam,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType
            ) {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_Question.txt");


            // Set the value of ?domainParam
            if (score != null && !score.isEmpty()) {
                // Replace the parameter placeholder with the actual domain value
                queryStr = queryStr.replace("?scoreParam",  score );
            } else {
                // If domain is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?scoreParam != \"\" && ?questionScore = ?scoreParam)", "");
            }

            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam",  '\"'+regexParam+'\"' );
            } else {
                queryStr = queryStr.replace("FILTER regex(?description, ?regexParam, \"i\")", "");
            }

            if (orderBy != null && !orderBy.isEmpty() && orderType != null && !orderType.isEmpty()
                    && (orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC"))) {
                queryStr = queryStr.replace("?orderBy",  '?'+orderBy );
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
                jsonObject.add("question", new JsonPrimitive(solution.get("question").toString()));
                jsonObject.add("description", new JsonPrimitive(solution.get("description").toString()));
                jsonObject.add("questionScore", new JsonPrimitive(solution.getLiteral("questionScore").getInt()));
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

    @GetMapping("/answers")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getAnswers(
            @RequestParam(value = "isCorrect", required = false) String isCorrect,
            @RequestParam(value = "regexParam", required = false) String regexParam,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType
    ) {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_Answer.txt");


            // Set the value of ?domainParam
            if (isCorrect != null && !isCorrect.isEmpty()) {
                // Replace the parameter placeholder with the actual domain value
                queryStr = queryStr.replace("?isCorrect",  isCorrect );
            } else {
                // If domain is not provided, remove the parameter and the FILTER condition from the query
                queryStr = queryStr.replace("FILTER (?isCorrect != \"\" && ?is_correct = ?isCorrect)", "");
            }

            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam",  '\"'+regexParam+'\"' );
            } else {
                queryStr = queryStr.replace("FILTER (regex(?answerDescription, ?regexParam, \"i\") || regex(?questionDescription, ?regexParam, \"i\"))", "");
                //queryStr = queryStr.replace("FILTER regex(?answerDescription, ?regexParam, \"i\")", "");
            }

            if (orderBy != null && !orderBy.isEmpty() && orderType != null && !orderType.isEmpty()
                    && (orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC"))) {
                queryStr = queryStr.replace("?orderBy",  '?'+orderBy );
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
                jsonObject.add("answerDescription", new JsonPrimitive(solution.getLiteral("answerDescription").getString()));
                jsonObject.add("is_correct", new JsonPrimitive(solution.getLiteral("is_correct").getString()));
                jsonObject.add("questionDescription", new JsonPrimitive(solution.get("questionDescription").toString()));
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


    @GetMapping("/quizzes")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getQuizzes(
            @RequestParam(value = "regexParam", required = false) String regexParam,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType
    ) {
        String NS = "";
        // lire le model a partir d'une ontologie
        if (model != null) {
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String queryStr = FileManager.get().readWholeFileAsUTF8("data/query_Quiz.txt");


            if (regexParam != null && !regexParam.isEmpty()) {
                queryStr = queryStr.replace("?regexParam",  '\"'+regexParam+'\"' );
            } else {
                queryStr = queryStr.replace("FILTER (regex(?userName, ?regexParam, \"i\") || regex(?skillName, ?regexParam, \"i\"))", "");
            }

            if (orderBy != null && !orderBy.isEmpty() && orderType != null && !orderType.isEmpty()
                    && (orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC"))) {
                queryStr = queryStr.replace("?orderBy",  '?'+orderBy );
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
                jsonObject.add("score", new JsonPrimitive(solution.getLiteral("score").getDouble()));
                jsonObject.add("playedAt", new JsonPrimitive(solution.getLiteral("playedAt").getString()));
                jsonObject.add("skillName", new JsonPrimitive(solution.get("skillName").toString()));
                jsonObject.add("userName", new JsonPrimitive(solution.get("userName").toString()));
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
}
